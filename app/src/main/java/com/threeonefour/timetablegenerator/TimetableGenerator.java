package com.threeonefour.timetablegenerator;

//region Import Statements
import android.content.Context;
import android.util.Log;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
//endregion

public class TimetableGenerator {

    static ArrayList<Subject>   subjects =  new ArrayList<>();//Stores all subjects in the course //get from Vishal
    static ArrayList<CClass>     classes =   new ArrayList<>();//Stores classes
    static ArrayList<Subject>   semSubs =   new ArrayList<>();//Stores subjects and class times offered in the semester
    static ArrayList<Timetable> tts = new ArrayList<>();
    static ArrayList<ArrayList<Subject>> combinations = new ArrayList<>();

//------MAIN------//
//    public static void main(String[] args) throws IOException, CloneNotSupportedException {
//        //-----PROGRAM
//        updateSubjectList();
//        for (Subject subject : subjects) System.out.println(subject);
//        System.out.println();
//
//        updateTimetable();
//        for (Class aClass : classes) {
//            System.out.println(aClass);
//        }
//        System.out.println();
//
//        updateCurrentSubjects();
//        for (Subject semSub : semSubs) { System.out.println(semSub); }
//        System.out.println();
//
//        addClassesToSubject();
//        for (Subject semSub : semSubs)
//            for (int j = 0; j < semSub.getClasses().length; j++)
//                for (int k = 0; k < semSub.getClasses()[j].size(); k++)
//                    System.out.println(semSub.getClasses()[j].get(k));
//        System.out.println();
//
//        //TESTING doesClash(Subject s1, s2)
////        doesClash(semSubs.get(0), semSubs.get(1));
////        System.out.println(doesClash(semSubs.get(0), semSubs.get(1)));
//
//        int numberOfSubs = 3;
//        subjectCombos(numberOfSubs);
//        System.out.println(combinations.size());
//        //TESTING doesClash(ArrayList<Subject> subs)
//        ArrayList<Subject> subs = new ArrayList<>();
//        subs.add(semSubs.get(1));
//        subs.add(semSubs.get(9));
//        subs.add(semSubs.get(10));
//        //System.out.println(doesClash(subs));
//
//        //tts = generateTimetables(combinations.get(0));
//        tts = generateTimetables(subs);
//        System.out.println(tts.get(0));
//
//    }
//-------END OF MAIN-------//

    //todo add a way to make it only read if the file version is different from what was previously read
    public static void updateSubjectList() throws IOException{
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
    }

    public static void updateTimetable(Context ctx) throws IOException{

        classes.clear();

//        src/main/java/com/threeonefour/timetablegenerator/
//        Scanner = new Scanner(new File(.open("Timetable_AUT2k19.csv")));
        Scanner ttReader = new Scanner(ctx.getAssets().open(String.format("Timetable_AUT2k19.csv")));
        Log.d("UPDATE_TIMETABLE", "TEST TEST TEST TESTTESTTEST");
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
    }

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
//        System.out.println("semSubjects<> populated");
    }

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
    }

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
    }

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
    }

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
    }

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
                s1id=genID(brute.get(0).get(i)[0]);
                s2id=genID(brute.get(0).get(i)[1]);
                for (int j = 0; j < brute.get(1).size(); j++) {
                    String checkid = genID(brute.get(1).get(j)[0]);
                    if(checkid.equals(s1id)){
                        s3id=genID(brute.get(1).get(j)[1]);
                        for (int k = 0; k < brute.get(2).size(); k++) {
                            String checkid2 = genID(brute.get(2).get(k)[0]);
                            String checkid3 = genID(brute.get(2).get(k)[1]);
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
    }

    public static String genID(ArrayList<CClass> c){
        String id = "";
        for (int i = 0; i < c.size(); i++) {
            id+=c.get(i).getCode();
            id+=c.get(i).getStartTime();
            id+=c.get(i).getEndTime();
        }
        return id;
    }

    public static void subjectCombos(int n){
        ArrayList<Subject> temp = new ArrayList<Subject>();
        for (int i = 0; i < subjects.size(); i++) {
            for (int j = 0; j < subjects.size(); j++) {
                for (int k = 0; k < subjects.size(); k++) {
                    if(i==j||i==k||j==k){
                        continue;
                    } else {
                        temp.add(subjects.get(i));
                        temp.add(subjects.get(j));
                        temp.add(subjects.get(k));
                    }
                }
            }

            if(!doesClash(temp)){
                combinations.add(new ArrayList<>(temp));
            }
            temp.clear();
        }
    }
}
