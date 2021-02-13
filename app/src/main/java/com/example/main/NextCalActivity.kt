package com.example.main

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
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

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
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

        date = intent.getStringExtra("date").toString()
        var que = intent.getStringExtra("ques")
        var ans = intent.getStringExtra("ans")
        var pic = intent.getByteArrayExtra("pic")
        var emo = intent.getStringExtra("emotion")

        dateTextView.text = date
        question.text = que
        answer.text = ans
        if (ans.isNullOrBlank()) {
            answer.visibility = View.GONE
        }

        var picBit: Bitmap = BitmapFactory.decodeByteArray(pic, 0, pic!!.size)
        diaryImageView.setImageBitmap(picBit)

        if (pic.isEmpty()==true) diaryImageView.visibility = View.GONE

        //emotion부분에 가져오는 값에 따라 나타나는 이미지가 다름
        when (emo) {
            "Happy" -> emotion.setImageResource(R.drawable.ic_baseline_sentiment_very_satisfied_24)
            "Good" -> emotion.setImageResource(R.drawable.ic_baseline_sentiment_satisfied_alt_24)
            "Soso" -> emotion.setImageResource(R.drawable.ic_baseline_sentiment_neutral_24)
            "Bad" -> emotion.setImageResource(R.drawable.ic_baseline_sentiment_very_dissatisfied_24)
            else -> emotion.visibility = View.GONE
        }

        closeButton.setOnClickListener {
            //이전화면으로 이동
            finishAndRemoveTask()
        }

        deleteButton.setOnClickListener {
            //해당 날짜 내용 삭제

            sqlDB = dbManager2.writableDatabase

            sqlDB.execSQL( "DELETE FROM list WHERE date ='"+dateTextView.text+"' AND logonoff ='On';")


            Toast.makeText(this, "삭제되었습니다", Toast.LENGTH_SHORT).show()
            sqlDB.close()
            finish()
        }
    }
}