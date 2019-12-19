package com.threeonefour.timetablegenerator;

import java.util.ArrayList;

public class Timetable {
    private ArrayList<String> subs = new ArrayList<>();//sub codes
    private ArrayList<ArrayList<CClass>> classes = new ArrayList<>();//each row(i) will have the classes for the corresponding (i) in the subs array
    /*
     * {lecture, tut, lab}
     * {lecture, tut, lab}
     * {lecture, tut, lab}
     * {lecture, tut, lab}
     * */

    public Timetable(){

    }

    public Timetable(Timetable t1){
        this.subs = new ArrayList<>(t1.subs);
        this.classes = new ArrayList<>(t1.classes);
    }

    public Timetable(ArrayList<String> subs, ArrayList<ArrayList<CClass>> classes) {
        this.subs = subs;
        this.classes = classes;
    }

    public ArrayList<String> getSubs() { return subs; }
    public ArrayList<ArrayList<CClass>> getClasses() { return classes; }

    public void setSubs(ArrayList<String> subs) { this.subs = subs; }
    public void setClasses(ArrayList<ArrayList<CClass>> classes) { this.classes = classes; }
}
