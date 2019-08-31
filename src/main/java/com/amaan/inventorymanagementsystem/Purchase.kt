package com.amaan.inventorymanagementsystem

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_purchase.*
import java.util.Date
import java.text.SimpleDateFormat

class Purchase : AppCompatActivity() {

    private var finalUnits = 0
    private var inventoryCount: String?=null
    private var finalInventoryCount: Int =0

    private var mAuth: FirebaseAuth? = null
    private lateinit var databasePurchase: DatabaseReference
    private lateinit var database: DatabaseReference
    private lateinit var databaseInventory: DatabaseReference
    private var userEmail: String? = null

    private var date = Date();
    val formatter= SimpleDateFormat("MMM dd yyy HH:mma")
    private val currentDate :String = formatter.format(date)

    private var cashDebit : String ? = null
    private var finalCashDebit = 0
    private var cashCredit : String ? = null
    private var finalCashCredit = 0

    private var accountpayableDebit : String ? = null
    private var finalAccountpayableDebit = 0
    private var accountpayableCredit : String ? = null
    private var finalAccountpayableCredit = 0

    private var purchaseDebit: String ? = null
    private var finalPurchaseDebit = 0
    private var purchaseCredit: String ? = null
    private var finalPurchaseCredit = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase)
        supportActionBar!!.title = "Purchase"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        databasePurchase = FirebaseDatabase.getInstance().getReference("purchases")
        database = FirebaseDatabase.getInstance().reference
        databaseInventory = FirebaseDatabase.getInstance().getReference("inventory")

        mAuth = FirebaseAuth.getInstance()
        var curUser: FirebaseUser? = mAuth!!.currentUser
        userEmail = curUser!!.email
    }

    fun purchaseDoneEvent(view: View) {
        var units: String = purchaseUnits.text.toString()
        var finalUnits: Int = Integer.parseInt(units)

        var unitsPrice: String = purchaseUnitsPrice.text.toString()
        var finalUnitsPrice: Int = Integer.parseInt(unitsPrice)

        var cash: String = purchaseCash.text.toString()
        var finalCash: Int = Integer.parseInt(cash)

        var finalTotalCost = finalUnits * finalUnitsPrice
        purchaseTotalCost.setText(finalTotalCost.toString())

        var finalAccountspayable = (finalUnits * finalUnitsPrice) - finalCash
        purchaseAccountPayable.setText(finalAccountspayable.toString())

        savePurchase(
            purchaseUnits.text.toString(),
            purchaseUnitsPrice.text.toString(),
            purchaseTotalCost.text.toString(),
            purchaseCash.text.toString(),
            purchaseAccountPayable.text.toString()
        )
    }

    fun savePurchase(units: String, unitsPrice: String, totalCost: String, cash: String, accountpayable: String) {
        if (!TextUtils.isEmpty(units) && !TextUtils.isEmpty(unitsPrice) && !TextUtils.isEmpty(totalCost) && !TextUtils.isEmpty(cash) && !TextUtils.isEmpty(accountpayable)) {
            var id: String? = null
            id = databasePurchase.push().getKey()!!
            var purchase = PurchasesClass(id, userEmail, units, unitsPrice, totalCost, cash, accountpayable, currentDate)
            databasePurchase.child(id).setValue(purchase)
            Toast.makeText(this, "Purchase Made", Toast.LENGTH_LONG).show()
            updateAccounts()
            checkInventory(units, unitsPrice)
        }
    }

    fun checkInventory(units: String, unitsPrice: String) {
         finalUnits = Integer.parseInt(units)

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.hasChild("inventory"))
                {
                    // If a unit price node already exists, update it
                    if (p0.child("inventory").hasChild(unitsPrice)) {
                        inventoryCount = p0.child("inventory").child(unitsPrice).child("units").value.toString()
                        finalInventoryCount = Integer.parseInt(inventoryCount!!)
                        finalUnits += finalInventoryCount
                        saveInventory(finalUnits, unitsPrice)
                    }
                    else {
                        saveInventory(finalUnits, unitsPrice)
                    }
                }
                else{
                    saveInventory(finalUnits, unitsPrice)
                }

            }
        })
    }
    fun saveInventory(units: Int, unitsPrice :String){
        val inventory = InventoryClass(units, unitsPrice, userEmail)
        databaseInventory.child(unitsPrice).setValue(inventory)

        purchaseUnits.text = null
        purchaseUnitsPrice.text = null
//        purchaseTotalCost.text = "0"
        purchaseCash.text = null
//        purchaseAccountPayable.text = "0"
    }
    private fun updateAccounts(){
        database = FirebaseDatabase.getInstance().reference
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.hasChild("businessAccounts")){
                    var cash = purchaseCash.text.toString()
                    var finalCash = Integer.parseInt(cash)
                    var accpay= purchaseAccountPayable.text.toString()
                    var finalAccPay = Integer.parseInt(accpay)
                    var pur = purchaseTotalCost.text.toString()
                    var finalPur = Integer.parseInt(pur)

                    cashDebit = p0.child("businessAccounts").child("Cash").child("debit").value.toString()
                    finalCashDebit = Integer.parseInt(cashDebit!!)
                    cashCredit = p0.child("businessAccounts").child("Cash").child("credit").value.toString()
                    finalCashCredit = finalCash + Integer.parseInt(cashCredit!!)
                    saveAccounts("Cash", "credit", finalCashDebit.toString(), finalCashCredit.toString())

                    accountpayableDebit =
                        p0.child("businessAccounts").child("Account Payable").child("debit").value.toString()
                    finalAccountpayableDebit = Integer.parseInt(accountpayableDebit!!)
                    accountpayableCredit =
                        p0.child("businessAccounts").child("Account Payable").child("credit").value.toString()
                    finalAccountpayableCredit = finalAccPay + Integer.parseInt(accountpayableCredit!!)
                    saveAccounts(
                        "Account Payable",
                        "credit",
                        finalAccountpayableDebit.toString(),
                        finalAccountpayableCredit.toString()
                    )

                    purchaseDebit = p0.child("businessAccounts").child("Purchase").child("debit").value.toString()
                    finalPurchaseDebit = finalPur +Integer.parseInt(purchaseDebit!!)
                    purchaseCredit = p0.child("businessAccounts").child("Purchase").child("credit").value.toString()
                    finalPurchaseCredit = Integer.parseInt(purchaseCredit!!)
                    saveAccounts("Purchase", "debit", finalPurchaseDebit.toString(), finalPurchaseCredit.toString())
                }
                else{
                    saveAccounts("Cash", "any" , finalCashDebit.toString() ,purchaseCash.text.toString())
                    saveAccounts("Account Payable", "credit" , finalAccountpayableDebit.toString() ,purchaseAccountPayable.text.toString())
                    saveAccounts("Purchase", "debit" , purchaseTotalCost.text.toString(),finalPurchaseCredit.toString())
                }
            }
        })
    }
    fun saveAccounts(title:String, type:String, deb : String , cred : String){
        var businessAccount = BusinessAccountsClass(title,type,deb,cred,userEmail)
        database = FirebaseDatabase.getInstance().getReference("businessAccounts")
        database.child(title).setValue(businessAccount)
    }
}