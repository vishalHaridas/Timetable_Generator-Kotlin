package com.threeonefour.timetablegenerator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    //UI elements
    private var tvWelcomeText: TextView? = null
    private var bGenTimetable: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        initialise()
    }

    private fun initialise() {
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference!!.child("Students")
        mAuth = FirebaseAuth.getInstance()
        tvWelcomeText = findViewById<View>(R.id.welcomeTextView) as TextView
        bGenTimetable = findViewById<Button>(R.id.genTimetableButton) as Button;

        bGenTimetable!!.isEnabled = false

        logoutButton!!
            .setOnClickListener {
                mAuth!!.signOut()
                val signUpIntent = Intent(this, LoginActivity::class.java)
                startActivity(signUpIntent)
            }

        subButton!!
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
    }

    override fun onStart() {
        super.onStart()
        val mUser = mAuth!!.currentUser
        val mUserReference = mDatabaseReference!!.child(mUser!!.uid)
//        tvEmail!!.text = mUser.email
//        tvEmailVerifiied!!.text = mUser.isEmailVerified.toString()

        mUserReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val fullName = snapshot.child("name").value as String
                val firstName = fullName.split(" ")[0]

                tvWelcomeText!!.text = "Hi $firstName"
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

}
