package com.care.banking.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.care.banking.Database.Transaction
import com.care.banking.R
import java.text.DecimalFormat

class TransactionDetailsAdapter(val context: Context, val transList : ArrayList<Transaction>,val accNo : Long) :
    RecyclerView.Adapter<TransactionDetailsAdapter.TDViewHolder>() {

    inner class TDViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val customerName: TextView = view.findViewById(R.id.customer_name)
        val transId: TextView = view.findViewById(R.id.trans_id)
        val status: TextView = view.findViewById(R.id.status)
        val dateTime: TextView = view.findViewById(R.id.dateTime)
        val amountPaid: TextView = view.findViewById(R.id.amountPaid)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TDViewHolder {
        val view = TDViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.transaction_details_single_item, parent, false)
        )
        return view
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TDViewHolder, position: Int) {
        val trans = transList[position]

        holder.transId.text = "Ref. Id :  ${trans.transId}"
        holder.dateTime.text = trans.dateTime

        val formatter = DecimalFormat("##,##,###.00")

        if(accNo == trans.receiverAccNo){
            //from
            holder.customerName.text = "From : ${trans.senderName}"
            holder.status.text = "Credit"
            holder.status.setTextColor(Color.parseColor("#43C826"))
            holder.amountPaid.text = "+ Rs.${trans.amountPaid}"
            holder.amountPaid.setTextColor(Color.parseColor("#43C826"))
        }
        else if(accNo == trans.senderAccNo){
            //to
            holder.customerName.text = "To : ${trans.receiverName}"
            holder.status.text = "Debit"
            holder.status.setTextColor(Color.parseColor("#FF0E09"))
            holder.amountPaid.text = "- Rs.${formatter.format(trans.amountPaid)}"
            holder.amountPaid.setTextColor(Color.parseColor("#FF0E09"))
        }
    }

    override fun getItemCount(): Int {
        return transList.size
    }
}