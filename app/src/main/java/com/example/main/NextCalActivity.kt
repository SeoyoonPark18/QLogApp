package com.example.main

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.constraintlayout.widget.Constraints
import androidx.fragment.app.FragmentActivity
import com.example.main.ui.calendar.CalendarFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton


class NextCalActivity : AppCompatActivity() {
    lateinit var closeButton: ImageButton
    lateinit var deleteButton: FloatingActionButton
    lateinit var dateTextView: TextView
    lateinit var question: TextView
    lateinit var answer: TextView
    lateinit var diaryImageView: ImageView
    lateinit var emotion: ImageView

    lateinit var actionBar: ActionBar

    lateinit var sqlDB: SQLiteDatabase

    lateinit var dbManager2: DBManager2

    lateinit var date: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next_cal)

        actionBar = supportActionBar!!

        actionBar.hide()

        closeButton = findViewById(R.id.closeButton)
        deleteButton = findViewById(R.id.deleteButton)
        dateTextView = findViewById(R.id.dateText)
        question = findViewById(R.id.qTextView)
        answer = findViewById(R.id.aTextView)
        diaryImageView = findViewById(R.id.diaryImageView)
        emotion = findViewById(R.id.emotion)

        dbManager2 = DBManager2(this, "list", null, 1)

        date = intent.getStringExtra("date").toString()

        dateTextView.text = date

        sqlDB = dbManager2.readableDatabase

        var state = ""
        var que = ""
        var ans = ""
        var pic: ByteArray = byteArrayOf()
        var picBit: Bitmap
        var day = ""
        var emo = ""
        var add = false

        var cursor: Cursor = sqlDB.rawQuery("SELECT * FROM list WHERE date == '$date';", null)
        while (cursor.moveToNext() && add==false) {
            que = cursor.getString(cursor.getColumnIndex("ques"))
            ans = cursor.getString(cursor.getColumnIndex("ans"))
            day = cursor.getString(cursor.getColumnIndex("date"))
            state = cursor.getString(cursor.getColumnIndex("logonoff"))
            emo = cursor.getString(cursor.getColumnIndex("emotion"))
            if (cursor.getBlob(cursor.getColumnIndex("pic")) != null)
                pic = cursor.getBlob((cursor.getColumnIndex("pic")))
            if (dateTextView.text == day) {
                add = true
                break
            }
        }

        if (dateTextView.text == day && state == "On") {
            question.text = que

            if (ans.isNullOrBlank()) {
                answer.visibility = View.GONE
            } else {
                answer.text = ans
            }

            if (pic.none()) {
                diaryImageView.visibility = View.GONE
            } else {
                picBit = BitmapFactory.decodeByteArray(pic, 0, pic.size)
                diaryImageView.setImageBitmap(picBit)
            }
        } else if (date != dateTextView.text) {
            question.visibility = View.GONE
            question.text = ""
            answer.visibility = View.GONE
            answer.text = ""
            diaryImageView.visibility = View.GONE
        }

        cursor.close()
        sqlDB.close()

        //emotion database
        when (emo) {
            "Happy" -> emotion.setImageResource(R.drawable.ic_baseline_sentiment_very_satisfied_24)
            "Good" -> emotion.setImageResource(R.drawable.ic_baseline_sentiment_satisfied_alt_24)
            "Soso" -> emotion.setImageResource(R.drawable.ic_baseline_sentiment_neutral_24)
            "Bad" -> emotion.setImageResource(R.drawable.ic_baseline_sentiment_very_dissatisfied_24)
            else -> emotion.visibility = View.GONE
        }

        closeButton.setOnClickListener {
            onBackPressed()
        }

        deleteButton.setOnClickListener {
            //해당 날짜 내용 삭제
            sqlDB = dbManager2.writableDatabase

            sqlDB.execSQL( "DELETE FROM list WHERE date ='"+dateTextView.text+"' AND logonoff ='On';")


            Toast.makeText(this, "삭제되었습니다", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}