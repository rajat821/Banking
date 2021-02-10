package com.care.banking.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.care.banking.Activity.AccountDetails
import com.care.banking.Activity.TransactionUserList
import com.care.banking.Database.Users
import com.care.banking.R
import java.text.DecimalFormat

class UserListAdapter(val context: Context, private val userList:ArrayList<Users>) : RecyclerView.Adapter<UserListAdapter.ULViewHolder>() {

    inner class ULViewHolder(view: View):RecyclerView.ViewHolder(view){
        val customerName: TextView = view.findViewById(R.id.customer_name)
        val accNo: TextView = view.findViewById(R.id.account_no)
        val balance : TextView = view.findViewById(R.id.balance)
        val viewDetails : Button = view.findViewById(R.id.btn_acc_details)
        val card : CardView = view.findViewById(R.id.card)
        val transfer : Button = view.findViewById(R.id.btn_transfer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ULViewHolder {
        val view = ULViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.user_single_item, parent, false)
        )
        return view
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ULViewHolder, position: Int) {
        val formatter = DecimalFormat("##,##,###.00")
        val user = userList[position]
        holder.accNo.text = "Acc No. : XXXXXX${user.accNo.toString().substring(6)}"
        holder.balance.text = "Rs.${formatter.format(user.balance)}"
        holder.customerName.text = "Name :  ${user.customerName}"

        holder.viewDetails.setOnClickListener {
            val intent = Intent(context,AccountDetails::class.java)
            intent.putExtra("accNo",user.accNo)
            val activityOptions = ActivityOptions.makeSceneTransitionAnimation(context as Activity,
                Pair.create(holder.card,"cardAnim"))
            context.startActivity(intent,activityOptions.toBundle())
        }

        holder.transfer.setOnClickListener {
            val intent = Intent(context,TransactionUserList::class.java)
            intent.putExtra("accNo",user.accNo)
            intent.putExtra("customer",user.customerName)
            context.startActivity(intent)
            (context as Activity).overridePendingTransition(R.anim.zoom,R.anim.static_animation)
        }
    }

    override fun getItemCount(): Int {
       return userList.size
    }
}