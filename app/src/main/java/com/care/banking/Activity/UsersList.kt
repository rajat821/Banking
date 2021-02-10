package com.care.banking.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.care.banking.Adapter.UserListAdapter
import com.care.banking.Database.UserDatabase
import com.care.banking.Database.Users
import com.care.banking.R
import com.care.banking.ThreadExecutor

class UsersList : AppCompatActivity() {

    lateinit var URecycler: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var adapter: UserListAdapter
    lateinit var userDatabase: UserDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.users_list)

        URecycler = findViewById(R.id.user_recycler)
        userDatabase = UserDatabase.getDatabase(this@UsersList)

        var userList: ArrayList<Users>

        ThreadExecutor.getInstance()?.diskIO?.execute(Runnable {
            kotlin.run {
                userList = userDatabase.userDao().getAllUsers() as ArrayList<Users>
                runOnUiThread(
                    Runnable {
                        kotlin.run {
                            layoutManager = LinearLayoutManager(this@UsersList)
                            URecycler.layoutManager = layoutManager
                            adapter = UserListAdapter(this@UsersList, userList)
                            URecycler.adapter = adapter
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