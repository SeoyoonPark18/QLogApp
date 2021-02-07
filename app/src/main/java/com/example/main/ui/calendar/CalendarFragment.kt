package com.example.main.ui.calendar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.ImageButton
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.main.NextCalActivity
import com.example.main.R
import java.util.*

class CalendarFragment : Fragment() {

    private lateinit var calendarViewModel: CalendarViewModel
    lateinit var expansionButton: ImageButton
    lateinit var scrollText: ScrollView
    lateinit var YearMonth: TextView
    //lateinit var calendarView: CalendarView
    lateinit var dateView: TextView

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        expansionButton = view.findViewById(R.id.expansionButton)
        scrollText = view.findViewById(R.id.scrollText)
        YearMonth = view.findViewById(R.id.YearMonth)
        //calendarView = view.findViewById(R.id.calendarView)
        dateView = view.findViewById(R.id.dateView)

        YearMonth.text = String.format("%d년 %d월", Calendar.YEAR+2020, Calendar.MONTH)
        dateView.text = String.format("%d년 %d월 %d일", Calendar.YEAR+2020, Calendar.MONTH, Calendar.DATE+2)

        expansionButton.setOnClickListener{
            val intent = Intent(getActivity(), NextCalActivity::class.java)
            intent.putExtra("KEY_DATE", dateView.text)
            startActivity(intent)
        }

        /*calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            dateView.visibility = View.VISIBLE
            dateView.text = String.format("%d년 %d월 %d일", year, month+1, dayOfMonth)
            //데이터 베이스
        }*/
    }
}
