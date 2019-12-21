package com.threeonefour.timetablegenerator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar


class TimeTableOptionsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_table_options)

        val toolbar: Toolbar = findViewById(R.id.timeTableOptionsToolbar);
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        val tTableGrid: GridView = findViewById(R.id.tTableGenGridView)
        tTableGrid.adapter = tTableGridAdapter(this)


        tTableGrid.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id
                ->
//                Toast.makeText(this@TimeTableOptionsActivity,
//                "$position",Toast.LENGTH_SHORT).show()
                TimetableGenerator.generateTimetables(TimetableGenerator.combinations[position])
                val timeTableViewerIntent = Intent(this, TimeTableViewerActivity::class.java)
                startActivity(timeTableViewerIntent)
            }

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

            val tTable: ArrayList<Subject> = getItem(position) as ArrayList<Subject>

            subTitle.text = "CSIT212\nCSIT214\nCSIT314\nCSIT111"

            return rowMain
        }

        override fun getItem(position: Int): Any {
            return TimetableGenerator.combinations[position]
        }

        override fun getItemId(position: Int): Long {
            return 0

        }

        override fun getCount(): Int {
            return TimetableGenerator.combinations.size
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

            val tTable: ArrayList<Subject> = getItem(position) as ArrayList<Subject>

            var text: String = ""
            for (i in 0 until tTable.size){
                text += if (i !=tTable.size -1)
                    "${tTable[i].code}\n"
                else
                    "${tTable[i].code}"
            }
            subTitle.text = "$text"

            return rowMain
        }

        override fun getItem(position: Int): Any {
            return TimetableGenerator.combinations[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getCount(): Int {
            return TimetableGenerator.combinations.size
        }

    }

}
