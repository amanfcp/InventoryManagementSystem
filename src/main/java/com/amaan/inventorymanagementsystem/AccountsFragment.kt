package com.amaan.inventorymanagementsystem

import android.os.Bundle
import android.util.Log
//import androidx.core.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_accounts.*
import kotlinx.android.synthetic.main.nav_header_nav_drawer.*

class AccountsFragment : Fragment(){
    var databaseBusinessAccountsReference = FirebaseDatabase.getInstance().getReference("businessAccounts")
    lateinit var businessAccountsAdapter : BusinessAccountsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var rootView= inflater.inflate(R.layout.fragment_accounts,container,false)
        var rv = rootView.findViewById<RecyclerView>(R.id.businessAccountsRV)
//        databaseBusinessAccountsReference.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onCancelled(p0: DatabaseError) {
//            }

//            override fun onDataChange(p0: DataSnapshot) {
//                if(p0.exists()){
                    Log.i("RecycleView","We're in recycler Function")
//                    var q: Query = databaseBusinessAccountsReference
//                    var op : FirebaseRecyclerOptions<BusinessAccountsClass> = FirebaseRecyclerOptions.Builder<BusinessAccountsClass>().setQuery(q,BusinessAccountsClass::class.java).build()
//                    var businessAccountsAdapter= BusinessAccountsAdapter(op)

                    rv.layoutManager = GridLayoutManager(context,2)
//                    rv.setHasFixedSize(true)

//                }
//            }
//        })
        return rootView
//        loadbusinessAccounts()
    }
//    fun loadbusinessAccounts(){
//        databaseBusinessAccountsReference.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onCancelled(p0: DatabaseError) {
//
//
//            override fun onDataChange(p0: DataSnapshot) {
//                if(p0.exists()){
//                    Log.i("RecycleView","We're in recycler Function")
////                    var q: Query = databaseBusinessAccountsReference
////                    var op : FirebaseRecyclerOptions<BusinessAccountsClass> = FirebaseRecyclerOptions.Builder<BusinessAccountsClass>().setQuery(q,BusinessAccountsClass::class.java).build()
////                    businessAccountsAdapter= BusinessAccountsAdapter(op)
//
//                    businessAccountsRV.setHasFixedSize(true)
//                    businessAccountsRV.layoutManager = LinearLayoutManager(context)
//                    businessAccountsRV.adapter = businessAccountsAdapter
//                }
//            }
//        })
//    }


//    override fun onResume() {
//        super.onResume()
//        businessAccountsAdapter.startListening()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        businessAccountsAdapter.stopListening()
//    }

//    override fun onCreate(savedInstanceState: Bundle?) {
///        super.onCreate(savedInstanceState)
//    }
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        databaseBusinessAccountsReference.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onCancelled(p0: DatabaseError) {
//            }
//
//            override fun onDataChange(p0: DataSnapshot) {
//                if(p0.exists()){
//                    Log.i("RecycleView","We're in recycler Function")
//                    var q: Query = databaseBusinessAccountsReference
//                    var op : FirebaseRecyclerOptions<BusinessAccountsClass> = FirebaseRecyclerOptions.Builder<BusinessAccountsClass>().setQuery(q,BusinessAccountsClass::class.java).build()
//                    businessAccountsAdapter= BusinessAccountsAdapter(op)
//                    Log.i("RecycleView","We're in recycler Function")
//                    businessAccountsRV.setHasFixedSize(true)
//                    businessAccountsRV.layoutManager = LinearLayoutManager(context)
//                    businessAccountsRV.adapter = businessAccountsAdapter
//                }
//            }
//        })
//
//    }

//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        businessAccountsAdapter.startListening()
//    }


    override fun onStart() {
        super.onStart()
        var q: Query = databaseBusinessAccountsReference
        var op : FirebaseRecyclerOptions<BusinessAccountsClass> = FirebaseRecyclerOptions.Builder<BusinessAccountsClass>().setQuery(q,BusinessAccountsClass::class.java).build()
        businessAccountsAdapter = BusinessAccountsAdapter(op)
        businessAccountsAdapter.notifyDataSetChanged()
        businessAccountsRV.adapter = businessAccountsAdapter
        businessAccountsAdapter.startListening()
    }
}

