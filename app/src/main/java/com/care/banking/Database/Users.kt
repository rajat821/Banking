package com.care.banking.Database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class Users(
        @ColumnInfo(name = "customer_name") val customerName : String,
        val balance : Float,
        @ColumnInfo(name = "ifsc_code") val ifsc : String,
        @PrimaryKey @ColumnInfo(name = "account_no") val accNo : Long,
        @ColumnInfo(name = "phone_no") val phone : String,
        val email : String,
        val address : String
)
