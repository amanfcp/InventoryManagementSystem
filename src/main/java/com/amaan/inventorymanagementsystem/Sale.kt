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
import kotlinx.android.synthetic.main.activity_sale.*
import java.text.SimpleDateFormat
import java.util.*

class Sale : AppCompatActivity() {

    private var units:String ?= null
    private var finalUnits = 0
    private var inventoryCount: String= " "
    private var finalInventoryCount: Int =0


    private var mAuth: FirebaseAuth?=null
    private lateinit var database: DatabaseReference
    private lateinit var databaseSales: DatabaseReference
    private lateinit var databaseInventory: DatabaseReference
    private lateinit var databasePurchases: DatabaseReference
    private var userEmail :String?= null

    private var date = Date();
    private val formatter= SimpleDateFormat("MMM dd yyy HH:mma")
    private val currentDate :String = formatter.format(date)

    private var cashDebit : String ? = null
    private var finalCashDebit = 0
    private var cashCredit : String ? = null
    private var finalCashCredit = 0

    private var accountReceivableDebit : String ? = null
    private var finalAccountReceivableDebit = 0
    private var accountReceivableCredit : String ? = null
    private var finalAccountReceivableCredit = 0

    private var saleDebit: String ? = null
    private var finalSaleDebit = 0
    private var saleCredit: String ? = null
    private var finalSaleCredit = 0

    private var cogsDebit: String ? = null
    private var finalCogsDebit = 0
    private var cogsCredit: String ? = null
    private var finalCogsCredit = 0

