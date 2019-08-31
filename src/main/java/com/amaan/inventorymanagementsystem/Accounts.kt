package com.amaan.inventorymanagementsystem

public class Accounts{
    var id: String ? = null
    var name : String ? = null
    var number : String ? = null
    var email : String ? = null
    var password : String ? = null
    var businessName : String ? = null
    var businessType : String ? = null

    fun Accounts(){

    }
         constructor(id: String, name: String,number: String,email: String,password: String,businessName: String,businessType: String){
        this.id=id
        this.name= name
        this.number= number
        this.email= email
        this.password= password
        this.businessName= businessName
        this.businessType= businessType

    }



}