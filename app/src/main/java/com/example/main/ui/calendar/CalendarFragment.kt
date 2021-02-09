package com.example.main.ui.calendar

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.main.DBManager
import com.example.main.NextCalActivity
import com.example.main.R
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.util.*

class CalendarFragment : Fragment() {

    private lateinit var calendarViewModel: CalendarViewModel
    lateinit var expansionButton: ImageButton
    lateinit var scrollText: ScrollView
    lateinit var calendarView: CalendarView
    lateinit var dateView: TextView
    lateinit var questionTextView: TextView
    lateinit var answerTextView: TextView
    lateinit var imageView: ImageView

    lateinit var sqlDB: SQLiteDatabase
    lateinit var DBManager: DBManager

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        calendarViewModel =
                ViewModelProvider(this).get(CalendarViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_calendar, container, false)
        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        expansionButton = view.findViewById(R.id.expansionButton)
        scrollText = view.findViewById(R.id.scrollText)
        calendarView = view.findViewById(R.id.calendarView)
        dateView = view.findViewById(R.id.dateView)
        questionTextView = view.findViewById(R.id.questionView)
        answerTextView = view.findViewById(R.id.answerView)
        imageView = view.findViewById(R.id.diaryImage)

        dateView.setText(LocalDate.now().year.toString() + "년 " +
                LocalDate.now().month.value.toString().toInt() + "월 " +
                LocalDate.now().dayOfMonth + "일")

        // 데이터 베이스 questionTextView.text

        if (answerTextView.text.isBlank()) {
            answerTextView.visibility = View.GONE
        } /*데이터 베이스else {

        }*/

        val stream = ByteArrayOutputStream()
        val bitmap: Bitmap
        val resize: Bitmap
        if (imageView.drawable != null) {
            bitmap = imageView.drawable.toBitmap()
            val scale: Float = 1024 / bitmap.width.toFloat()
            val image_w: Int = (bitmap.width * scale).toInt()
            val image_h: Int = (bitmap.height * scale).toInt()
            resize = Bitmap.createScaledBitmap(bitmap, image_w, image_h, true)
        } else {
            imageView.visibility = View.GONE
        }
        val byteArray: ByteArray = stream.toByteArray()

        expansionButton.setOnClickListener {
            val intent = Intent(activity, NextCalActivity::class.java)
            intent.putExtra("KEY_DATE", dateView.text.toString())
            intent.putExtra("KEY_QUESTION", questionTextView.text.toString())
            intent.putExtra("KEY_ANSWER", answerTextView.text.toString())
            intent.putExtra("KEY_IMAGE", byteArray)
            startActivity(intent)
        }

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val m: Int
            m = month + 1
            dateView.visibility = View.VISIBLE
            dateView.text = year.toString() + "년 " + m.toString() + "월 " + dayOfMonth + "일"
            //데이터 베이스 추가
        }
    }

}