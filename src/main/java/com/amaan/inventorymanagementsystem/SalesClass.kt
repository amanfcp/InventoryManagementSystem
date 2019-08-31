package com.amaan.inventorymanagementsystem

public class SalesClass{

    var id: String ? = null
    var units: String ? = null
    var unitsPrice : String ? = null
    var totalCost : String ? = null
    var cash : String ? = null
    var accountReceivable: String ? = null
    var email: String ? = null
    var date: String ? = null


     constructor(){}

    constructor(id: String ,email:String?, units: String, unitsPrice: String,totalCost: String,cash: String,accountReceivable: String,date: String){
        this.id=id
        this.email=email
        this.units=units
        this.unitsPrice= unitsPrice
        this.totalCost= totalCost
        this.cash= cash
        this.accountReceivable= accountReceivable
        this.date = date
    }



}