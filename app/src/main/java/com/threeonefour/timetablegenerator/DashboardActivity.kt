package com.threeonefour.timetablegenerator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.activity_signup.logoutButton

class DashboardActivity : AppCompatActivity() {

    //Firebase references
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null

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


        _db = FirebaseDatabase.getInstance().getReference("Courses/COBSC01")
//        _StudentsDB = FirebaseDatabase.getInstance().getReference("Students")

        bGenTimetable!!.isEnabled = false

        logoutButton!!
            .setOnClickListener {
                mAuth!!.signOut()
                val signUpIntent = Intent(this, LoginActivity::class.java)
                startActivity(signUpIntent)
            }


//        currentUserDb.addValueEventListener( object: ValueEventListener {
//            override fun onCancelled(p0: DatabaseError) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//
//            override fun onDataChange(p0: DataSnapshot) {
//                if (p0.child("completedSubs").exists()){
////                    Toast.makeText(this@DashboardActivity, "Does exist.", Toast.LENGTH_SHORT).show()
//                    Log.d("COMP SUB EXISTS", "Does exist")
//                }
//                else{
////                    Toast.makeText(this@DashboardActivity, "Does not exist.", Toast.LENGTH_SHORT).show()
//                    Log.d("COMP SUB EXISTS", "Does NOT exist")
//                }
//            }
//
//        })

        chooseSubButton!!
            .setOnClickListener{
                val subSelection = Intent(this, SubjectSelectionActivity::class.java)
                startActivity(subSelection);
            }

        bGenTimetable!!
            .setOnClickListener{
                if (!bGenTimetable!!.isEnabled){
                    Toast.makeText(this@DashboardActivity, "Select Subjects first.",Toast.LENGTH_SHORT).show()
                }
            }


        chooseSubButton!!.isEnabled = false
    }

    override fun onStart() {
        super.onStart()
        val mUser = mAuth!!.currentUser
        val mUserID = mUser!!.uid
        val mUserReference = mDatabaseReference!!.child(mUserID)
//        val _compSubs =
//        tvEmail!!.text = mUser.email
//        tvEmailVerifiied!!.text = mUser.isEmailVerified.toString()

//        _StudentsDB.addValueEventListener(object: ValueEventListener{
//            override fun onCancelled(p0: DatabaseError) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//
//            override fun onDataChange(p0: DataSnapshot) {
//                val count = p0.childrenCount
//                Log.d("STUDENT CHILD COUNT", "$count")
//            }
//
//        })

        _db.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){

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

//                val compSubs: DataSnapshot = snapshot.child(mUserID)
                val count = snapshot.childrenCount
                Log.d("STUDENT CHILD COUNT", "$count")


                val fullName = snapshot.child("name").value as String
                val firstName = fullName.split(" ")[0]

                tvWelcomeText!!.text = "Hi $firstName"

                tvSubSelSubheading!!.visibility = View.VISIBLE
                chooseSubButton!!.visibility = View.VISIBLE
                chooseSubButton!!.text = "Edit Subjects"
                bGenTimetable!!.isEnabled = true

                tvSubSelSubheading!!.setText("Setup Subjects")
                chooseSubButton!!.setText("Edit Subjects")
                bGenTimetable!!.isEnabled = true


                if (count > 1){
                    tvSubSelSubheading!!.visibility = View.GONE;
                    chooseSubButton!!.setText("Edit Subjects")
                    tvYourTimetable!!.visibility = View.VISIBLE
                    bGenTimetable!!.visibility = View.VISIBLE
                    bGenTimetable!!.isEnabled = true


                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

}
