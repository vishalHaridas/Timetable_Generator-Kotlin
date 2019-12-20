package com.threeonefour.timetablegenerator

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.threeonefour.timetablegenerator.SubjectSelectionActivity.Companion.COMPLETED_SUBJECTS
import com.threeonefour.timetablegenerator.SubjectSelectionActivity.Companion.COURSE_SUBJECT_LIST
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.toolbar_dashboard.*
import kotlin.system.exitProcess

//import kotlinx.android.synthetic.main.activity_signup.logoutButton

//import com.threeonefour.timetablegenerator.TimetableGenerator.updateTimetable as updateTimetable;

class DashboardActivity : AppCompatActivity() {

    //Firebase references
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null

    /*-----------------------------------JAVA VARIABLES------------------------------------*/
    val timeTableGenObj : TimetableGenerator = TimetableGenerator()
    /*--------------------------------END OF JAVA VARIABLES---------------------------------*/


    private lateinit var _db: DatabaseReference
//    private lateinit var _StudentsDB: DatabaseReference

    //UI elements
    private var tvWelcomeText: TextView? = null
    private var bGenTimetable: Button? = null
    private var tvSubSelSubheading: TextView? = null
    private var tvYourTimetable: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)


        chooseSubButton!!.isEnabled =false

        initialise()

    }

    private fun initialise() {
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference!!.child("Students")
        mAuth = FirebaseAuth.getInstance()
        val userId = mAuth!!.currentUser!!.uid
        val currentUserDb = mDatabaseReference!!.child(userId)
        tvWelcomeText = findViewById<View>(R.id.welcomeTextView) as TextView
        bGenTimetable = findViewById(R.id.genTimetableButton) as Button;
        tvSubSelSubheading = findViewById(R.id.curSubText) as TextView;
        tvYourTimetable = findViewById(R.id.yourTtTextView) as TextView;

        val dashBoardToolbar: Toolbar = findViewById(R.id.dasboardToolbar);
        setSupportActionBar(dashBoardToolbar)


        _db = FirebaseDatabase.getInstance().getReference("Courses/COBSC01")
//        _StudentsDB = FirebaseDatabase.getInstance().getReference("Students")

        bGenTimetable!!.isEnabled = false

        logoutButton!!.setOnClickListener {
                mAuth!!.signOut()
                val signUpIntent = Intent(this, LoginActivity::class.java)
                startActivity(signUpIntent)
            }

        chooseSubButton!!.setOnClickListener{
                val subSelection = Intent(this, SubjectSelectionActivity::class.java)
                startActivity(subSelection);
            }

        bGenTimetable!!.setOnClickListener{
//                if (!bGenTimetable!!.isEnabled){
//                    Toast.makeText(this@DashboardActivity, "Select Subjects first.",Toast.LENGTH_SHORT).show()
//                }
                val intent = Intent(this@DashboardActivity, SubjectRangeActivity::class.java)
                startActivity(intent)
//                finish();

            }


        chooseSubButton!!.isEnabled = false
    }

    override fun onStart() {

        super.onStart()
        val mUser = mAuth!!.currentUser
        val mUserID = mUser!!.uid
        val mUserReference = mDatabaseReference!!.child(mUserID)

        Log.d("COMP_SUB_ARRAYLIST", "${COMPLETED_SUBJECTS.size}")

        _db.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){

                    SubjectSelectionActivity.COURSE_SUBJECT_LIST.clear()

                    val coreSubsList: DataSnapshot = p0.child("core");
                    val gedSubsList: DataSnapshot = p0.child("gedSubs");
                    val majorElectiveSubsList: DataSnapshot = p0.child("major/MAMGD01/elective")
                    val majorSpecializationSubsList: DataSnapshot = p0.child("major/MAMGD01/specialization")

                    for (sub in gedSubsList.children){
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

                        SubjectSelectionActivity.COURSE_SUBJECT_LIST.add(subObject);
                    }

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

                        SubjectSelectionActivity.COURSE_SUBJECT_LIST.add(subObject);

//                        Log.d("DATABASE COURSE SUB", "subcode: ${subObject.code}, prereq: ${subObject.prereqs}")
                    }
                    Log.d("GLOBAL COURSE LIST", "${SubjectSelectionActivity.COURSE_SUBJECT_LIST[0].code}")
                    for (sub in majorElectiveSubsList.children){
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

                        SubjectSelectionActivity.COURSE_SUBJECT_LIST.add(subObject);
                    }
                    for (sub in majorSpecializationSubsList.children){
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

                        SubjectSelectionActivity.COURSE_SUBJECT_LIST.add(subObject);
                    }

                }
                chooseSubButton!!.isEnabled = true
            }

        })

        mUserReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                COMPLETED_SUBJECTS.clear()

//                val compSubs: DataSnapshot = snapshot.child(mUserID)
                val count = snapshot.childrenCount

                val fullName = snapshot.child("name").value as String
                val firstName = fullName.split(" ")[0]
                tvWelcomeText!!.text = "Hi, $firstName"

                val compSubs: DataSnapshot = snapshot.child("completedSubs")
                Log.d("COMP SUB SNAPSHOT", "$compSubs")
                if (compSubs != null)
                for (subs in compSubs.children){
                    val subId: String = subs.value as String
                    Log.d("COMP_SUB_ID", "$subId")
                    COMPLETED_SUBJECTS.add(subId)
                }


                tvSubSelSubheading!!.visibility = View.VISIBLE
                chooseSubButton!!.visibility = View.VISIBLE
//                chooseSubButton!!.text = "Edit Subjects"
                tvYourTimetable!!.visibility = View.VISIBLE
                bGenTimetable!!.visibility = View.VISIBLE
                logoutButton!!.visibility =View.VISIBLE

                tvSubSelSubheading!!.setText("Setup completed subjects first")
//                chooseSubButton!!.setText("Edit Subjects")
                bGenTimetable!!.isEnabled = false


                if (count > 1){
                    tvSubSelSubheading!!.visibility = View.GONE;
                    chooseSubButton!!.setText("Edit Subjects")
                    tvYourTimetable!!.visibility = View.VISIBLE
                    bGenTimetable!!.visibility = View.VISIBLE
                    bGenTimetable!!.isEnabled = true


                }
            }
            override fun onCancelled(databaseError: DatabaseError) {

            }
        })


        //region -----------------------------------JAVA BACKEND CALLS------------------------------------

        TimetableGenerator.subjects = COURSE_SUBJECT_LIST;
//        Log.d("SUBJECTLIST_SIZE", "${TimetableGenerator.subjects.size}")
        TimetableGenerator.updateTimetable(this)
        TimetableGenerator.updateCurrentSubjects()
        TimetableGenerator.addClassesToSubject()
        if (TimetableGenerator.subjects.size > 0)
            for (i in 0 until TimetableGenerator.subjects.size)
            Log.d("SUBJECTLIST_OBJECT", "${TimetableGenerator.subjects[i]}")




        /*--------------------------------END OF JAVA CODE---------------------------------*/
        //endregion

    }

}
