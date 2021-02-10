package com.care.banking.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.care.banking.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_view_customers).setOnClickListener {
            startActivity(Intent(this@MainActivity, UsersList::class.java))
            overridePendingTransition(R.anim.zoom,R.anim.static_animation)
        }
    }
}