package com.threeonefour.timetablegenerator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.annotation.IntegerRes
import com.google.firebase.database.*
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.fragment.app.FragmentActivity


//TODO: Course List with subject object
//TODO: Completed subjects as just codes


class SubjectSelectionActivity : AppCompatActivity() {
    companion object {
        var COURSE_SUBJECT_LIST: ArrayList<Subject> = ArrayList<Subject>()
    }



//    private var subList : ArrayList<Model>? = null
    private var subArrayList: ArrayList<Model>? = null
    private var lv: ListView? = null
//    private var modelArrayList: ArrayList<Model>? = null
    private var subSelAdapter: SubjectSelectionAdapter? = null
    private val subList = arrayOf("Lion", "Tiger", "Leopard", "Cat", "Tiger", "Leopard", "Cat", "Tiger", "Leopard", "Cat", "Tiger", "Leopard", "Cat", "Tiger", "Leopard", "Cat", "Tiger", "Leopard", "Cat")

    private lateinit var _db: DatabaseReference
    private lateinit var dbCourseList: MutableList<Course>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject_selection)

        initialise()
    }

    private fun initialise(){

        _db = FirebaseDatabase.getInstance().getReference("Courses/COBSC01")
        dbCourseList = mutableListOf()

        lv = findViewById(R.id.subListView) as ListView

        subArrayList = getModel(false)
        subSelAdapter = SubjectSelectionAdapter(this, subArrayList!!)
        lv!!.adapter = subSelAdapter

        _db.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){

                    val coreSubsList: DataSnapshot = p0.child("core");
                    val gedSubsList: DataSnapshot = p0.child("gedSubs");
                    val majorSubsList: DataSnapshot = p0.child("major/MAMGD01/elective")

                    for (sub in coreSubsList.children){
                        val subCode: String? = sub.key;
                        val subName: String? = sub.child("name").value as String?
                        val subPrereqList: DataSnapshot = sub.child("prereq")

                        var prereqList: ArrayList<String>? = ArrayList<String>()

                        for (prereq in subPrereqList.children){
                            val prereqID: String? = prereq.value as String?
                            if (prereqID != null) {
                                prereqList?.add(prereqID)
                            }
//                            Log.d("DATABASE COURSE PRE SUB", "subcode: $subCode, prereq: $prereqID")
                        }

                        var subObject: Subject = Subject("$subCode","$subName", false)
                        subObject.prereqs = prereqList

                        COURSE_SUBJECT_LIST.add(subObject);

//                        Log.d("DATABASE COURSE SUB", "subcode: ${subObject.code}, prereq: ${subObject.prereqs}")
                    }
                    for (sub in gedSubsList.children){
                        val subDetails: DataSnapshot = sub;
//                        Log.d("DATABASE GED SUB", "$subDetails")
                    }
                    for (sub in majorSubsList.children){
                        val subDetails: DataSnapshot = sub;
//                        Log.d("DATABASE MAJOR SUB", "$subDetails")
                    }
                }
            }

        })
    }

    private fun getModel(isSelect: Boolean): ArrayList<Model> {
        val list = ArrayList<Model>()
        for (i in subList.indices) {

            val model = Model()
            model.setSelects(isSelect)
            model.setsubName(subList[i])
            list.add(model)
        }
        return list
    }




}
