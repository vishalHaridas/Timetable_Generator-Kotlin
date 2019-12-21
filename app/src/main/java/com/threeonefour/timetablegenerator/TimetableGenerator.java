package com.threeonefour.timetablegenerator;

//region Import Statements
import android.content.Context;
import android.util.Log;

//region Import Statements
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.io.FileReader;
import java.io.IOException;
//endregion

public class TimetableGenerator {

    static ArrayList<Subject>   subjects = new ArrayList<>();//Stores all subjects in the course //get from Vishal
    static ArrayList<CClass>    classes  = new ArrayList<>();//Stores classes
    static ArrayList<Subject>   semSubs  = new ArrayList<>();//Stores subjects and class times offered in the semester
    static ArrayList<Timetable> tts      = new ArrayList<>();
    static ArrayList<ArrayList<Subject>> combinations = new ArrayList<>();
    static ArrayList<String>    completedSubCodes = new ArrayList<>();
    //static ArrayList<Subject> completedSubs = new ArrayList<>();

    //------MAIN------//
    public static void main(String[] args) throws IOException, CloneNotSupportedException {
        completedSubCodes.add("CSIT111");
        completedSubCodes.add("CSIT113");
        completedSubCodes.add("CSIT121");
        completedSubCodes.add("CSIT115");
        completedSubCodes.add("CSIT128");
        completedSubCodes.add("CSIT114");
        completedSubCodes.add("MATH070");
        completedSubCodes.add("STAT131");

        //-----PROGRAM
        updateSubjectList();
        for (Subject subject : subjects) System.out.println(subject);
        System.out.println();

        updateTimetable();
        for (CClass aClass : classes) {
            System.out.println(aClass);
        }
        System.out.println();

        updateCurrentSubjects();
        for (Subject semSub : semSubs) { System.out.println(semSub); }
        System.out.println();

        addClassesToSubject();
        for (Subject semSub : semSubs)
            for (int j = 0; j < semSub.getClasses().length; j++)
                for (int k = 0; k < semSub.getClasses()[j].size(); k++)
                    System.out.println(semSub.getClasses()[j].get(k));
        System.out.println();

        markCompletedSubs();
        removeCompletedSubs();
        for (int i = 0; i < semSubs.size(); i++) {
            System.out.println(semSubs.get(i));
        }

        removeIneligibleSubs();//might not be working
        System.out.println();

        int numberOfSubs = 3;//todo remove from android app. replace with num that user selects
        genSubCombos(numberOfSubs);//no test required. testing code can be found inside func code

        tts = generateTimetables(combinations.get(22));//todo can modify func to directly write to static arraylist
        System.out.println(tts.size());
        System.out.println(tts.get(1));

    }

//-------END OF MAIN-------//

    //todo add a way to make it only read if the file version is different from what was previously read
    public static void updateSubjectList() throws IOException{
        subjects.clear();
        Scanner subReader = new Scanner(new FileReader("src/com/AVK/Files/AllSubjects.csv"));
        subReader.useDelimiter("[,\n]");
        String tempCode, tempSubName;
        while (subReader.hasNext()){
            tempCode = subReader.next();
            tempSubName = subReader.next();
            subjects.add(new Subject(tempCode, tempSubName, false));
        }
        subReader.close();
        System.out.println("Subject list updated successfully");
    }//works

