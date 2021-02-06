package com.example.main

import android.app.PendingIntent.getActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ToggleButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.system.exitProcess

class NextCalActivity : AppCompatActivity() {
    lateinit var closeButton: ImageButton
    lateinit var deleteButton: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next_cal)

        closeButton = findViewById(R.id.closeButton)
        deleteButton = findViewById(R.id.deleteButton)

        closeButton.setOnClickListener {
            onBackPressed()
        }
    }
}