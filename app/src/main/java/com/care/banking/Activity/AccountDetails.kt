package com.care.banking.Activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import com.care.banking.Adapter.UserListAdapter
import com.care.banking.Database.UserDatabase
import com.care.banking.Database.Users
import com.care.banking.R
import com.care.banking.ThreadExecutor
import org.w3c.dom.Text
import java.text.DecimalFormat

class AccountDetails : AppCompatActivity() {

    lateinit var transferButton : Button
    lateinit var userDatabase: UserDatabase

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_details)

        transferButton = findViewById(R.id.btn_transfer)
        userDatabase = UserDatabase.getDatabase(this@AccountDetails)

        var user : Users
        var accNo : Long? = null
        var customer : String? = null
        val formatter = DecimalFormat("##,##,###.00")

        if(intent!=null){
            accNo = intent.getLongExtra("accNo",-1)
        }

        ThreadExecutor.getInstance()?.diskIO?.execute(Runnable {
            kotlin.run {
                user = userDatabase.userDao().getOneUser(accNo!!)
                customer = user.customerName
                runOnUiThread(
                    Runnable {
                        kotlin.run {
                            findViewById<TextView>(R.id.customer_name).text = user.customerName
                            findViewById<TextView>(R.id.account_no).text = "Acc/No. :  ${user.accNo}"
                            findViewById<TextView>(R.id.ifscCode).text = "IFSC Code :  ${user.ifsc}"
                            findViewById<TextView>(R.id.email).text = "Email :  ${user.email}"
                            findViewById<TextView>(R.id.phoneNo).text = "Phone No. :  ${user.phone}"
                            findViewById<TextView>(R.id.address).text = "Address :  ${user.address}"
                            findViewById<TextView>(R.id.balance).text = "Balance :  Rs.${formatter.format(user.balance)}"
                        }
                    }
                )
            }
        })

        findViewById<CardView>(R.id.back_btn).setOnClickListener {
            onBackPressed()
        }

        findViewById<Button>(R.id.btn_transfer).setOnClickListener {
            val intent = Intent(this@AccountDetails,TransactionUserList::class.java)
            intent.putExtra("accNo",accNo)
            intent.putExtra("customer",customer)
            startActivity(intent)
            overridePendingTransition(R.anim.zoom,R.anim.static_animation)
        }

        findViewById<TextView>(R.id.viewTransaction).setOnClickListener {
            val intent =Intent(this@AccountDetails,TransactionDetails::class.java)
            intent.putExtra("accNo",accNo)
            intent.putExtra("customer",customer)
            startActivity(intent)
            overridePendingTransition(R.anim.zoom,R.anim.static_animation)
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.static_animation,R.anim.zoom_out)
    }
}