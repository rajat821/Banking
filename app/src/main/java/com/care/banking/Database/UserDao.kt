package com.care.banking.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @Insert
    fun insert(users : Users)

    @Query("SELECT * FROM users")
    fun getAllUsers() : List<Users>

    @Query("UPDATE users SET balance = :bal WHERE account_no = :accNo")
    fun updateBalance(accNo : Long, bal : Float)

    @Query("SELECT * FROM users WHERE account_no = :accNo")
    fun getOneUser(accNo : Long): Users

    @Query("SELECT * FROM users WHERE account_no != :accNo")
    fun getReceiver(accNo : Long): List<Users>
}