package com.amaan.inventorymanagementsystem

//import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class Expense : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense)
        supportActionBar!!.title = "Expense"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}
