package com.example.main

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
    lateinit var DBManager: DBManager

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
        val byteArray: ByteArray = intent.getByteArrayExtra("KEY_IMAGE")!!

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
        if (emotion.drawable == null){
            emotion.visibility = View.GONE
        }

        closeButton.setOnClickListener {
            onBackPressed()
        }

        deleteButton.setOnClickListener {
            //데이터베이스
            /*sqlDB = DBManager.writableDatabase
            sqlDB.execSQL("DELETE FROM registerTBL WHERE gDate = '" + dateTextView.text.toString() + "';")

            sqlDB.close()*/
            onBackPressed()
            Toast.makeText(this, "삭제되었습니다", Toast.LENGTH_SHORT).show()
        }
    }
}