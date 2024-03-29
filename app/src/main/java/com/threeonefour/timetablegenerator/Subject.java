package com.threeonefour.timetablegenerator;

import java.util.ArrayList;

import java.util.ArrayList;

public class Subject implements Comparable<Subject>
{
    private String              code;
    private String              subName;
    private ArrayList<CClass>[]  classes; //row 0 for lec. times, 1 for tut. , 2 for labs
    private ArrayList<String>  prereqs;
    private boolean             isComplete;

    //CONSTRUCTORS
    //todo default constructor

    public Subject(String code, String subName, boolean isComplete)
    {
        this.code       = code;
        this.subName    = subName;
        this.isComplete = isComplete;

        this.classes = new ArrayList[3];
        for (int i=0; i<classes.length; i++){
            classes[i] = new ArrayList<CClass>(1);
        }
        this.prereqs = new ArrayList<String>();
    }

    public Subject(Subject s1){
        this.code = s1.code;
        this.subName = s1.subName;
        this.isComplete = s1.isComplete;
        this.classes = s1.getClasses().clone();
        this.prereqs = new ArrayList(s1.prereqs);
    }

    //GETTERS
    public String               getCode()       { return code; }
    public String               getSubName()    { return subName; }
    public ArrayList<CClass>[]   getClasses()    { return classes; }
    public ArrayList<String>    getPrereqs()    { return prereqs; }
    public boolean              isComplete()    { return isComplete; }

    //SETTERS
    public void setCode     (String code)                   { this.code = code; }
    public void setSubName  (String subName)                { this.subName = subName; }
    public void setClasses  (ArrayList<CClass>[] classes)    { this.classes = classes; }
    public void setPrereqs  (ArrayList<String> prereqs)     { this.prereqs = prereqs; }
    public void setComplete (boolean complete)              { isComplete = complete; }

    //METHODS

    @Override
    public String toString() {
        return code + " " + subName;
    }


    @Override
    public int compareTo(Subject s) {
        return (this.code.compareTo(s.code));
    }
}
