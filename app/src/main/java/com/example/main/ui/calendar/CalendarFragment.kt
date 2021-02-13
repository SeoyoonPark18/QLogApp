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
        var pic: ByteArray = byteArrayOf(0)

        while (cursor.moveToNext()) { //커서로 이동하면서 date날짜를 가져와 textview에 출력
            date = cursor.getString(cursor.getColumnIndex("date"))

            var layout_item: LinearLayout = LinearLayout(activity)
            layout_item.orientation = LinearLayout.HORIZONTAL
            layout_item.id = num

            var tvTextView: TextView = TextView(activity)
            if (date != "null") { //작성한 것이 없다면 모아보기 부분에 표시되지 않음
                tvTextView.text = date
            }
            tvTextView.textSize = 25F
            tvTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_edit_24, 0, R.drawable.ic_baseline_keyboard_arrow_right_24, 0)
            tvTextView.compoundDrawablePadding = 30
            if (num % 2 == 0) tvTextView.setBackgroundColor(Color.rgb(246, 189, 197))
            tvTextView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            tvTextView.setPadding(40, 30, 0, 30)
            tvTextView.gravity = LEFT

            //텍스트뷰의 텍스트가 비어있거나 없다면 layout에 추가하지 않음
            if (tvTextView.text.isNullOrEmpty() == false) layout_item.addView(tvTextView)

            //레이아웃에 있는 텍스트뷰를 눌렀을 때 해당 날짜의 데이터를 다음화면으로 넘김
            layout_item.setOnClickListener {
                sqliteDB = DBManager2.readableDatabase
                var recursor = sqliteDB.rawQuery("SELECT * FROM list WHERE date == '${tvTextView.text}'", null)
                var ques = ""
                var ans = ""
                var emotion = ""
                while (recursor.moveToNext()) {
                    ques = recursor.getString(cursor.getColumnIndex("ques"))
                    ans = recursor.getString(cursor.getColumnIndex("ans"))
                    emotion = recursor.getString(cursor.getColumnIndex("emotion"))
                    if (recursor.getBlob(cursor.getColumnIndex("pic")).isNotEmpty())
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