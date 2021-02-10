package com.care.banking.Activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import com.care.banking.Adapter.UserListAdapter
import com.care.banking.Database.Transaction
import com.care.banking.Database.UserDatabase
import com.care.banking.Database.Users
import com.care.banking.R
import com.care.banking.ThreadExecutor
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.FadingCircle
import com.github.ybq.android.spinkit.style.Wave
import org.w3c.dom.Text
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class TransactionPage : AppCompatActivity() {

    lateinit var amountEditText : EditText
    lateinit var userDatabase: UserDatabase
    lateinit var layout : RelativeLayout
    lateinit var progress : ProgressBar

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.transaction_page)

        amountEditText = findViewById(R.id.amountEditText)
        userDatabase = UserDatabase.getDatabase(this@TransactionPage)
        layout = findViewById(R.id.layout)
        progress = findViewById(R.id.progress)
        val fdCircle: Sprite = FadingCircle()
        progress.indeterminateDrawable = fdCircle
        var senderAccNo : Long? = null
        var receiverAccNo : Long? = null
        var senderName : String? = null
        var receiverName : String? = null
        var receiverBalance : Float? = null
        var sender : Users
        var receiver : Users
        var balance : Float? = null
        val formatter = DecimalFormat("##,##,###.00")

        if(intent != null){
            senderAccNo = intent.getLongExtra("senderAccNo",-1)
            receiverAccNo = intent.getLongExtra("receiverAccNo",-1)
        }


        ThreadExecutor.getInstance()?.diskIO?.execute(Runnable {
            kotlin.run {
                sender = userDatabase.userDao().getOneUser(senderAccNo!!)
                receiver = userDatabase.userDao().getOneUser(receiverAccNo!!)
                balance = sender.balance
                receiverBalance = receiver.balance
                senderName = sender.customerName
                receiverName = receiver.customerName
                runOnUiThread(
                    Runnable {
                        kotlin.run {
                            findViewById<TextView>(R.id.sender_name).text = sender.customerName
                            findViewById<TextView>(R.id.receiver_name).text = receiver.customerName
                            findViewById<TextView>(R.id.sender_acc_no).text = sender.accNo.toString()
                            findViewById<TextView>(R.id.receiver_acc_no).text = "XXXXXX${receiver.accNo.toString().substring(6)}"
                            findViewById<TextView>(R.id.balance).text = "Rs.${formatter.format(sender.balance)}"
                        }
                    }
                )
            }
        })


        findViewById<ImageView>(R.id.back_btn).setOnClickListener {
            onBackPressed()
        }

        findViewById<Button>(R.id.btn_send).setOnClickListener {
            if(amountEditText.text.toString().isEmpty() || amountEditText.text.toString().toFloat() == 0.0f){
                amountEditText.error = "Enter valid Amount"
            }
            else if(amountEditText.text.toString().toFloat() > balance!!){
                amountEditText.error = "Amount exceeds Av. Balance"
            }
            else{
                amountEditText.error = null
                layout.visibility = View.VISIBLE
                progress.visibility = View.VISIBLE
                Handler().postDelayed(Runnable {
                    kotlin.run {
                        val refId = UUID.randomUUID().toString().replace("-","")
                        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
                        val currentDate = sdf.format(Date())
                        val dateTime = currentDate
                        val amount = amountEditText.text.toString().toFloat()
                        val customDialog : Dialog = Dialog(this@TransactionPage)
                        ThreadExecutor.getInstance()?.diskIO?.execute(Runnable {
                            kotlin.run {
                                userDatabase.trasactionDao().insertTrans(
                                    Transaction(refId,senderName!!,receiverName!!,senderAccNo!!,receiverAccNo!!,amount,dateTime)
                                )

                                userDatabase.userDao().updateBalance(senderAccNo,balance!!-amount)
                                userDatabase.userDao().updateBalance(receiverAccNo,receiverBalance!!+amount)

                                runOnUiThread(Runnable {
                                    kotlin.run {
                                        progress.visibility = View.GONE
                                        customDialog.setContentView(R.layout.transaction_dialog)
                                        val amountPaid :TextView = customDialog.findViewById(R.id.amountPaid)
                                        val transId : TextView = customDialog.findViewById(R.id.trans_id)
                                        val date : TextView = customDialog.findViewById(R.id.dateTime)
                                        val receName : TextView = customDialog.findViewById(R.id.receiver_name)
                                        val receiverAmount : TextView = customDialog. findViewById(R.id.receiver_amount)
                                        val sendName : TextView = customDialog.findViewById(R.id.sender_name)
                                        val senderAmount : TextView = customDialog. findViewById(R.id.sender_amount)
                                        val doneBtn : TextView = customDialog. findViewById(R.id.btn_done)
                                        amountPaid.text = "Paid :  Rs.${formatter.format(amount)}"
                                        transId.text = "Ref. Id : $refId"
                                        date.text = dateTime.toString()
                                        receName.text = receiverName
                                        sendName.text = senderName
                                        receiverAmount.text = "+ ${formatter.format(amount)}"
                                        senderAmount.text = "- ${formatter.format(amount)}"
                                        doneBtn.setOnClickListener {
                                            val intent= Intent(this,MainActivity::class.java)
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                            startActivity(intent)
                                            overridePendingTransition(R.anim.zoom,R.anim.static_animation)
                                            finish()
                                        }
                                        customDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                                        customDialog.setCanceledOnTouchOutside(false)
                                        customDialog.show()
                                    }
                                })
                            }
                        })

                    }
                               },2000)







            }

        }

    }

    override fun onBackPressed() {
        val dialog = AlertDialog.Builder(this@TransactionPage)
        dialog.setTitle("!! Alert !!")
        dialog.setMessage("Do you want to cancel the transaction?")
        dialog.setPositiveButton("Yes") { text, which ->
            val intent= Intent(this,MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            overridePendingTransition(R.anim.zoom,R.anim.static_animation)
            finish()
        }
        dialog.setNegativeButton("No"){text,which ->
        }
        val alert = dialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
    }
}