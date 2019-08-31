package com.amaan.inventorymanagementsystem

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

data class InventoryData (var units: String, var unitsPrice: String)




object Supplier{

    val units= listOf<InventoryData>(
        InventoryData("10","20"),
        InventoryData("20","30"),
        InventoryData("30","10"),
        InventoryData("40","40"),
        InventoryData("50","20")
    )
}