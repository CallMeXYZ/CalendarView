package com.callmexyz.calendarview.dayclicklistener;

import com.callmexyz.calendarview.CalendarView;
import com.callmexyz.calendarview.DayView;
import com.callmexyz.calendarview.R;

import java.util.Calendar;

/**
 * Created by CallMeXYZ on 2016/3/23.
 */
public class CilcleColor implements CalendarView.DayClickListener {

    @Override
    public void onDayClick(DayView view, Calendar c) {
        view.setBackgroundResource(R.drawable.blue_circle);
    }

    @Override
    public void onDayUnClick(DayView view, Calendar c) {
        view.setBackgroundDrawable(null);
    }
}
