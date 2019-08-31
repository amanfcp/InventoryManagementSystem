package com.amaan.inventorymanagementsystem

import android.content.Intent
import android.os.Bundle
//import android.support.design.widget.FloatingActionButton
//import android.support.design.widget.Snackbar
import androidx.core.view.GravityCompat
//import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
//import androidx.core.widget.DrawerLayout
//import android.support.design.widget.NavigationView
//import androidx.core.app.FragmentActivity
//import android.support.v7.app.AppCompatActivity
//import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_inventory.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_accounts.*
import kotlinx.android.synthetic.main.nav_header_nav_drawer.*


class NavDrawer : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var databaseBusinessAccountsReference: DatabaseReference
    lateinit var databaseAccountsReference: DatabaseReference
    private var mAuth: FirebaseAuth?=null
    private var userEmail: String? = null
    lateinit var adapter: BusinessAccountsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_drawer)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

//        val fab: FloatingActionButton = findViewById(R.id.fab)
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
        navView.isFocusableInTouchMode = false

        supportFragmentManager.beginTransaction().replace(R.id.fragment_contrainer, HomeFragment()).commit()
        navView.setCheckedItem(R.id.navHome)



        mAuth = FirebaseAuth.getInstance()
        var curUser : FirebaseUser? = mAuth!!.currentUser
        userEmail = curUser!!.email

        //database work
        databaseAccountsReference = FirebaseDatabase.getInstance().getReference("accounts")
        databaseBusinessAccountsReference = FirebaseDatabase.getInstance().getReference("businessAccounts")

        // Read from the database
        databaseAccountsReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for(data in dataSnapshot.children){
                    if(data.child("email").value.toString() == userEmail) {
                        navHeaderHeading1.text = data.child("name").value.toString()
                        navHeaderHeading2.text = data.child("businessName").value.toString()
                        supportActionBar!!.title = data.child("businessName").value.toString()
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Not Found", Toast.LENGTH_LONG).show()
            }
        })
    }


    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.nav_drawer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if(item.itemId == R.id.logout_button){
            userLogout()
        }
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
    fun userLogout(){
        mAuth!!.signOut()
        startActivity(Intent(applicationContext,MainActivity::class.java))
        finish()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.navHome -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_contrainer, HomeFragment()).commit()
            }
            R.id.navAccounts -> {
                supportActionBar!!.title = "Accounts"
                supportFragmentManager.beginTransaction().replace(R.id.fragment_contrainer, AccountsFragment()).commit()
            }
            R.id.navFStatements -> {
                supportActionBar!!.title = "Financial Statements"
                supportFragmentManager.beginTransaction().replace(R.id.fragment_contrainer, FinancialStatementsFragment()).commit()
            }
            R.id.navReports -> {
                supportActionBar!!.title = "Reports"
                supportFragmentManager.beginTransaction().replace(R.id.fragment_contrainer, ReportsFragment()).commit()
            }
            R.id.nav_share -> {
                Toast.makeText(this,"SHARE",Toast.LENGTH_SHORT).show()
            }
            R.id.nav_send -> {
                Toast.makeText(this,"SHARE",Toast.LENGTH_SHORT).show()
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun inventoryButtonEvent(view: View){
        val intent = Intent(this, Inventory::class.java)
        startActivity(intent)
    }
    fun transactionButtonEvent(view:View){
        val intent = Intent(this, Transaction::class.java)
        startActivity(intent)
    }


}
