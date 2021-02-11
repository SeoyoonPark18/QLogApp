package com.example.main.ui.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat.PNG
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.main.NextCalActivity
import com.example.main.R
import com.example.main.dateDBManager
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import org.apache.commons.io.IOUtils
import java.io.*
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

    lateinit var date: String
    lateinit var id: String
    lateinit var dateId: String

    lateinit var sqlDB: SQLiteDatabase
    lateinit var datesql: SQLiteDatabase
    lateinit var dateDBManager: dateDBManager

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

        sqlDB = SQLiteDatabase.openDatabase(
                "/data/data/com.example.main/databases/list",
                null, SQLiteDatabase.OPEN_READONLY
        )

        var writeDay: ArrayList<CalendarDay> = ArrayList()

        var state = ""
        var que = ""
        var ans = ""
        var emo = ""
        var pic = ""
        var pic_uri: Uri
        var year = 0
        var month = 0
        var day  = 0

        var cursor: Cursor = sqlDB.rawQuery("SELECT * FROM list;", null)
        while (cursor.moveToNext()){
            id = cursor.getString(cursor.getColumnIndex("id"))
            que = cursor.getString(cursor.getColumnIndex("ques"))
            ans = cursor.getString(cursor.getColumnIndex("ans"))
            date = cursor.getString(cursor.getColumnIndex("date"))
            state = cursor.getString(cursor.getColumnIndex("logonoff"))
            emo = cursor.getString(cursor.getColumnIndex("emotion"))
            pic = cursor.getString(cursor.getColumnIndex("pic"))
        }

        if (calendarView.isSelected == true){
            if (dateView.text == date && state == "On") {
                questionTextView.text = que
                if (ans == null) {
                    answerTextView.visibility = View.GONE
                } else {
                    answerTextView.text = ans
                }

                if (pic != null) {
                    pic_uri = Uri.parse(pic)
                    getFilePathFromURI(this.requireContext(), pic_uri)
                    imageView.setImageURI(pic_uri)
                }
            }
        }

        //일기를 쓴 날짜 가져오기
        dateDBManager = dateDBManager(activity, "dateDB", null, 1)
        datesql = dateDBManager.readableDatabase

        var datecursor: Cursor = datesql.rawQuery("SELECT * FROM dateDB", null)
        while (datecursor.moveToNext()){
            dateId = datecursor.getString(datecursor.getColumnIndex("id"))
            year = datecursor.getInt(datecursor.getColumnIndex("year"))
            month = datecursor.getInt(datecursor.getColumnIndex("month"))
            day = datecursor.getInt(datecursor.getColumnIndex("day"))
            if(year != null && (month<=12 && month >=1) && (day >= 1&& day <= 31)){
                writeDay.add(CalendarDay.from(year, month, day))
            }
        }
        //달력에 쓴 날짜 표시
        calendarView.addDecorator(EventDecorator(Color.BLACK, writeDay))

        //NextCalActivity로 이미지를 넘기기
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
            intent.putExtra("ID", id)
            intent.putExtra("DATEID", dateId)
            startActivity(intent)
        }

        //다른 날짜를 눌렀을 때
        calendarView.setOnDateChangedListener{ widget, date, selected ->
            val bitmap: Bitmap
            dateView.text = date.year.toString() + "년 " + date.month.toString() + "월 " + date.day.toString() + "일"

            if (dateView.text == this.date && state == "On") {
                questionTextView.text = que
                if (ans == null) {
                    answerTextView.visibility = View.GONE
                } else {
                    answerTextView.text = ans
                }

                if (pic != null) {

                } else {
                    imageView.visibility = View.GONE
                }
            }
        }
        cursor.close()
        datecursor.close()
        sqlDB.close()
        datesql.close()
    }
    open fun getFilePathFromURI(context: Context, contentUri: Uri?): String? {
        val fileName = getFileName(contentUri)
        if (!TextUtils.isEmpty(fileName)) {
            val copyFile= File("context.getFilesDir() + File.separator + fileName")
            copy(context, contentUri, copyFile)
            return copyFile.getAbsolutePath()
        }
        return null
    }

    open fun getFileName(uri: Uri?): String? {
        if (uri == null) return null
        var fileName: String? = null
        val path = uri.path
        val cut = path!!.lastIndexOf('/')
        if (cut != -1) {
            fileName = path!!.substring(cut + 1)
        }
        return fileName
    }

    open fun copy(context: Context, srcUri: Uri?, dstFile: File?) {
        try {
            val inputStream = context.contentResolver.openInputStream(srcUri!!) ?: return
            val outputStream: OutputStream = FileOutputStream(dstFile)
            IOUtils.copy(inputStream, outputStream)
            inputStream.close()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}