package com.xyz.calendarview;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.view.View;
import android.widget.TextView;

import com.callmexyz.calendarview.CalendarView;
import com.callmexyz.calendarview.DayView;
import com.callmexyz.calendarview.dayclicklistener.CircleColor;
import com.callmexyz.calendarview.styles.MonthViewStyle;

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
        findViewById(R.id.latter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.navToNext();
            }
        });
        findViewById(R.id.former).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.navToFormer();
            }
        });


    /*
       example for selectDay using selectDay(Calendar) when initiating
      or u can just call selectDayAtInit(Calendar);
    *//* Thread t = new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Calendar c = Calendar.getInstance();
                        c.add(Calendar.MONTH,1);
                        c.add(Calendar.DAY_OF_YEAR, -2);
                        calendarView.selectDay(c);
                    }
                });
            }
        };
        t.start();*/

        calendarView.setFirstDayOfWeek(Calendar.FRIDAY);
        calendarView.selectDayAtInit(Calendar.getInstance());
        // u may need to set the time title manually since initiating won't call onMonthSelected
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        tv.setText(sdf.format(date));
        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                calendarView.startCollapse();
                MonthViewStyle w = calendarView.getMonthViewStyle();
                if (w.getMonthType() == MonthViewStyle.MonthType.MONTH_VIEW)
                    w.setMonthType(MonthViewStyle.MonthType.WEEK_VIEW);
                else w.setMonthType(MonthViewStyle.MonthType.MONTH_VIEW);
                calendarView.setMonthViewStyle(w);
            }

        });
    }

    class MyDayClickListener extends CircleColor {
        public MyDayClickListener(@ColorInt int color) {
            super(color);
        }

        @Override
        public void onDayClick(DayView view, Calendar c, boolean ifRestoring) {
            super.onDayClick(view, c, ifRestoring);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (!ifRestoring) {
                tv.setText(sdf.format(new Date(calendarView.getSelectedCalendar().getTimeInMillis())));
            }
        }

        @Override
        public void onDayUnClick(DayView view, Calendar c) {
            super.onDayUnClick(view, c);
        }
    }
}
