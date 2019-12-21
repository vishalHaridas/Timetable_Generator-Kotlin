package com.threeonefour.timetablegenerator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.activity_subject_range.*

class SubjectRangeActivity : AppCompatActivity() {

    private var threeSubs: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject_range)

        threeSubs = findViewById(R.id.threeSubsButton)

        val toolbar: Toolbar = findViewById(R.id.subRangeToolbar);
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        threeSubs!!.setOnClickListener {


            TimetableGenerator.genSubCombos(3);

            val subSelection = Intent(this, TimeTableOptionsActivity::class.java)
            startActivity(subSelection);
        }

    }

    override fun onStart() {
        super.onStart()

        TimetableGenerator.completedSubCodes = DashboardActivity.COMPLETED_SUBJECTS;
        Log.d("TGRN_COMP_SUB_SIZE", "${TimetableGenerator.completedSubCodes.size}")
        Log.d("SUBS_BEFORE_FUNC", "${TimetableGenerator.semSubs.size}")
        TimetableGenerator.markCompletedSubs()
        Log.d("SUBS_AFTER_FUNC", "${TimetableGenerator.semSubs.size}")


    }
}
