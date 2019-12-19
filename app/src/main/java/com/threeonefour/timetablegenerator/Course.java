package com.threeonefour.timetablegenerator;

import javax.security.auth.Subject;

class Course {
    private String code;
    private String name;
    private Subject[] generalSubs;
    private Subject[] codeSubs;
    private Major major;
}
