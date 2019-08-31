package com.amaan.inventorymanagementsystem

public class PurchasesClass{

    var id: String ? = null
    var units: String ? = null
    var unitsPrice : String ? = null
    var totalCost : String ? = null
    var cash : String ? = null
    var accountPayable: String ? = null
    var email: String ? = null
    var date: String ? = null

    fun PurchasesClass(){}

    constructor(id: String ,email:String?, units: String, unitsPrice: String,totalCost: String,cash: String,accountPayable: String, date: String){
        this.id=id
        this.email=email
        this.units=units
        this.unitsPrice= unitsPrice
        this.totalCost= totalCost
        this.cash= cash
        this.accountPayable= accountPayable
        this.date=date
    }
}