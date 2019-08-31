package com.amaan.inventorymanagementsystem

public class InventoryClass{

    var unitsPrice : String ? = null
    var units: Int ? = 0
    var email: String ? = null

    constructor(){}

    constructor(units: Int,unitsPrice: String, email:String?){
        this.email=email
        this.units=units
        this.unitsPrice= unitsPrice
    }



}