    private var purchaseDebit: String ? = null
    private var finalPurchaseDebit = 0
    private var purchaseCredit: String ? = null
    private var finalPurchaseCredit = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sale)
        supportActionBar!!.title = "Sale"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        database = FirebaseDatabase.getInstance().reference
        databaseSales = FirebaseDatabase.getInstance().getReference("sales")
        databasePurchases = FirebaseDatabase.getInstance().getReference("purchases")
        databaseInventory= FirebaseDatabase.getInstance().getReference("inventory")

        mAuth = FirebaseAuth.getInstance()
        val curUser : FirebaseUser? = mAuth!!.currentUser
        userEmail = curUser!!.email

    }


    fun saleDoneEvent (view: View){
        var units: String = saleUnits.text.toString()
        var finalUnits: Int = Integer.parseInt(units)

        var unitsPrice: String = saleUnitsPrice.text.toString()
        var finalUnitsPrice: Int = Integer.parseInt(unitsPrice)

        var cash: String = saleCash.text.toString()
        var finalCash: Int = Integer.parseInt(cash)

        var finalTotalCost = finalUnits * finalUnitsPrice
        saleTotalCost.setText(finalTotalCost.toString())

        var finalAccountReceivable= (finalUnits * finalUnitsPrice) - finalCash
        saleAccountReceivable.setText(finalAccountReceivable.toString())



        units = saleUnits.text.toString()
        finalUnits = Integer.parseInt(units!!)

        database.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.hasChild("inventory") && !TextUtils.isEmpty(saleUnits.toString())
                        && !TextUtils.isEmpty(saleUnitsPrice.toString())
                        && !TextUtils.isEmpty(saleCash.toString())){
                    for (data in p0.child("inventory").children) {
                        if (data.child("email").value!! == userEmail!!) {
                            inventoryCount = data.child("units").value.toString()
                            finalInventoryCount = Integer.parseInt(inventoryCount)
                            if (finalInventoryCount > 0 && finalUnits > 0) {
                                if (finalInventoryCount < finalUnits) {
                                    finalUnits -= finalInventoryCount
                                    finalInventoryCount = 0
                                    databaseInventory.child(data.key.toString()).removeValue()
                                } else if (finalInventoryCount >= finalUnits) {
                                    finalInventoryCount -= finalUnits
                                    finalUnits = 0
                                    saveInventory(finalInventoryCount, data.child("unitsPrice").value.toString())
                                    saveSale(saleUnits.text.toString(),
                                            saleUnitsPrice.text.toString(),
                                            saleTotalCost.text.toString(),
                                            saleCash.text.toString(),
                                            saleAccountReceivable.text.toString()
                                    )
                                    updateAccounts()
                                    break
                                }
                            }
                            else if(finalInventoryCount == 0){
                                databaseInventory.child(data.key.toString()).removeValue()
                            }
                        }
                    }
                }else{
                    Toast.makeText(applicationContext, "Inventory does not have enough Units", Toast.LENGTH_LONG).show()
                }
            }

        })
    }

    fun saveSale(units : String, unitsPrice : String, totalCost : String ,cash : String , accountpayable : String){
        if(!TextUtils.isEmpty(units) && !TextUtils.isEmpty(unitsPrice) && !TextUtils.isEmpty(cash)){
            val id = databaseSales.push().key!!
            val sale= SalesClass(id, userEmail, units, unitsPrice, totalCost, cash, accountpayable,currentDate)
            databaseSales.child(id).setValue(sale)
            Toast.makeText(this,"Sale Complete", Toast.LENGTH_LONG).show()
        }
    }
    fun saveInventory(units: Int, unitsPrice :String){
        val inventory = InventoryClass(units, unitsPrice, userEmail)
        databaseInventory.child(unitsPrice).setValue(inventory)
    }
    fun updateAccounts(){
        database = FirebaseDatabase.getInstance().reference
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.hasChild("businessAccounts")){
                    val cash = saleCash.text.toString()
                    val finalCash = Integer.parseInt(cash)
                    val accRec= saleAccountReceivable.text.toString()
                    val finalAccRec = Integer.parseInt(accRec)
                    val sale = saleTotalCost.text.toString()
                    val finalSale = Integer.parseInt(sale)

                    Log.i("updateAccounts","we're in Update Accounts old")
                    cashDebit = p0.child("businessAccounts").child("Cash").child("debit").value.toString()
                    finalCashDebit = finalCash + Integer.parseInt(cashDebit!!)
                    cashCredit = p0.child("businessAccounts").child("Cash").child("credit").value.toString()
                    finalCashCredit =  Integer.parseInt(cashCredit!!)
                    saveAccounts("Cash", "credit ", finalCashDebit.toString(), finalCashCredit.toString())

                    if(p0.child("businessAccounts").hasChild("Account Receivable")
                            && p0.child("businessAccounts").hasChild("Sales Revenue")
                            && p0.child("businessAccounts").hasChild("COGS") ){
                        accountReceivableDebit =
                                        p0.child("businessAccounts").child("Account Receivable").child("debit").value.toString()
                        finalAccountReceivableDebit = finalAccRec + Integer.parseInt(accountReceivableDebit!!)
                        accountReceivableCredit =
                                p0.child("businessAccounts").child("Account Receivable").child("credit").value.toString()
                        finalAccountReceivableCredit =  Integer.parseInt(accountReceivableCredit!!)
                        saveAccounts(
                                "Account Receivable",
                                "debit",
                                finalAccountReceivableDebit.toString(),
                                finalAccountReceivableCredit.toString()
                        )

                        saleDebit = p0.child("businessAccounts").child("Sales Revenue").child("debit").value.toString()
                        finalSaleDebit = Integer.parseInt(saleDebit!!)
                        saleCredit = p0.child("businessAccounts").child("Sales Revenue").child("credit").value.toString()
                        finalSaleCredit = finalSale + Integer.parseInt(saleCredit!!)
                        saveAccounts("Sales Revenue", "credit", finalSaleDebit.toString(), finalSaleCredit.toString())

                        cogsDebit = p0.child("businessAccounts").child("COGS").child("debit").value.toString()
                        //cogs debit main calculated cogs add hoga
                        finalCogsDebit = Integer.parseInt(cogsDebit!!)
                        cogsCredit = p0.child("businessAccounts").child("COGS").child("credit").value.toString()
                        finalCogsCredit = Integer.parseInt(cogsCredit!!)
                        saveAccounts("COGS","debit", finalCogsDebit.toString(), finalCogsCredit.toString())

                        purchaseDebit = p0.child("businessAccounts").child("Purchase").child("debit").value.toString()
                        finalPurchaseDebit = Integer.parseInt(purchaseDebit!!)
                        purchaseCredit = p0.child("businessAccounts").child("Purchase").child("credit").value.toString()
                        // purchase credit main calculated cogs add hoga
                        finalPurchaseCredit = Integer.parseInt(purchaseCredit!!)
                        saveAccounts("Purchase", "debit", finalPurchaseDebit.toString(), finalPurchaseCredit.toString())
                    }
                    else{
                        saveAccounts(
                                "Account Receivable",
                                "debit",
                                finalAccRec.toString(),
                                finalAccountReceivableCredit.toString()
                        )
                        saveAccounts("Sales Revenue", "credit", finalSaleDebit.toString(), finalSale.toString())
                        saveAccounts("COGS","debit", finalCogsDebit.toString(), finalCogsCredit.toString())
                    }
                }

                else{
                   Toast.makeText(applicationContext, "Sales cant be made",Toast.LENGTH_LONG).show()
                }
            }
        })
    }
    fun saveAccounts(title:String, type:String, deb : String , cred : String){
        val businessAccount = BusinessAccountsClass(title,type,deb,cred,userEmail)
        database = FirebaseDatabase.getInstance().getReference("businessAccounts")
        database.child(title).setValue(businessAccount)
    }
}