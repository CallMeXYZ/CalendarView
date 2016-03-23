package com.callmexyz.calendarview;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by CallMeXYZ on 2016/3/22.
 */
public class DayView extends TextView {

    private Calendar mCalendar;

    public DayView(Context context, Calendar calendar) {
        super(context);
        init(calendar);

    }

    private void init(Calendar calendar) {
        mCalendar = calendar;
        setGravity(Gravity.CENTER);
        setText(mCalendar.get(Calendar.DAY_OF_MONTH)+"");

    }

    public Calendar getCalendar() {
        return mCalendar;
    }
}
