package me.yaoandy107.ntut_timetable.model;

/**
 * Created by blackmaple on 2017/5/4.
 */

public class CourseInfo {
    private String name = null;
    private String[] times = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getTimes() {
        return times;
    }

    public void setCourseTime(String[] courseTimes) {
        this.times = courseTimes;
    }

    public void setCourseTime(String sunday, String monday, String tuesday, String wednesday, String thursday, String friday, String saturday) {
        this.times = new String[]{sunday, monday, tuesday, wednesday, thursday, friday, saturday};
    }
}