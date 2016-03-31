package com.xyz.calendarview;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.widget.TextView;
import android.widget.Toast;

import com.callmexyz.calendarview.CalendarView;
import com.callmexyz.calendarview.DayView;
import com.callmexyz.calendarview.dayclicklistener.CilcleColor;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by CallMeXYZ on 2016/3/23.
 */
public class TestActivity extends Activity {
    CalendarView calendarView;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        calendarView = (CalendarView) findViewById(R.id.calendar_view);
        calendarView.setDayClickListener(new MyDayClickListener(getResources().getColor(android.R.color.holo_blue_light)));

        tv = (TextView) findViewById(R.id.time);

        calendarView.setMonthSelectedListener(new CalendarView.MonthSelectedListener() {
            @Override
            public void onMonthSelected(Calendar c) {
                Date date = new Date(c.getTimeInMillis());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                tv.setText(sdf.format(date));
            }
        });
    }

    class MyDayClickListener extends CilcleColor {
        public MyDayClickListener(@ColorInt int color) {
            super(color);
        }

        @Override
        public void onDayClick(DayView view, Calendar c, boolean ifRestoring) {
            super.onDayClick(view, c, ifRestoring);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (!ifRestoring)
                Toast.makeText(getApplicationContext(), sdf.format(new Date(calendarView.getSelectedCalendar().getTimeInMillis())), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onDayUnClick(DayView view, Calendar c) {
            super.onDayUnClick(view, c);
        }
    }
}
