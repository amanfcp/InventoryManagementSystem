package com.amaan.inventorymanagementsystem

import android.content.Intent
//import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.activity_signup.businessName
import kotlinx.android.synthetic.main.activity_signup.businessType

class signup : AppCompatActivity() {

    private var mAuth: FirebaseAuth?=null
    lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("accounts")

    }


    fun signupButtonEvent(view: View){
//        val name = signUpName.text.toString()
//        val number= signUpNumber.text.toString()
        val email = signUpEmail.text.toString()
        val password= signUpPassword.text.toString()
//        val businessName1 = businessName.text.toString()
//        val businessType1= businessType.text.toString()

        signUpInFirebase(email,password)
    }

    fun signUpInFirebase(email:String, password:String){
        mAuth!!.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){ task ->
                if (task.isSuccessful){
                    val name = signUpName.text.toString()
                    val number= signUpNumber.text.toString()
                    val email = signUpEmail.text.toString()
                    val password= signUpPassword.text.toString()
                    val businessName1 = businessName.text.toString()
                    val businessType1= businessType.text.toString()

                    Toast.makeText(this, "Successful Sign up", Toast.LENGTH_LONG).show()
                    val currentUser = mAuth!!.currentUser
                    Log.d("SignUp", currentUser!!.uid)
                    saveInfoInDatabase(name, number, email, password, businessName1, businessType1)
                    val intent = Intent(applicationContext, NavDrawer::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(this, "Sign up Failed", Toast.LENGTH_LONG).show()
                }
            }
    }

    fun saveInfoInDatabase(name:String, number:String, email:String, password:String, businessName:String, businessType:String){
        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(number) && !TextUtils.isEmpty(email)&& !TextUtils.isEmpty(password)&& !TextUtils.isEmpty(businessName)&& !TextUtils.isEmpty(businessType)){
            var id:String?=null
            id = database.push().getKey()!!
            var account = Accounts(id,name, number, email, password,businessName,businessType)
            database.child(id).setValue(account)
            Toast.makeText(this,"Account Made", Toast.LENGTH_LONG).show()
        }
    }
}