    public static void updateTimetable() throws IOException{
        classes.clear();
        Scanner ttReader = new Scanner(new FileReader("src/com/AVK/Files/Timetable_AUT2k19.csv"));
        ttReader.useDelimiter("[,\n]");
        String subCode, roomNum, profName, day, start, end;
        char classType, classCode;
        DayOfWeek tempDay;
        LocalTime tempStart, tempEnd;
        while(ttReader.hasNext()){
            subCode = ttReader.next();
            classType = ttReader.next().charAt(0);
            classCode = ttReader.next().charAt(0);
            roomNum = ttReader.next();
            profName = ttReader.next();
            day = ttReader.next();
            start = ttReader.next();
            end = ttReader.next();
            switch (day){
                case "Sunday":
                    tempDay = DayOfWeek.SUNDAY;
                    break;
                case "Monday":
                    tempDay = DayOfWeek.MONDAY;
                    break;
                case "Tuesday":
                    tempDay = DayOfWeek.TUESDAY;
                    break;
                case "Wednesday":
                    tempDay = DayOfWeek.WEDNESDAY;
                    break;
                case "Thursday":
                    tempDay = DayOfWeek.THURSDAY;
                    break;
                case "Friday":
                    tempDay = DayOfWeek.FRIDAY;
                    break;
                case "Saturday":
                    tempDay = DayOfWeek.SATURDAY;
                    break;
                default:
                    tempDay = DayOfWeek.FRIDAY;
                    break;
            }
            end = end.replaceAll("\r", "").replaceAll("\n", "");
            tempStart = LocalTime.parse(start);
            tempEnd = LocalTime.parse(end);
            classes.add(new CClass(subCode, classType, classCode, roomNum, profName, tempDay, tempStart, tempEnd));
        }
        System.out.println("Classes<> populated successfully");
    }//works

    public static void updateCurrentSubjects(){
        semSubs.clear();
        String subCode;
        int subIndex = -1;
        Subject temp;
        for (CClass aClass : classes) {
            subCode = aClass.getSubj();
            for (int j = 0; j < subjects.size(); j++) {
                if (subjects.get(j).getCode().equals(subCode)) {
                    subIndex = j;
                }
            }
            if (subIndex == -1) {
                continue;
            }
            temp = subjects.get(subIndex);
            if (semSubs.isEmpty()) {
                semSubs.add(temp);
            } else {
                if (!semSubs.contains(subjects.get(subIndex))) {
                    semSubs.add(subjects.get(subIndex));
                }
            }
        }
        System.out.println("semSubjects<> populated");
    }//works

    public static void addClassesToSubject(){
        int subClassRow;
        String subjCode;
        CClass tempClass;

        for (CClass aClass : classes) {
            subjCode = aClass.getSubj();
            switch (aClass.getClassType()) {
                case 'L':
                    subClassRow = 0;
                    break;
                case 'T':
                    subClassRow = 1;
                    break;
                case 'C':
                    subClassRow = 2;
                    break;
                default:
                    System.out.println("invalid");
                    subClassRow = 2;
                    break;
            }
            tempClass = aClass;
            for (Subject semSub : semSubs) {
                if (semSub.getCode().equals(subjCode)) {
                    semSub.getClasses()[subClassRow].add(tempClass);
                    break;
                }
            }
        }
        System.out.println("Classes added to semSubjects<>");
    }//works

    public static boolean doesClash(CClass c1, CClass c2){
        boolean clashes;
        if(c1.getDay().equals(c2.getDay())) {
            if(c1.getStartTime().equals(c2.getStartTime()))
                clashes = true;

            else if (c1.getStartTime().isBefore(c2.getStartTime()))
            {
                if (c2.getStartTime().isBefore(c1.getEndTime())) {clashes = true;}
                else if(c1.getEndTime().equals(c2.getStartTime())) { clashes = false; }
                else{ clashes = false; }
            }

            else if (c2.getStartTime().isBefore(c1.getStartTime()))
            {
                if (c1.getStartTime().isBefore(c2.getEndTime())) { clashes= true; }
                else if(c2.getEndTime().equals(c1.getStartTime())) { clashes= false; }
                else{ clashes= false; }
            }

            else {
                clashes = false;
            }
        }
        else { clashes= false; }
        return clashes;
    }//works

