package com.amaan.inventorymanagementsystem

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_inventory.*

class Inventory : AppCompatActivity() {

    private var userEmail: String? = null
    private var mAuth: FirebaseAuth? = null
    lateinit var database: DatabaseReference
    private lateinit var adapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)
        supportActionBar!!.title = "Inventory"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        database = FirebaseDatabase.getInstance().reference
        mAuth = FirebaseAuth.getInstance()
        val curUser: FirebaseUser? = mAuth!!.currentUser
        userEmail = curUser!!.email

        setupRecyclerView()
    }

    private fun setupRecyclerView(){
        val q: Query = database.child("inventory")
        val op : FirebaseRecyclerOptions<InventoryClass> = FirebaseRecyclerOptions.Builder<InventoryClass>().setQuery(q,InventoryClass::class.java).build()
        adapter = MyAdapter(op)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.adapter = adapter


    }

    public override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    public override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}