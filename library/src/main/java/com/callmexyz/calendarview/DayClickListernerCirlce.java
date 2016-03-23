package com.callmexyz.calendarview;

import java.util.Calendar;

/**
 * Created by CallMeXYZ on 2016/3/23.
 */
public interface DayClickListernerCirlce extends CalendarView.DayClickListener {
    @Override
    void onDayClick(DayView view, Calendar c);
}
