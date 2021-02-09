package com.care.banking.Database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "transaction")
data class Transaction(
    @PrimaryKey @ColumnInfo(name = "trans_id") val transId : String,
    @ColumnInfo(name = "sender_name") val senderName : String,
    @ColumnInfo(name = "receiver_name") val receiverName : String,
    @ColumnInfo(name = "sender_acc_no") val senderAccNo : Long,
    @ColumnInfo(name = "receiver_acc_no") val receiverAccNo : Long,
    val amountPaid : Float,
    @ColumnInfo(name = "date_time") val dateTime : String,
)
