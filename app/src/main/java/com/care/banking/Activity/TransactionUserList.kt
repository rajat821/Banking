package com.care.banking.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.care.banking.Adapter.TransactionDetailsAdapter
import com.care.banking.Adapter.TrasactionUserAdapter
import com.care.banking.Adapter.UserListAdapter
import com.care.banking.Database.UserDatabase
import com.care.banking.Database.Users
import com.care.banking.R
import com.care.banking.ThreadExecutor

class TransactionUserList : AppCompatActivity() {

    lateinit var TURecycler : RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var adapter : TrasactionUserAdapter
    lateinit var userDatabase : UserDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.transaction_user_list)

        TURecycler = findViewById(R.id.transaction_user_recycler)
        var senderAccNo : Long? = null
        var customer : String? = null

        if (intent != null){
            senderAccNo = intent.getLongExtra("accNo",-1)
            customer = intent.getStringExtra("customer")
        }


        userDatabase = UserDatabase.getDatabase(this@TransactionUserList)

        var userList: ArrayList<Users>

        ThreadExecutor.getInstance()?.diskIO?.execute(Runnable {
            kotlin.run {
                userList = userDatabase.userDao().getReceiver(senderAccNo!!) as ArrayList<Users>
                runOnUiThread(
                    Runnable {
                        kotlin.run {
                            findViewById<TextView>(R.id.customer_name).text = customer
                            layoutManager = LinearLayoutManager(this@TransactionUserList)
                            adapter = TrasactionUserAdapter(this@TransactionUserList, userList,senderAccNo)
                            TURecycler.adapter = adapter
                            TURecycler.layoutManager = layoutManager
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