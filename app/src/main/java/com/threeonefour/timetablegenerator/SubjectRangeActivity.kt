package com.threeonefour.timetablegenerator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
            val subSelection = Intent(this, TimeTableOptionsActivity::class.java)
            startActivity(subSelection);
        }

    }
}
