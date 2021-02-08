package com.example.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class NextCalActivity : AppCompatActivity() {
    lateinit var closeButton: ImageButton
    lateinit var deleteButton: FloatingActionButton
    lateinit var dateTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next_cal)

        closeButton = findViewById(R.id.closeButton)
        deleteButton = findViewById(R.id.deleteButton)
        dateTextView = findViewById(R.id.dateText)

        dateTextView.text = intent.getStringExtra("KEY_DATE")

        closeButton.setOnClickListener {
            onBackPressed()
        }

        deleteButton.setOnClickListener {
            //데이터베이스
            onBackPressed()
        }
    }
}