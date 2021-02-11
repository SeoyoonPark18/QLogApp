package com.example.main.ui.calendar

import android.graphics.Color
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import java.util.*

class EventDecorator : DayViewDecorator {
    private var color: Int = Color.BLACK
    private val dates: List<CalendarDay>
    constructor(color: Int, dates: List<CalendarDay>){
        this.color = color
        this.dates = dates
    }
    override fun shouldDecorate(day: CalendarDay): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(DotSpan(5F, color))
    }
}