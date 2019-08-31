package com.amaan.inventorymanagementsystem

class BusinessAccountsClass{

    var accountTitle : String ? = null
    var accountType: String ? = null
    var debit: String? = null
    var credit: String? = null
    var email: String ? = null


    constructor(){}

    constructor(
        accountTitle: String?,
        accountType: String?,
        debit: String?,
        credit: String?,
        email: String?
    ) {
        this.accountTitle = accountTitle
        this.accountType = accountType
        this.debit = debit
        this.credit = credit
        this.email = email
    }


}