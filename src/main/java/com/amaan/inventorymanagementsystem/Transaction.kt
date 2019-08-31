package com.amaan.inventorymanagementsystem

import android.content.Intent
//import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class Transaction : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)
        supportActionBar!!.title = "Transaction"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
    fun saleButtonEvent(view:View){
        val intent = Intent(this, Sale::class.java)
        startActivity(intent)
    }
    fun purchaseButtonEvent(view:View){
        val intent = Intent(this, Purchase::class.java)
        startActivity(intent)
    }
    fun expenseButtonEvent(view:View){
        val intent = Intent(this, Expense::class.java)
        startActivity(intent)
    }
    fun paymentButtonEvent(view:View){
        val intent = Intent(this, Payment::class.java)
        startActivity(intent)
    }
}
