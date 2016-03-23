package com.xyz.calendarview;

import android.app.Activity;
import android.os.Bundle;

import com.callmexyz.calendarview.CalendarView;
import com.callmexyz.calendarview.dayclicklistener.CilcleColor;

/**
 * Created by CallMeXYZ on 2016/3/23.
 */
public class TestActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        CalendarView calendarView = (CalendarView) findViewById(R.id.calendar_view);
        calendarView.setDayClickListener(new CilcleColor());
    }
}
