package com.care.banking.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TransactionDao {

    @Insert
    fun insertTrans(transaction: Transaction)

    @Query("SELECT * FROM 'transaction' WHERE sender_acc_no = :sender or receiver_acc_no = :sender")
    fun getTransactions(sender: Long): List<Transaction>

}