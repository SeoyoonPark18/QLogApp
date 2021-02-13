package com.example.main.ui.calendar


import android.content.ContentUris
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.main.DBManager2
import com.example.main.NextCalActivity
import com.example.main.R

private const val REQUEST_MSG_CODE = 1

open class CalendarFragment : Fragment() {

    private lateinit var calendarViewModel: CalendarViewModel
    lateinit var layout: LinearLayout

    lateinit var sqlDB: SQLiteDatabase
    lateinit var sqliteDB: SQLiteDatabase
    lateinit var DBManager2: DBManager2


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        calendarViewModel =
                ViewModelProvider(this).get(CalendarViewModel::class.java)
        val root =  inflater.inflate(R.layout.fragment_calendar, container, false)
        layout = root.findViewById(R.id.dateText)
        showDate()
        return root
    }

    //textview와 imageview를 동적할당하여 데이터베이스에 있는 일자를 표시
    fun showDate() {
        DBManager2 = DBManager2(activity, "list", null, 1)
        sqlDB = DBManager2.readableDatabase

        var cursor: Cursor
        cursor = sqlDB.rawQuery("SELECT * FROM list WHERE logonoff =='On';", null)
        var num: Int = 0

        var date = ""
        var pic: ByteArray = byteArrayOf()

        while (cursor.moveToNext()) { //커서로 이동하면서 date날짜를 가져와 textview에 출력
            date = cursor.getString(cursor.getColumnIndex("date"))

            var layout_item: LinearLayout = LinearLayout(activity)
            layout_item.orientation = LinearLayout.HORIZONTAL
            layout_item.id = num

            var tvTextView: TextView = TextView(activity)
            tvTextView.text = date
            tvTextView.textSize = 25F
            if (num % 2 == 0) tvTextView.setBackgroundColor(Color.rgb(246, 189, 197))
            tvTextView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            tvTextView.setPadding(40, 30, 0, 30)
            tvTextView.gravity = LEFT

            layout_item.addView(tvTextView)

            layout_item.setOnClickListener {
                sqliteDB = DBManager2.readableDatabase
                var recursor = sqliteDB.rawQuery("SELECT * FROM list WHERE date == '${tvTextView.text}'",null)
                var ques = ""
                var ans = ""
                var emotion = ""
                while (recursor.moveToNext()){
                    ques = recursor.getString(cursor.getColumnIndex("ques"))
                    ans = recursor.getString(cursor.getColumnIndex("ans"))
                    emotion = recursor.getString(cursor.getColumnIndex("emotion"))
                    if (recursor.getString(cursor.getColumnIndex("pic"))!=null)
                        pic = recursor.getBlob(cursor.getColumnIndex("pic"))
                }
                val intent = Intent(getActivity(), NextCalActivity::class.java)
                intent.putExtra("date", tvTextView.text)
                intent.putExtra("ques", ques)
                intent.putExtra("ans", ans)
                intent.putExtra("emotion", emotion)
                intent.putExtra("pic", pic)
                recursor.close()
                sqliteDB.close()
                startActivity(intent)
                }
            layout.addView(layout_item)
            num++
        }
        cursor.close()
        sqlDB.close()

    }
}