package com.threeonefour.timetablegenerator;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class CClass
{
    private String      subj;
    private char        classType;
    private char        code;
    private String      roomNum;
    private String      profName;
    private DayOfWeek   day;
    private LocalTime   startTime;
    private LocalTime   endTime;

    //Constructors
    public CClass()
    {
        subj        = null;
        classType   = '\0';
        code        = '\0';
        roomNum     = null;
        day         = null;
        startTime   = null;
        endTime     = null;
    }

    public CClass(String subj, char classType, char code, String roomNum, String profName,
                  DayOfWeek day, LocalTime startTime, LocalTime endTime)
    {
        this.subj       = subj;
        this.classType  = classType;
        this.code       = code;
        this.roomNum    = roomNum;
        this.profName   = profName;
        this.day        = day;
        this.startTime  = startTime;
        this.endTime    = endTime;
    }

    //GETTERS
    public String       getSubj()       { return subj; }
    public char         getClassType()  { return classType; }
    public char         getCode()       { return code; }
    public String       getRoomNum()    { return roomNum; }
    public String       getProfName()   { return profName; }
    public DayOfWeek    getDay()        { return day; }
    public LocalTime    getStartTime()  { return startTime; }
    public LocalTime    getEndTime()    { return endTime; }

    //SETTERS
    public void setSubj         (String subj)           { this.subj = subj; }
    public void setClassType    (char classType)        { this.classType = classType; }
    public void setCode         (char code)             { this.code = code; }
    public void setRoomNum      (String roomNum)        { this.roomNum = roomNum; }
    public void setProfName     (String profName)       { this.profName = profName; }
    public void setDay          (DayOfWeek day)         { this.day = day; }
    public void setStartTime    (LocalTime startTime)   { this.startTime = startTime; }
    public void setEndTime      (LocalTime endTime)     { this.endTime = endTime; }

    //METHODS
    @Override
    public String toString() {
        String output = subj + " " + classType + code + ", " + roomNum + ", " + profName + ", "
                + day + ", Start: " + startTime + ", End: " + endTime;
        return output;
    }
}
