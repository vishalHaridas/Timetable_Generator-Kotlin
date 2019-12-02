package com.threeonefour.timetablegenerator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_signup.*


class SignUp : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    val email: String = emailText?.text.toString();
    val password: String = passConfirmText?.text.toString();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        courseSpinner.adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.Courses))

        auth = FirebaseAuth.getInstance()

        createSubmitButton.setOnClickListener{
//            val signUpIntent = Intent(this, SubjectSelection::class.java)
//            startActivity(signUpIntent)
//            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    public override fun onStart() {
        super.onStart()
        if (auth.currentUser != null){
            //Do something
        }
    }








//    majorSpinner.adapter = ArrayAdapter(
//    this, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.Courses));



}
