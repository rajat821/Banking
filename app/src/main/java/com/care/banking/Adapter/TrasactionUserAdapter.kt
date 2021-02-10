package com.care.banking.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.care.banking.Activity.TransactionPage
import com.care.banking.Database.Users
import com.care.banking.R

class TrasactionUserAdapter(val context: Context, private val userList : ArrayList<Users>,
                            private val senderAccNo : Long) : RecyclerView.Adapter<TrasactionUserAdapter.TUViewHolder>(){

    inner class TUViewHolder(view: View):RecyclerView.ViewHolder(view){
        val customerName: TextView = view.findViewById(R.id.customer_name)
        val accNo: TextView = view.findViewById(R.id.account_no)
        val sendButton : Button = view.findViewById(R.id.btn_send)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TUViewHolder {
        val view = TUViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.transaction_user_single_item, parent, false)
        )
        return view
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TUViewHolder, position: Int) {
        val user = userList[position]

        holder.customerName.text = "Name :  ${user.customerName}"
        holder.accNo.text = "Acc/No. : XXXXXX${user.accNo.toString().substring(6)}"
        holder.sendButton.setOnClickListener{
            val intent = Intent(context,TransactionPage::class.java)
            intent.putExtra("senderAccNo",senderAccNo)
            intent.putExtra("receiverAccNo",user.accNo)
            context.startActivity(intent)
            (context as Activity).overridePendingTransition(R.anim.zoom,R.anim.static_animation)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}