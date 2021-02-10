package com.example.main

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Friend_Activity : AppCompatActivity()  {

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var date: TextView
    lateinit var question: TextView
    lateinit var answer: TextView

    lateinit var str_name: String
    lateinit var str_date: String
    lateinit var str_question: String
    lateinit var str_answer: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend)

        date = findViewById(R.id.date)
        question = findViewById(R.id.question)
        answer = findViewById(R.id.answer)

        val intent = intent
        str_name = intent.getStringExtra("intent_name").toString()


    }
}