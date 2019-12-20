package com.threeonefour.timetablegenerator

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import java.time.DayOfWeek
import java.time.LocalTime

class TimeTableOptionsActivity : AppCompatActivity() {

    companion object{
        var TIMETABLE_LIST: ArrayList<Timetable> = ArrayList<Timetable>()
    }

    var classOneList: ArrayList<CClass> = ArrayList<CClass>()
    var classesList: ArrayList<ArrayList<CClass>> = ArrayList<ArrayList<CClass>>()

    val cOne: CClass =
        CClass("CSIT212", 'L', '-', "KV14-125",
            "prof1", DayOfWeek.SUNDAY, LocalTime.parse("10:30"),
            LocalTime.parse("12:30"))

    val cTwo: CClass =
        CClass("CSIT212", 'T', '-', "KV14-125",
            "prof2", DayOfWeek.MONDAY, LocalTime.parse("08:30"),
            LocalTime.parse("10:30"))

    val cThree: CClass =
        CClass("CSIT212", 'C', '-', "KV14-125",
            "prof2", DayOfWeek.MONDAY, LocalTime.parse("13:30"),
            LocalTime.parse("17:30"))





//    val ttTest: Timetable = Timetable()

    override fun onCreate(savedInstanceState: Bundle?) {

        TIMETABLE_LIST.clear()
        classOneList.clear()
        classesList.clear()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_table_options)

        val toolbar: Toolbar = findViewById(R.id.timeTableOptionsToolbar);
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        val tTableGrid: GridView = findViewById(R.id.tTableGenGridView)
        tTableGrid.adapter = tTableGridAdapter(this)

//        val tTableSubList = findViewById<ListView>(R.id.tTablesubListView)
//        tTableSubList.adapter = tTableAdapter(this)

        classOneList.add(cOne);
        classOneList.add(cTwo)
        classOneList.add(cThree)

        classesList.add(classOneList);


        val subsList: ArrayList<String> = ArrayList<String>()
        subsList.add("CSIT212")

        val timeTableOne: Timetable = Timetable(subsList, classesList);

        TIMETABLE_LIST.add(timeTableOne);
        TIMETABLE_LIST.add(timeTableOne);
        TIMETABLE_LIST.add(timeTableOne);
        TIMETABLE_LIST.add(timeTableOne);
        TIMETABLE_LIST.add(timeTableOne);
        TIMETABLE_LIST.add(timeTableOne);
        TIMETABLE_LIST.add(timeTableOne);
        TIMETABLE_LIST.add(timeTableOne);
        TIMETABLE_LIST.add(timeTableOne);
        TIMETABLE_LIST.add(timeTableOne);
        TIMETABLE_LIST.add(timeTableOne);
        TIMETABLE_LIST.add(timeTableOne);
        TIMETABLE_LIST.add(timeTableOne);
        TIMETABLE_LIST.add(timeTableOne);
        TIMETABLE_LIST.add(timeTableOne);

    }


    private class tTableAdapter(context: Context): BaseAdapter(){

        private val mContext: Context

        init{
            mContext = context
        }


        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val LayoutInflater = LayoutInflater.from(mContext)
            val rowMain = LayoutInflater.inflate(R.layout.time_table_sub_option, parent, false)

            val subTitle = rowMain.findViewById(R.id.tTableSubTextView) as TextView

            val tTable: Timetable = getItem(position) as Timetable

            subTitle.text = "CSIT212\nCSIT214\nCSIT314\nCSIT111"

            return rowMain
        }

        override fun getItem(position: Int): Any {
            return TIMETABLE_LIST[position]
        }

        override fun getItemId(position: Int): Long {
            return 0

        }

        override fun getCount(): Int {
            return TIMETABLE_LIST.size
        }

    }

    private class tTableGridAdapter(context: Context) : BaseAdapter(){

        private val mContext: Context

        init{
            mContext = context
        }


        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val LayoutInflater = LayoutInflater.from(mContext)
            val rowMain = LayoutInflater.inflate(R.layout.time_table_sub_option, parent, false)

            val subTitle = rowMain.findViewById(R.id.tTableSubTextView) as TextView

            val tTable: Timetable = getItem(position) as Timetable

            subTitle.text = "CSIT212\nCSIT214\nCSIT314\nCSIT111"

            return rowMain
        }

        override fun getItem(position: Int): Any {
            return TIMETABLE_LIST[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getCount(): Int {
            return TIMETABLE_LIST.size
        }

    }

}
