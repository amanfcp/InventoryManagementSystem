package com.amaan.inventorymanagementsystem

//import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class Payment : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        supportActionBar!!.title = "Payment"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}
