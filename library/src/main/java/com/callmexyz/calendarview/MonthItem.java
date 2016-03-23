package com.callmexyz.calendarview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

/**
 * Created by CallMeXYZ on 2016/3/22.
 */
public class MonthItem extends ViewGroup {

    private Calendar mFisrtDay;
    private CalendarView mCalendarView;

    public MonthItem(Context context, Calendar monthFirstDay, CalendarView calendarView) {
        super(context);
        mCalendarView = calendarView;
        mFisrtDay = monthFirstDay;
        Calendar viewStart = Utils.getMonthViewStart(mFisrtDay, Calendar.SUNDAY);
        for (int i = 0; i < Utils.MONTH_SIZE; i++) {
            DayView dayView = new DayView(context, viewStart);
            dayView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCalendarView.handleDayClick((DayView) v);
                }
            });
            addView(dayView);
            viewStart.add(Calendar.DAY_OF_YEAR, 1);
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        setMeasuredDimension(widthSize, heightSize);
        int childWidth = MeasureSpec.makeMeasureSpec(widthSize / Utils.WEEK_SIZE, MeasureSpec.EXACTLY);
        for (int i = 0; i < getChildCount(); i++) {

            getChildAt(i).measure(childWidth, childWidth);
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = 0;
        int top = 0;
        int width = 0;
        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (i == 0) {
                width = view.getMeasuredWidth();
                height = view.getMeasuredHeight();
            }
            if (i != 0 && i % Utils.WEEK_SIZE == 0) {
                top += height;
                left = 0;
            }
            view.layout(left, top, left + height, top + width);
            left += width;
        }
    }
}
