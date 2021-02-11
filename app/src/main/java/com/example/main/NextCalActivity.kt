package com.example.main

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

    lateinit var emo: String

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

        intent.extras!!
        dateTextView.text = intent.getStringExtra("KEY_DATE")
        question.text = intent.getStringExtra("KEY_QUESTION")
        answer.text = intent.getStringExtra("KEY_ANSWER")
        emo = intent.getStringExtra("KEY_EMO").toString()
        val byteArray: ByteArray = intent.getByteArrayExtra("KEY_IMAGE")!!

        sqlDB = SQLiteDatabase.openDatabase(
            "/data/data/com.example.main/databases/list",
            null, SQLiteDatabase.OPEN_READWRITE)

        if(byteArray.isNotEmpty()) {
            val bitmap: Bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            diaryImageView.setImageBitmap(bitmap)
        } else {
            diaryImageView.visibility = View.GONE
        }

        if (answer.text.isBlank()){
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
            //데이터베이스
            sqlDB.execSQL("DELETE FROM list WHERE date = '" + dateTextView.text.toString() + "';")
            sqlDB.close()
            onBackPressed()
            Toast.makeText(this, "삭제되었습니다", Toast.LENGTH_SHORT).show()
        }
    }
}