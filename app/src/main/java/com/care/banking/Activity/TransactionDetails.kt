package com.care.banking.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.care.banking.Adapter.TransactionDetailsAdapter
import com.care.banking.Adapter.UserListAdapter
import com.care.banking.Database.Transaction
import com.care.banking.Database.UserDatabase
import com.care.banking.Database.Users
import com.care.banking.R
import com.care.banking.ThreadExecutor

class TransactionDetails : AppCompatActivity() {

    lateinit var TDRecycler : RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var adapter : TransactionDetailsAdapter
    lateinit var transDatabase : UserDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.transaction_details)

        TDRecycler = findViewById(R.id.transaction_details_recycler)
        layoutManager = LinearLayoutManager(this@TransactionDetails)
        transDatabase = UserDatabase.getDatabase(this@TransactionDetails)
        var customer : String? = null
        var accNo : Long? = null

        if (intent != null){
            customer = intent.getStringExtra("customer")
            accNo = intent.getLongExtra("accNo",-1)
        }

        var transList: ArrayList<Transaction>

        ThreadExecutor.getInstance()?.diskIO?.execute(Runnable {
            kotlin.run {
                transList = transDatabase.trasactionDao().getTransactions(accNo!!) as ArrayList<Transaction>
                if (transList.size == 0){
                    findViewById<TextView>(R.id.noTrans).visibility = View.VISIBLE
                }
                findViewById<TextView>(R.id.customer_name).text = customer
                runOnUiThread(
                    Runnable {
                        kotlin.run {
                            layoutManager = LinearLayoutManager(this@TransactionDetails)
                            adapter = TransactionDetailsAdapter(this@TransactionDetails, transList,accNo)
                            TDRecycler.adapter = adapter
                            TDRecycler.layoutManager = layoutManager
                        }
                    }
                )
            }
        })

        findViewById<CardView>(R.id.back_btn).setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.static_animation,R.anim.zoom_out)
    }
}