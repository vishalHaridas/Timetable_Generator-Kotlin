package com.threeonefour.timetablegenerator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import me.yaoandy107.ntut_timetable.CourseTableLayout;
import me.yaoandy107.ntut_timetable.model.CourseInfo;
import me.yaoandy107.ntut_timetable.model.StudentCourse;

public class TimeTableViewerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table_viewer);


        Toolbar timeTableViewToolbar = (Toolbar) findViewById(R.id.timeTableViewToolbar);
        setSupportActionBar(timeTableViewToolbar);
//        getActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
    }

    private void initView() {
        CourseTableLayout courseTable = findViewById(R.id.courseTable);
        StudentCourse studentCourse = new StudentCourse();
        ArrayList<CourseInfo> courseInfoList = new ArrayList<>();

        // Add course1 - sample1
        
//        for (int i = 0; i < TimetableGenerator.tts.size(); i++){
//            for (int j = 0; j < TimetableGenerator.tts.get(i).getClasses().size(); j++) {
//                for (int k = 0; k < TimetableGenerator.tts.get(i).getClasses().get(j).size(); k++) {
//
//                }
//            }
//        }
        
        
        CustomCourseInfo customCourseInfo = new CustomCourseInfo();
        customCourseInfo.setName(TimetableGenerator.tts.get(0).getSubs().get(0));
        customCourseInfo.setCourseTime("1 2 5 9", "", "2", "3", "4", "", "");
        customCourseInfo.setLoaction("Taipei");
        customCourseInfo.setId("01");
        customCourseInfo.setTeacher("OuO");
        courseInfoList.add(customCourseInfo);


//        CustomCourseInfo customCourseInfo = new CustomCourseInfo();


//        // Add course1 - sample2
//        CustomCourseInfo customCourseInfo1 = new CustomCourseInfo();
//        customCourseInfo1.setName("Course 2");
//        customCourseInfo1.setCourseTime(new String[]{"4", "5", "3", "6 7 8", "", "", ""});
//        customCourseInfo1.setLoaction("Taiwan");
//        customCourseInfo1.setId("02");
//        customCourseInfo1.setTeacher("OωO");
//        courseInfoList.add(customCourseInfo1);
//
//        // Add course1 - sample3
//        CustomCourseInfo customCourseInfo2 = new CustomCourseInfo();
//        customCourseInfo2.setName("Course 3");
//        customCourseInfo2.setCourseTime("5 6", "3 4", "", "7 9", "", "", "");
//        customCourseInfo2.setLoaction("Kaohsiung");
//        customCourseInfo2.setId("03");
//        customCourseInfo2.setTeacher("OnO");
//        courseInfoList.add(customCourseInfo2);
//
//        // Add course1 - sample4
//        CustomCourseInfo customCourseInfo3 = new CustomCourseInfo();
//        customCourseInfo3.setName("Course 4");
//        customCourseInfo3.setCourseTime("", "1 2", "7 8", "", "5 6", "", "");
//        customCourseInfo3.setLoaction("Hualien");
//        customCourseInfo3.setId("04");
//        customCourseInfo3.setTeacher("OwO");
//        courseInfoList.add(customCourseInfo3);
//
//        // Add course1 - sample5
//        CustomCourseInfo customCourseInfo4 = new CustomCourseInfo();
//        customCourseInfo4.setName("Course 5");
//        customCourseInfo4.setCourseTime("7 8", "", "5 6", "1 2", "7 8", "", "");
//        customCourseInfo4.setLoaction("Taichung");
//        customCourseInfo4.setId("05");
//        customCourseInfo4.setTeacher("OAO");
//        courseInfoList.add(customCourseInfo4);

        // Set timetable
        studentCourse.setCourseList(courseInfoList);
        courseTable.setStudentCourse(studentCourse);
        courseTable.setTableInitializeListener(new CourseTableLayout.TableInitializeListener() {
            @Override
            public void onTableInitialized(CourseTableLayout course_table) {
                Toast.makeText(TimeTableViewerActivity.this, "Finish intialized", Toast.LENGTH_SHORT).show();
            }
        });
        courseTable.setOnCourseClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomCourseInfo item = (CustomCourseInfo) view.getTag();
                showInfoDialog(view.getId(), item.getName(), item);
            }
        });
    }

    private void showInfoDialog(int id, String courseName, CustomCourseInfo course) {
        String message = String.format(Locale.TAIWAN, "%s%s\n%s%s\n%s%s",
                "Course ID：", course.getId(),
                "Location：", course.getLoaction(),
                "Teacher：", course.getTeacher());
        AlertDialog.Builder courseDialogBuilder = new AlertDialog.Builder(this)
                .setTitle(courseName)
                .setMessage(message)
                .setPositiveButton("Okay", null);
        courseDialogBuilder.show();
    }
}
