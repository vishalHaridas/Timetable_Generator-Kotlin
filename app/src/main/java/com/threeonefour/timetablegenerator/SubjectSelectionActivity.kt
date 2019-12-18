package com.threeonefour.timetablegenerator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.annotation.IntegerRes
import com.google.firebase.database.*
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.google.firebase.auth.FirebaseAuth
import com.threeonefour.timetablegenerator.SubjectSelectionActivity.Companion.COURSE_SUBJECT_LIST
import kotlinx.android.synthetic.main.activity_dashboard.*


//TODO: Course List with subject object
//TODO: Completed subjects as just codes


class SubjectSelectionActivity : AppCompatActivity() {
    companion object {
        var COURSE_SUBJECT_LIST: ArrayList<Subject> = ArrayList<Subject>()
    }

    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null


//    private var subList : ArrayList<Model>? = null
    private var subArrayList: ArrayList<Model>? = null
    private var lv: ListView? = null
    private var doneButton: Button? = null
    private var subSelAdapter: SubjectSelectionAdapter? = null


//    private lateinit var _db: DatabaseReference
//    private lateinit var dbCourseList: MutableList<Course>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject_selection)

        initialise()
    }

    private fun initialise(){

//        _db = FirebaseDatabase.getInstance().getReference("Student/COBSC01")
//        dbCourseList = mutableListOf()

        lv = findViewById<ListView>(R.id.subListView)
        doneButton = findViewById<Button>((R.id.subSelDoneButton))

        subArrayList = getModel(false)
        subSelAdapter = SubjectSelectionAdapter(this, subArrayList!!)
        lv!!.adapter = subSelAdapter


        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference!!.child("Students")
        mAuth = FirebaseAuth.getInstance()
        val userId = mAuth!!.currentUser!!.uid
        val currentUserDb = mDatabaseReference!!.child(userId)
        Log.d("CURRENT USER", "$userId");

        doneButton!!.setOnClickListener {
//                    }
            var counter = 0
            currentUserDb.child("completedSubs").removeValue()
                for (i in 0 until SubjectSelectionAdapter.public_subjectArrayList!!.size){
                    if (SubjectSelectionAdapter.public_subjectArrayList!!.get(i).isSelected) {
                        val testString = SubjectSelectionAdapter.public_subjectArrayList!!.get(i).subName
                        currentUserDb.child("completedSubs").child("$counter").setValue(testString)
                        counter++;
                    }
                }
            }




//        subArrayList = getModel(false)
//        subSelAdapter = SubjectSelectionAdapter(this, subArrayList!!)
//        lv!!.adapter = subSelAdapter
    }

    private fun getModel(isSelect: Boolean): ArrayList<Model> {
        val list = ArrayList<Model>()
        for (i in COURSE_SUBJECT_LIST.indices) {

            val model = Model()
            model.setSelects(isSelect)
            model.setsubName(COURSE_SUBJECT_LIST[i].code)
            list.add(model)
        }
        return list
    }




}
