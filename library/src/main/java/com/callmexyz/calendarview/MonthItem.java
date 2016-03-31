package com.callmexyz.calendarview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

/**
 * Created by CallMeXYZ on 2016/3/22.
 */
public class MonthItem extends ViewGroup {

    // the first day of month
    private Calendar mFirstDay;
    // the first day of MontItem
    private Calendar mMonthStartDay;
    private CalendarView mCalendarView;

    public MonthItem(Context context, Calendar monthFirstDay, CalendarView calendarView) {
        super(context);
        mCalendarView = calendarView;
        mFirstDay = monthFirstDay;
        initViews();
    }

    private void initViews() {
        mMonthStartDay = Utils.getMonthViewStart(mFirstDay, mCalendarView.getFirstDayOfWeek());
        Calendar viewStart = (Calendar) mMonthStartDay.clone();
        for (int i = 0; i < Utils.MONTH_VIEW_DAY_SIZE; i++) {
            DayView dayView = new DayView(getContext(), (Calendar) viewStart.clone(), mFirstDay, mCalendarView.getDayViewStyle());
            if (Utils.ifSameMonth(viewStart, mFirstDay))
                dayView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCalendarView.handleDayClick((DayView) v);
                    }
                });
                // TODO: 2016/3/31  outMonthDay Click event
            else if ((mCalendarView.getDayViewStyle().isOutMonthVisible() && mCalendarView.getDayViewStyle().isOutMonthClickable())) {

            }
            //here restore day click state
            // TODO: 2016/3/24 in the future, dayview tag will be introduced. be careful of the tag state when restoring
            if (null != mCalendarView.getSelectedCalendar() && Utils.ifSameDay(viewStart, mCalendarView.getSelectedCalendar()) && Utils.ifSameMonth(viewStart, mFirstDay) && null != mCalendarView.getDayClickListener())
                mCalendarView.getDayClickListener().onDayClick(dayView, (Calendar) dayView.getDate().clone(), true);
            addView(dayView);
            viewStart.add(Calendar.DAY_OF_YEAR, 1);
        }
    }
    public void refreshUI() {
        removeAllViews();
        initViews();
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

    public DayView getChildAt(Calendar c) {
        int x = mMonthStartDay.get(Calendar.DAY_OF_MONTH);
        int m = mMonthStartDay.get(Calendar.MONTH);
        int j = mFirstDay.get(Calendar.DAY_OF_MONTH);
        int n = mFirstDay.get(Calendar.MONTH);
        int s = c.get(Calendar.DAY_OF_MONTH);
        int sss = c.get(Calendar.MONTH);
        long diff = Utils.getDayDifference(mMonthStartDay, c);
        if (diff > Utils.MONTH_VIEW_DAY_SIZE) return null;
        return (DayView) getChildAt((int) diff - 1);
    }

    public Calendar getFirstDay() {
        return mFirstDay;
    }

    public Calendar getMonthStartDay() {
        return mMonthStartDay;
    }
}
