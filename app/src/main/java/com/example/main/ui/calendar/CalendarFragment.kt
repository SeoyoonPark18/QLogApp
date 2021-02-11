package com.example.main.ui.calendar

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat.*
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
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
import com.example.main.NextCalActivity
import com.example.main.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.InputStream
import java.time.LocalDate


open class CalendarFragment : Fragment() {

    private lateinit var calendarViewModel: CalendarViewModel
    lateinit var expansionButton: ImageButton
    lateinit var scrollText: ScrollView
    lateinit var calendarView: MaterialCalendarView
    lateinit var dateView: TextView
    lateinit var questionTextView: TextView
    lateinit var answerTextView: TextView
    lateinit var imageView: ImageView

    lateinit var state: String
    lateinit var que: String
    lateinit var ans: String
    lateinit var emo: String
    lateinit var pic: String
    lateinit var pic_uri: Uri

    lateinit var date: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        calendarViewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }


    @SuppressLint("WrongThread")
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

        calendarView.setSelectedDate(CalendarDay.today())

        dateView.setText(
            LocalDate.now().year.toString() + "년 " +
                    LocalDate.now().month.value.toString().toInt() + "월 " +
                    LocalDate.now().dayOfMonth + "일"
        )

        val sqlDB: SQLiteDatabase = SQLiteDatabase.openDatabase(
            "/data/data/com.example.main/databases/list",
            null, SQLiteDatabase.OPEN_READONLY
        )

        var writeDay: ArrayList<CalendarDay> = ArrayList()

        var year = 0
        var month = 0
        var day  = 0

        var cursor: Cursor = sqlDB.rawQuery("SELECT * FROM list;", null)
        while (cursor.moveToNext()){

            que = cursor.getString(cursor.getColumnIndex("ques"))
            ans = cursor.getString(cursor.getColumnIndex("ans"))
            date = cursor.getString(cursor.getColumnIndex("date"))
            year = cursor.getInt(cursor.getColumnIndex("year"))
            month = cursor.getInt(cursor.getColumnIndex("month"))
            day = cursor.getInt(cursor.getColumnIndex("day"))
            state = cursor.getString(cursor.getColumnIndex("logonoff"))
            emo = cursor.getString(cursor.getColumnIndex("emotion"))
            pic = cursor.getString(cursor.getColumnIndex("pic"))

            if(year != null && (month<=12 && month >=1) && (day >= 1&& day <= 31)){
                writeDay.add(CalendarDay.from(year, month, day))
            }

            if (dateView.text == date && state == "On" && que.isNullOrBlank() == false) {
                questionTextView.text = que
                if (ans == null) {
                    answerTextView.visibility = View.GONE
                } else {
                    answerTextView.text = ans
                }

                if (pic != null) {
                    try {
                        pic_uri = Uri.parse(pic)
                        val inputStream: InputStream? = activity?.contentResolver?.openInputStream(
                            pic_uri
                        )
                        val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
                        imageView.setImageBitmap(bitmap)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        calendarView.addDecorator(EventDecorator(Color.BLACK, writeDay))

        //extActivity로 이미지를 넘기기
        val stream = ByteArrayOutputStream()
        if (imageView.drawable != null) {
            val bitmap: Bitmap = imageView.drawable.toBitmap(390, 390)
            val scale: Float = 1024 / bitmap.width.toFloat()
            val image_w: Int = (bitmap.width * scale).toInt()
            val image_h: Int = (bitmap.height * scale).toInt()
            val resize: Bitmap = Bitmap.createScaledBitmap(bitmap, image_w, image_h, true)
            resize.compress(PNG, 100, stream)
        } else {
            imageView.visibility = View.GONE
        }
        val byteArray: ByteArray = stream.toByteArray()

        //확대버튼
        expansionButton.setOnClickListener {
            val intent = Intent(activity, NextCalActivity::class.java)
            intent.putExtra("KEY_DATE", dateView.text.toString())
            intent.putExtra("KEY_QUESTION", questionTextView.text.toString())
            intent.putExtra("KEY_ANSWER", answerTextView.text.toString())
            intent.putExtra("KEY_IMAGE", byteArray)
            intent.putExtra("KEY_EMO", emo)
            intent.putExtra("DATE", date)
            startActivity(intent)
        }

        //다른 날짜를 눌렀을 때
        calendarView.setOnDateChangedListener{ widget, date, selected ->
            val bitmap: Bitmap
            dateView.text = date.year.toString() + "년 " + date.month.toString() + "월 " + date.day.toString() + "일"

            if (dateView.text == this.date && state == "On" && que.isNullOrBlank() == false) {
                questionTextView.text = que
                if (ans == null) {
                    answerTextView.visibility = View.GONE
                } else {
                    answerTextView.text = ans
                }

                if (pic != null) {
                    try {
                        pic_uri = Uri.parse(pic)
                        val inputStream: InputStream? = activity?.contentResolver?.openInputStream(
                                pic_uri
                        )
                        val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
                        imageView.setImageBitmap(bitmap)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        cursor.close()
        sqlDB.close()
    }

}