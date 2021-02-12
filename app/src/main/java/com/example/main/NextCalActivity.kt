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

private const val REQUEST_MSG_CODE = 1

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
    lateinit var datesql: SQLiteDatabase

    lateinit var dbManager2: DBManager2
    lateinit var dateDBManager: dateDBManager

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

        dbManager2 = DBManager2(this, "list", null, 2)
        dateDBManager = dateDBManager(this, "dateDB", null, 1)

        dateTextView.text = intent.getStringExtra("KEY_DATE")
        question.text = intent.getStringExtra("KEY_QUESTION")
        answer.text = intent.getStringExtra("KEY_ANSWER")
        var emo = intent.getStringExtra("KEY_EMO")
        var date = intent.getStringExtra("DATE")
        var id = intent.getStringExtra("ID")
        var dateId = intent.getStringExtra("DATEID")
        intent.extras!!
        val byteArray: ByteArray = intent.getByteArrayExtra("KEY_IMAGE")!!
        val bitmap: Bitmap

        if(byteArray.isNotEmpty()) {
            bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            diaryImageView.setImageBitmap(bitmap)
        } else {
            diaryImageView.visibility = View.GONE
        }

        if (answer.text.isNullOrBlank()){
            answer.visibility = View.GONE
        }

        //emotion database
        when(emo){
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
            var year = 0
            var month = 0
            var day = 0

            datesql = dateDBManager.writableDatabase
            var cursor:Cursor = datesql.rawQuery("SELECT * FROM dateDB WHERE date='{${dateTextView.text}';", null)
            while (cursor.moveToNext()){
                year = cursor.getInt(cursor.getColumnIndex("year"))
                month = cursor.getInt(cursor.getColumnIndex("month"))
                day = cursor.getInt(cursor.getColumnIndex("day"))
            }
            sqlDB = dbManager2.writableDatabase
            question.text = ""
            answer.text = ""
            emotion.visibility = View.GONE

            if (id == dateId) {
                sqlDB.execSQL("UPDATE list SET ques= 0 WHERE date='${dateTextView.text}';")
                sqlDB.execSQL("UPDATE list SET ans= 0 WHERE date='${dateTextView.text}';")
                sqlDB.execSQL("UPDATE list SET pic= 0 WHERE date='${dateTextView.text}';")
                sqlDB.execSQL("UPDATE list SET emotion = 0 WHERE date = '${dateTextView}';'")
                sqlDB.execSQL("UPDATE list SET date= 0 WHERE date = '${id}';'")
                datesql.execSQL("DELETE FROM dateDB WHERE date ='" + dateTextView.text + "';")
            }

            var intent = Intent()
            intent.putExtra("reque", question.text)
            intent.putExtra("reans", answer.text)
            intent.putExtra("year", year)
            intent.putExtra("month", month)
            intent.putExtra("day", day)
            CalendarFragment::imageView.set(CalendarFragment(),diaryImageView)

            onActivityResult(REQUEST_MSG_CODE, RESULT_OK, intent)

            Toast.makeText(this, "삭제되었습니다", Toast.LENGTH_SHORT).show()

            sqlDB.close()
            datesql.close()
        }
    }
}