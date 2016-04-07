package com.xyz.calendarview;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.callmexyz.calendarview.CalendarView;
import com.callmexyz.calendarview.DayView;
import com.callmexyz.calendarview.MapDayEvent;
import com.callmexyz.calendarview.dayclicklistener.CircleColor;
import com.callmexyz.calendarview.interfaces.DayEventHandler;
import com.callmexyz.calendarview.interfaces.EventProvider;
import com.callmexyz.calendarview.interfaces.PageSelectedListener;
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

        calendarView.setMonthSelectedListener(new PageSelectedListener() {
            @Override
            public void onPageSelected(Calendar c) {
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


        //  example for selectDay using selectDay(Calendar) when initiating
        // or u can just call selectDayAtInit(Calendar);
   /* Thread t = new Thread(){
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
        calendarView.setMonthEventProvider(new EventProvider() {
            @Override
            public MapDayEvent getMonthDayEvents(Calendar start, Calendar end) {
                MapDayEvent map = new MapDayEvent();
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR, 10);
                map.putEvent(calendar, 1);
                calendar.add(Calendar.DAY_OF_YEAR, -17);
                map.putEvent(calendar, 2);
                return map;
            }
        });
        calendarView.setDayEventProvider(new DayEventHandler() {
            @Override
            public void handleDayEvent(DayView dayView, boolean ifSameMonth, int eventNum) {
                if (eventNum > 0) {
                    TextView textView = new TextView(dayView.getContext());
                    textView.setText(eventNum + "");
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    if (ifSameMonth)
                        textView.setTextColor(getResources().getColor(android.R.color.holo_blue_light));
                    dayView.addView(textView, layoutParams);
                }
            }
        });
        calendarView.selectDayAtInit(Calendar.getInstance());
        // u may need to set the time title manually since initiating won't call onMonthSelected
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        tv.setText(sdf.format(date));
        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            String s = sdf.format(new Date(calendarView.getSelectedCalendar().getTimeInMillis()));
            tv.setText(s);
            if (!ifRestoring) Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onDayUnClick(DayView view, Calendar c) {
            super.onDayUnClick(view, c);
        }
    }

}
