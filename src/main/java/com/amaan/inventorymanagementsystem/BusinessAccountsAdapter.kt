package com.amaan.inventorymanagementsystem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


public class BusinessAccountsAdapter(options: FirebaseRecyclerOptions<BusinessAccountsClass>) :
    FirebaseRecyclerAdapter<BusinessAccountsClass, BusinessAccountsAdapter.MyViewHolder>(options) {
    var credit : String?=null
    var debit: String?=null
    var finalCredit :Int ?= 0
    var finalDebit : Int ?= 0
    var total : Int ?= 0
    private var mAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private val curUser = mAuth.currentUser
    private val userEmail = curUser!!.email

    override fun onBindViewHolder(holder: MyViewHolder, pos : Int, model : BusinessAccountsClass) {
        if (model.email == this.userEmail) {
            holder.accountTitle.text = model.accountTitle!!.toString()
            holder.debit.text = model.debit!!.toString()
            holder.credit.text = model.credit!!.toString()

            //total kar ke result show karna hai
            credit = model.credit!!.toString()
            finalCredit = Integer.parseInt(credit!!)

            debit = model.debit!!.toString()
            finalDebit = Integer.parseInt(debit!!)

            if (finalCredit!! > finalDebit!!) {
                total = finalCredit!! - finalDebit!!
            } else if (finalDebit!! > finalCredit!!) {
                total = finalDebit!! - finalCredit!!
            }
            holder.total.text = total.toString()
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.business_accounts_cardview,p0,false)
        return MyViewHolder(view)

    }
    public class MyViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        var accountTitle = itemView.findViewById<TextView>(R.id.businessAccountTitle)!!
        var debit= itemView.findViewById<TextView>(R.id.businessAccountDebit)!!
        var credit = itemView.findViewById<TextView>(R.id.businessAccountCredit)!!
        var total = itemView.findViewById<TextView>(R.id.businessAccountTotal)!!
    }
}