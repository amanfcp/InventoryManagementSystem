package com.amaan.inventorymanagementsystem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth

class MyAdapter(options: FirebaseRecyclerOptions<InventoryClass>) :
    FirebaseRecyclerAdapter<InventoryClass, MyAdapter.MyViewHolder>(options) {
    private var mAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private val curUser = mAuth.currentUser
    private val userEmail = curUser!!.email
    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: InventoryClass) {

        if(model.email == userEmail){
            holder.units.text = model.units!!.toString()
            holder.unitsPrice.text = model.unitsPrice!!.toString()
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.inventory_items,p0,false)
        return MyViewHolder(view)

    }
    class MyViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        var units = itemView.findViewById<TextView>(R.id.unit)!!
        var unitsPrice = itemView.findViewById<TextView>(R.id.unitPrice)!!
    }
}