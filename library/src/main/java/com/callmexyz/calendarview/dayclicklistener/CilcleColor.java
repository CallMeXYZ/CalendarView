package com.callmexyz.calendarview.dayclicklistener;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;

import com.callmexyz.calendarview.CalendarView;
import com.callmexyz.calendarview.DayView;
import com.callmexyz.calendarview.R;

import java.util.Calendar;

/**
 * Created by CallMeXYZ on 2016/3/23.
 */
public class CilcleColor implements CalendarView.DayClickListener {
    int mColor;

    public CilcleColor(@ColorInt int color) {
        mColor = color;
    }

    @Override
    public void onDayClick(DayView view, Calendar c,boolean ifRestoring) {
        Drawable d = view.getResources().getDrawable(R.drawable.blue_circle);
        d.setColorFilter(mColor, PorterDuff.Mode.SRC_ATOP);
        view.setBackgroundDrawable(d);
    }

    @Override
    public void onDayUnClick(DayView view, Calendar c) {
        view.setBackgroundColor(view.getBgColor());
    }
}
