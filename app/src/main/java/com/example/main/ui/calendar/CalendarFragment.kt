package com.example.main.ui.calendar

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat.PNG
import android.graphics.BitmapFactory
import android.graphics.Color
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
import com.example.main.DBManager2
import com.example.main.NextCalActivity
import com.example.main.R
import com.example.main.dateDBManager
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import java.io.*
import kotlin.collections.ArrayList

private const val REQUEST_MSG_CODE = 1

open class CalendarFragment : Fragment() {

    private lateinit var calendarViewModel: CalendarViewModel
    lateinit var expansionButton: ImageButton
    lateinit var scrollText: ScrollView
    lateinit var calendarView: MaterialCalendarView
    lateinit var dateView: TextView
    lateinit var questionTextView: TextView
    lateinit var answerTextView: TextView
    lateinit var imageView: ImageView

    lateinit var id: String
    lateinit var dateId: String
    lateinit var date: String
    lateinit var writeDay: ArrayList<CalendarDay>

    lateinit var sqlDB: SQLiteDatabase
    lateinit var datesql: SQLiteDatabase
    lateinit var dateDBManager: dateDBManager
    lateinit var DBManager2: DBManager2

    fun setData() {
        var state = ""
        var que = ""
        var ans = ""
        var pic: ByteArray = byteArrayOf()
        var picBit: Bitmap

        DBManager2 = DBManager2(activity, "list", null, 1)
        sqlDB = DBManager2.writableDatabase

        var cursor: Cursor = sqlDB.rawQuery("SELECT * FROM list WHERE date='${dateView.text}';", null)
        var row = cursor.count
        for (i in 0..row) {
            if (cursor.moveToNext()) {
                que = cursor.getString(cursor.getColumnIndex("ques"))
                ans = cursor.getString(cursor.getColumnIndex("ans"))
                date = cursor.getString(cursor.getColumnIndex("date"))
                state = cursor.getString(cursor.getColumnIndex("logonoff"))
                pic = cursor.getBlob(cursor.getColumnIndex("pic"))
            }
            if (dateView.text == date && state == "On") break
        }

        if (dateView.text == date && state == "On") {
            questionTextView.text = que

            if (ans.isNullOrBlank()) {
                answerTextView.visibility = View.GONE
            } else {
                answerTextView.text = ans
            }

            if (pic.none()) {
                imageView.visibility = View.GONE
            } else {
                picBit = BitmapFactory.decodeByteArray(pic, 0, pic.size)
                imageView.setImageBitmap(picBit)
            }
        } else if (date != dateView.text) {
            questionTextView.visibility = View.GONE
            questionTextView.text = ""
            answerTextView.visibility = View.GONE
            answerTextView.text = ""
            imageView.visibility = View.GONE
        }
        cursor.close()
        sqlDB.close()
    }

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

        var year = 0
        var month = 0
        var day = 0

        var emotion = ""
        var emo = ""

        dateView.text = CalendarDay.today().year.toString() + "년 " +
                CalendarDay.today().month + "월 " +
                CalendarDay.today().day + "일"

        setData()//스크롤뷰 부분에 텍스트뷰와 이미지뷰 값을 설정

        writeDay = ArrayList()

        //일기를 쓴 날짜 가져오기
        dateDBManager = dateDBManager(activity, "dateDB", null, 1)
        datesql = dateDBManager.readableDatabase

        var datecursor: Cursor = datesql.rawQuery("SELECT * FROM dateDB", null)
        while (datecursor.moveToNext()) {
            dateId = datecursor.getString(datecursor.getColumnIndex("id"))
            year = datecursor.getInt(datecursor.getColumnIndex("year"))
            month = datecursor.getInt(datecursor.getColumnIndex("month"))
            day = datecursor.getInt(datecursor.getColumnIndex("day"))
            if (year != null && (month <= 12 && month >= 1) && (day >= 1 && day <= 31)) {
                writeDay.add(CalendarDay.from(year, month, day))
            }
        }

        DBManager2 = DBManager2(activity, "list", null, 1)
        sqlDB = DBManager2.readableDatabase
        var cursor: Cursor = sqlDB.rawQuery("SELECT * FROM list WHERE date='${dateView.text}';", null)
        while (cursor.moveToNext()) {
            id = cursor.getString(cursor.getColumnIndex("id"))
            emo = cursor.getString(cursor.getColumnIndex("emotion"))
        }

        emotion = emo // 감정 상태

        //일기 쓴 날짜를 달력에 표시
        calendarView.addDecorator(EventDecorator(Color.BLACK, writeDay))

        datecursor.close()
        datesql.close()

        //NextCalActivity로 이미지를 넘기기
        val stream = ByteArrayOutputStream()
        if (imageView.drawable != null) {
            val bitmap: Bitmap = imageView.drawable.toBitmap(300, 300)
            val scale: Float = 700 / bitmap.width.toFloat()
            val image_w: Int = (bitmap.width * scale).toInt()
            val image_h: Int = (bitmap.height * scale).toInt()
            val resize: Bitmap = Bitmap.createScaledBitmap(bitmap, image_w, image_h, true)
            resize.compress(PNG, 20, stream)
        } else {
            imageView.visibility = View.GONE
        }
        val byteArray: ByteArray = stream.toByteArray()

        //확대버튼
        expansionButton.setOnClickListener {
            val intent = Intent(activity, NextCalActivity::class.java)
            intent.putExtra("KEY_DATE", dateView.text)
            intent.putExtra("KEY_QUESTION", questionTextView.text)
            intent.putExtra("KEY_ANSWER", answerTextView.text)
            intent.putExtra("KEY_IMAGE", byteArray)
            intent.putExtra("KEY_EMO", emotion)
            intent.putExtra("DATE", date)
            intent.putExtra("ID", id)
            intent.putExtra("DATEID", dateId)
            activity?.startActivityForResult(intent, REQUEST_MSG_CODE)
        }

        //다른 날짜를 눌렀을 때
        calendarView.setOnDateChangedListener(OnDateSelectedListener { widget, date, selected ->

        })

    }


    //nextcalactivity에서 intent값을 받기 위해 오버라이딩
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_MSG_CODE) {
            if (resultCode == RESULT_OK) {
                var que = data?.getStringExtra("que")
                var ans = data?.getStringExtra("ans")
                var pic = data?.getStringExtra("pho")
                var emo = data?.getStringExtra("emo")
                var date = data?.getStringExtra("date")
                var Date = data?.getStringExtra("Date")
                var year = data?.getStringExtra("year")?.toInt()
                var month = data?.getStringExtra("month")?.toInt()
                var day = data?.getStringExtra("day")?.toInt()
                writeDay.removeAll(listOf(CalendarDay.from(year!!, month!!, day!!)))

                DBManager2 = DBManager2(activity, "list",  null, 1)
                sqlDB = DBManager2.writableDatabase

                sqlDB.rawQuery(que, null)
                sqlDB.rawQuery(ans, null)
                sqlDB.rawQuery(pic, null)
                sqlDB.rawQuery(emo, null)
                sqlDB.rawQuery(date, null)

                sqlDB.close()

                dateDBManager = dateDBManager(activity, "dateDB", null, 1)
                datesql = dateDBManager.writableDatabase

                datesql.rawQuery("$Date", null)

                datesql.close()

                questionTextView.text = ""
                questionTextView.visibility = View.GONE
                answerTextView.text = ""
                answerTextView.visibility = View.GONE

                imageView.setImageDrawable(null)
                imageView.visibility = View.GONE
            }
        }
    }
}