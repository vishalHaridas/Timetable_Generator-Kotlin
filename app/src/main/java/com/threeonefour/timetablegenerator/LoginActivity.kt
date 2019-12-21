package com.threeonefour.timetablegenerator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    private val TAG = "LoginActivity"

    //global variables
    private var email: String? = null
    private var password: String? = null

    //UI elements
    private var etEmail: EditText? = null
    private var etPassword: EditText? = null
    private var btnLogin: Button? = null
    private var btnCreateAccount: TextView? = null
    //private var mProgressBar: ProgressDialog? = null

    //Firebase references
    private var user: FirebaseUser? = null
    private var mAuth: FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

//        createUserText.setOnClickListener{
//            val signUpIntent = Intent(this, SignUpActivity::class.java);
//            startActivity(signUpIntent);
//        }

        user = FirebaseAuth.getInstance().currentUser

        if (user != null)
            updateUI()




        initialise()
    }

    private fun initialise() {
        etEmail = findViewById<View>(R.id.enterEmailText) as EditText
        etPassword = findViewById<View>(R.id.enterPasswordText) as EditText
        btnLogin = findViewById<View>(R.id.loginButton) as Button
        btnCreateAccount = findViewById<View>(R.id.createUserText) as TextView

        //mProgressBar = ProgressDialog(this)
        mAuth = FirebaseAuth.getInstance()
        btnCreateAccount!!
            .setOnClickListener {
                val signUpIntent = Intent(this, SignUpActivity::class.java);
                startActivity(signUpIntent)
            }
        btnLogin!!.setOnClickListener { loginUser() }

    }

    private fun loginUser() {
        email = etEmail?.text.toString()
        password = etPassword?.text.toString()
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            //mProgressBar!!.setMessage("Registering User...")
            //mProgressBar!!.show()
            Log.d(TAG, "Logging in user.")
            mAuth!!.signInWithEmailAndPassword(email!!, password!!)
                .addOnCompleteListener(this) { task ->
                    //mProgressBar!!.hide()
                    if (task.isSuccessful) {
                        // Sign in success, update UI with signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        updateUI()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.e(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(this@LoginActivity, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Enter all details", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI() {
        val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
        finish();
    }
}


