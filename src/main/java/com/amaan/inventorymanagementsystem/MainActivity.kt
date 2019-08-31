package com.amaan.inventorymanagementsystem

import android.content.Intent
//import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()

        // making signup text clickable

        var spannableText = SpannableString(signUpLink.text.toString())
        var clickableSpan = object : ClickableSpan(){
            override fun onClick(p0: View) {
                val intent = Intent(applicationContext, signup::class.java)
                startActivity(intent)
            }
        }
        spannableText.setSpan(clickableSpan, 23,30,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        signUpLink.setText(spannableText)
        signUpLink.setMovementMethod(LinkMovementMethod.getInstance())



    }

    fun loginButtonEvent(view:View){
        val email = loginEmail.text.toString()
        val password= loginPassword.text.toString()
        loginToFirebase(email,password)
    }

    private fun loginToFirebase(email:String,Pword:String)
    {
        mAuth!!.signInWithEmailAndPassword(email,Pword)
            .addOnCompleteListener(this){ task ->
                if (task.isSuccessful){
                    Toast.makeText(applicationContext, "Successful Login", Toast.LENGTH_LONG).show()
                    val currentUser = mAuth!!.currentUser
                    Log.d("Login", currentUser!!.uid)
                    val intent = Intent(applicationContext, NavDrawer::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(applicationContext, "Login Failed", Toast.LENGTH_LONG).show()
                }
            }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth!!.getCurrentUser()
        if(currentUser != null){
            val intent = Intent(this, NavDrawer::class.java)
            startActivity(intent)
        }
    }

}