    public static ArrayList<ArrayList<CClass>[]> doesClash(Subject s1, Subject s2){
        boolean impossible = false;
        ArrayList<CClass[]> lecCombos = new ArrayList<>();
        ArrayList<CClass[]> tutCombos = new ArrayList<>();
        ArrayList<CClass[]> labCombos = new ArrayList<>();
        ArrayList<ArrayList<CClass>[]> workingCombos = new ArrayList<>();
        ArrayList<CClass>[] workingCombo = new ArrayList[2];
        ArrayList<CClass> sub1_combo = new ArrayList<>();
        ArrayList<CClass> sub2_combo = new ArrayList<>();
        CClass[] combo = new CClass[2];

        //s1 and s2 have labs
        if(s1.getClasses()[2].size()>0 && s2.getClasses()[2].size()>0){
            for (int i=0; i<Math.max(s1.getClasses().length, s2.getClasses().length); i++){
                for (int j=0; j<s1.getClasses()[i].size(); j++){
                    for (int k=0; k<s2.getClasses()[i].size(); k++){
                        if(!doesClash(s1.getClasses()[i].get(j), s2.getClasses()[i].get(k))){
                            combo[0] = s1.getClasses()[i].get(j);
                            combo[1] = s2.getClasses()[i].get(k);
                            switch (i){
                                case 0:
                                    lecCombos.add(combo.clone());
                                    break;
                                case 1:
                                    tutCombos.add(combo.clone());
                                    break;
                                case 2:
                                    labCombos.add(combo.clone());
                                    break;
                                default:
                                    System.out.println("switch failed");
                                    break;
                            }
                        }
                    }
                }
            }
            if(lecCombos.size()==0 || tutCombos.size()==0 || labCombos.size()==0){
//                impossible = true;
//                return impossible;
                workingCombos.clear();
                return workingCombos;
            } if ((s1.getClasses().length==2 || s2.getClasses().length==2)&&labCombos.size()==0) {
//                impossible = true;
//                return impossible;
                workingCombos.clear();
                return workingCombos;
            }
            for(int i=0; i<lecCombos.size(); i++) {
                for (int j = 0; j < tutCombos.size(); j++) {
                    if (!doesClash(lecCombos.get(i)[0], tutCombos.get(j)[1])) {
                        for (int k = 0; k < labCombos.size(); k++) {
                            if (!doesClash(lecCombos.get(i)[0], labCombos.get(k)[1])) {
                                if (!doesClash(tutCombos.get(j)[0], lecCombos.get(i)[1]) && !doesClash(tutCombos.get(j)[0], labCombos.get(k)[1])) {
                                    if (!doesClash(labCombos.get(k)[0], lecCombos.get(i)[1]) && !doesClash(labCombos.get(k)[0], tutCombos.get(j)[1])) {
                                        sub1_combo.add(lecCombos.get(i)[0]);
                                        sub1_combo.add(tutCombos.get(i)[0]);
                                        sub1_combo.add(labCombos.get(i)[0]);
                                        sub2_combo.add(lecCombos.get(i)[1]);
                                        sub2_combo.add(tutCombos.get(j)[1]);
                                        sub2_combo.add(labCombos.get(k)[1]);
                                        workingCombo[0] = new ArrayList<>(sub1_combo)/*sub1_combo*/;
                                        workingCombo[1] = new ArrayList<>(sub2_combo);
                                        workingCombos.add(workingCombo.clone());
                                        sub1_combo.clear();
                                        sub2_combo.clear();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        //s1 has labs and s2 doesn't
        else if (s1.getClasses()[2].size()>0 && s2.getClasses()[2].size()==0){
            for (int i=0; i<Math.max(s1.getClasses().length, s2.getClasses().length); i++){
                for (int j=0; j<s1.getClasses()[i].size(); j++){
                    for (int k=0; k<s2.getClasses()[i].size(); k++){
                        if(!doesClash(s1.getClasses()[i].get(j), s2.getClasses()[i].get(k))){
                            switch (i){
                                case 0:
                                    combo[0] = s1.getClasses()[i].get(j);
                                    combo[1] = s2.getClasses()[i].get(k);
                                    lecCombos.add(combo.clone());
                                    break;
                                case 1:
                                    combo[0] = s1.getClasses()[i].get(j);
                                    combo[1] = s2.getClasses()[i].get(k);
                                    tutCombos.add(combo.clone());
                                    break;
                                case 2:
                                    combo[0] = s1.getClasses()[i].get(j);
                                    combo[1] = null;
                                    labCombos.add(combo.clone());
                                    break;
                                default:
                                    System.out.println("switch failed");
                                    break;
                            }
//                            combo[0] = s1.getClasses()[i].get(j);
//                            labCombos.add(combo.clone());
                        }

                    }
                    if (i==2){
                        combo[0] = s1.getClasses()[i].get(j);
                        combo[1] = null;
                        labCombos.add(combo.clone());
                    }
                }
            }
            if(lecCombos.size()==0 || tutCombos.size()==0 /*|| labCombos.size()==0*/){
//                impossible = true;
//                return impossible;
                workingCombos.clear();
                return workingCombos;
            } if ((s1.getClasses().length==2 || s2.getClasses().length==2)&&labCombos.size()==0) {
//                impossible = true;
//                return impossible;
                workingCombos.clear();
                return workingCombos;
            }
            for(int i=0; i<lecCombos.size(); i++) {
                for (int j = 0; j < tutCombos.size(); j++) {
                    if (!doesClash(lecCombos.get(i)[0], tutCombos.get(j)[1])) {
                        for (int k = 0; k < labCombos.size(); k++) {
                            if (!doesClash(tutCombos.get(j)[0], lecCombos.get(i)[1])) {
                                if (!doesClash(labCombos.get(k)[0], lecCombos.get(i)[1]) && !doesClash(labCombos.get(k)[0], tutCombos.get(j)[1])) {
                                    sub1_combo.add(lecCombos.get(i)[0]);
                                    sub1_combo.add(tutCombos.get(j)[0]);
                                    sub1_combo.add(labCombos.get(k)[0]);
                                    sub2_combo.add(lecCombos.get(i)[1]);
                                    sub2_combo.add(tutCombos.get(j)[1]);
                                    workingCombo[0] = new ArrayList<>(sub1_combo);
                                    workingCombo[1] = new ArrayList<>(sub2_combo);
                                    workingCombos.add(workingCombo.clone());
                                    sub1_combo.clear();
                                    sub2_combo.clear();
                                }
                            }
                        }
                    }
                }
            }
        }
        //s2 has labs and s1 doesn't
        else if (s1.getClasses()[2].size()==0 && s2.getClasses()[2].size()>0){
            for (int i=0; i<Math.max(s1.getClasses().length, s2.getClasses().length); i++){
                for (int j=0; j<s1.getClasses()[i].size(); j++){
                    for (int k=0; k<s2.getClasses()[i].size(); k++){
                        if(!doesClash(s1.getClasses()[i].get(j), s2.getClasses()[i].get(k))){
                            switch (i){
                                case 0:
                                    combo[0] = s1.getClasses()[i].get(j);
                                    combo[1] = s2.getClasses()[i].get(k);
                                    lecCombos.add(combo.clone());
                                    break;
                                case 1:
                                    combo[0] = s1.getClasses()[i].get(j);
                                    combo[1] = s2.getClasses()[i].get(k);
                                    tutCombos.add(combo.clone());
                                    break;
                                case 2:
                                    combo[1] = s2.getClasses()[i].get(k);
                                    break;
                                default:
                                    System.out.println("switch failed");
                                    break;
                            }
                            combo[0] = s1.getClasses()[i].get(j);
                            labCombos.add(combo.clone());
                        }
                        if (i==2){
                            combo[0] = s1.getClasses()[i].get(k);
                            combo[1] = null;
                            labCombos.add(combo.clone());
                        }
                    }

                }
            }
            if(lecCombos.size()==0 || tutCombos.size()==0 /*|| labCombos.size()==0*/){
//                impossible = true;
//                return impossible;
                workingCombos.clear();
                return workingCombos;
            } if ((s1.getClasses().length==2 || s2.getClasses().length==2)&&labCombos.size()==0) {
//                impossible = true;
//                return impossible;
                workingCombos.clear();
                return workingCombos;
            }
            for(int i=0; i<lecCombos.size(); i++) {
                for (int j = 0; j < tutCombos.size(); j++) {
                    if (!doesClash(lecCombos.get(i)[0], tutCombos.get(j)[1])) {
                        for (int k = 0; k < labCombos.size(); k++) {
                            if (!doesClash(lecCombos.get(i)[0], labCombos.get(k)[1])) {
                                if (!doesClash(tutCombos.get(j)[0], lecCombos.get(i)[1]) && !doesClash(tutCombos.get(j)[0], labCombos.get(k)[1])) {
                                    sub1_combo.add(lecCombos.get(i)[0]);
                                    sub1_combo.add(tutCombos.get(j)[0]);
                                    sub2_combo.add(lecCombos.get(i)[1]);
                                    sub2_combo.add(tutCombos.get(j)[1]);
                                    sub2_combo.add(labCombos.get(k)[1]);
                                    workingCombo[0] = new ArrayList<>(sub1_combo);
                                    workingCombo[1] = new ArrayList<>(sub2_combo);
                                    workingCombos.add(workingCombo.clone());
                                    sub1_combo.clear();
                                    sub2_combo.clear();
                                }
                            }
                        }
                    }
                }
            }
        }
        //both don't have labs
        else {
            for (int i=0; i<Math.max(s1.getClasses().length, s2.getClasses().length); i++){
                for (int j=0; j<s1.getClasses()[i].size(); j++){
                    for (int k=0; k<s2.getClasses()[i].size(); k++){
                        if(!doesClash(s1.getClasses()[i].get(j), s2.getClasses()[i].get(k))){
                            combo[0] = s1.getClasses()[i].get(j);
                            combo[1] = s2.getClasses()[i].get(k);
                            switch (i){
                                case 0:
                                    lecCombos.add(combo.clone());
                                    break;
                                case 1:
                                    tutCombos.add(combo.clone());
                                    break;
                                default:
                                    System.out.println("switch failed");
                                    break;
                            }
                        }
                    }
                }
            }
            if(lecCombos.size()==0 || tutCombos.size()==0){
//                impossible = true;
//                return impossible;
                workingCombos.clear();
                return workingCombos;
            }
//            if ((s1.getClasses().length==2 || s2.getClasses().length==2)&&labCombos.size()==0) {
//                impossible = true;
//                return impossible;
//            }
            for(int i=0; i<lecCombos.size(); i++) {
                for (int j = 0; j < tutCombos.size(); j++) {
                    if (!doesClash(lecCombos.get(i)[0], tutCombos.get(j)[1])) {
                        if (!doesClash(tutCombos.get(j)[0], lecCombos.get(i)[1])){
                            sub1_combo.add(lecCombos.get(i)[0]);
                            sub1_combo.add(tutCombos.get(j)[0]);
                            sub2_combo.add(lecCombos.get(i)[1]);
                            sub2_combo.add(tutCombos.get(j)[1]);
                            workingCombo[0] = new ArrayList<>(sub1_combo);
                            workingCombo[1] = new ArrayList<>(sub2_combo);
                            workingCombos.add(workingCombo.clone());
                            sub1_combo.clear();
                            sub2_combo.clear();
                        }
                    }
                }
            }
        }

//        return impossible;
        return workingCombos;
    }//works

    public static boolean doesClash(ArrayList<Subject> subs){
        boolean clashes = false;
        for (int i=0; i<subs.size(); i++){
            for (int j=1; j<subs.size(); j++){
                if (i==j){
                    continue;
                } else {
                    if (doesClash(subs.get(i), subs.get(j)).size()==0){
                        clashes=true;
                        return true;
                    }
                }
            }
        }
        return false;
    }//works

    public static ArrayList<Timetable> generateTimetables(ArrayList<Subject> subs) throws CloneNotSupportedException{
        ArrayList<Timetable> tts = new ArrayList<>();
        ArrayList<ArrayList<ArrayList<CClass>[]>> brute = new ArrayList<>();
        String s1id, s2id, s3id, s4id;
        Timetable temp = new Timetable();
        for (int i=0; i<subs.size()-1; i++){
            for (int j=1; j<subs.size(); j++){
                if (i==j){
                    continue;
                } else {
                    brute.add(new ArrayList<>(doesClash(subs.get(i), subs.get(j))));
                }
            }
        }
        if(subs.size()==3){
            for (int i = 0; i < brute.get(0).size(); i++) {
                //s1id=brute.get(0).get(i)[0].toString();
                //s2id=brute.get(0).get(i)[1].toString();
                s1id=genClassID(brute.get(0).get(i)[0]);
                s2id=genClassID(brute.get(0).get(i)[1]);
                for (int j = 0; j < brute.get(1).size(); j++) {
                    String checkid = genClassID(brute.get(1).get(j)[0]);
                    if(checkid.equals(s1id)){
                        s3id=genClassID(brute.get(1).get(j)[1]);
                        for (int k = 0; k < brute.get(2).size(); k++) {
                            String checkid2 = genClassID(brute.get(2).get(k)[0]);
                            String checkid3 = genClassID(brute.get(2).get(k)[1]);
                            if(checkid2.equals(s2id) && checkid3.equals(s3id)){
                                //sub 1
                                temp.getSubs().add(subs.get(0).getCode());
                                temp.getClasses().add(new ArrayList<>(brute.get(0).get(i)[0]));
                                //sub 2
                                temp.getSubs().add(subs.get(1).getCode());
                                temp.getClasses().add(new ArrayList<>(brute.get(0).get(i)[1]));
                                //sub 3
                                temp.getSubs().add(subs.get(2).getCode());
                                temp.getClasses().add(new ArrayList<>(brute.get(1).get(j)[1]));
                                tts.add(new Timetable(temp));
                                temp.getSubs().clear();
                                temp.getClasses().clear();
                            }
                        }
                    }
                }
            }
        }

        return tts;
    }//works

    public static String genClassID(ArrayList<CClass> c){
        String id = "";
        for (int i = 0; i < c.size(); i++) {
            id+=c.get(i).getCode();
            id+=c.get(i).getStartTime();
            id+=c.get(i).getEndTime();
        }
        return id;
    }//works

    public static void markCompletedSubs(){
        for (int i = 0; i < completedSubCodes.size(); i++) {
            for (int j = 0; j < subjects.size(); j++) {
                if(completedSubCodes.get(i).equals(subjects.get(j).getCode())){
                    subjects.get(j).setComplete(true);
                }
            }
        }
        System.out.println("Completed subs marked");
    }//works

    public static void removeCompletedSubs(){
        for (int i = 0; i < completedSubCodes.size(); i++) {
            for (int j = 0; j < semSubs.size(); j++) {
                if(completedSubCodes.get(i).equals(semSubs.get(j).getCode())){
                    semSubs.get(j).setComplete(true);
                    semSubs.remove(j);
                }
            }
        }
        System.out.println("Completed subs removed");
    }//works

    public static void removeIneligibleSubs(){
        for (int i = 0; i < semSubs.size(); i++) {
            for (int j = 0; j < semSubs.get(i).getPrereqs().size(); j++) {
                for (int k = 0; k < subjects.size(); k++) {
                    if(semSubs.get(i).getPrereqs().get(j).equals(subjects.get(k).getCode())){
                        if(!subjects.get(k).isComplete()){
                            semSubs.remove(i);
                        }
                    }
                }
            }
        }
        System.out.println("Ineligible subjects removed");
    }//NOT SURE

    public static void genSubCombos(int n){
        ArrayList<Subject> temp = new ArrayList<Subject>();
        for (int i = 0; i < semSubs.size(); i++) {
            for (int j = 0; j < semSubs.size(); j++) {
                if (i==j) continue;
                for (int k = 0; k < semSubs.size(); k++) {
                    if(i==k||j==k){
                        continue;
                    } else {
                        temp.add(semSubs.get(i));
                        temp.add(semSubs.get(j));
                        temp.add(semSubs.get(k));
                        Collections.sort(temp);
                        if(!doesClash(temp)){
                            if(!combinations.contains(temp)){
                                combinations.add(new ArrayList<>(temp));
                            }
                        }
                        temp.clear();
                    }
                }
            }
        }
        //region FOR TESTING
//        int count = 0;
//        for (int i = 0; i < combinations.size(); i++) {
//            System.out.print(count + " ");
//            for (int j = 0; j < combinations.get(i).size(); j++) {
//                System.out.print(combinations.get(i).get(j).getCode() + " ");
//            }
//            count++;
//            System.out.println();
//        }
        //endregion
    }//works
}