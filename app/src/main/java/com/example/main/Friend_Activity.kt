package com.example.main

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Friend_Activity : AppCompatActivity()  {

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var date: TextView
    lateinit var question: TextView
    lateinit var answer: TextView

    lateinit var str_id: String
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
        str_id = intent.getStringExtra("intent_id").toString()

        dbManager = DBManager(this, "list", null, 1)
        sqlitedb = dbManager.readableDatabase

        var cursor: Cursor
        cursor = sqlitedb.rawQuery("SELECT * FROM list WHERE id = '" + str_id + "';", null)

            if(cursor.moveToNext()) {
                str_date = cursor.getString(cursor.getColumnIndex("date")).toString()
                str_question = cursor.getString(cursor.getColumnIndex("ques")).toString()
                str_answer = cursor.getString(cursor.getColumnIndex("ans")).toString()
            }

        if(cursor.getString(cursor.getColumnIndex("date")).toString() == "null") {
            str_date = " "
        }
        if(cursor.getString(cursor.getColumnIndex("ques")).toString() == "null") {
            str_question = " "
        }
        if(cursor.getString(cursor.getColumnIndex("ans")).toString() == "null") {
            str_answer = "기록이 없습니다."
        }

        cursor.close()
        sqlitedb.close()
        dbManager.close()

        date.text = str_date
        question.text = str_question
        answer.text = str_answer
    }

}