package com.callmexyz.calendarview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.callmexyz.calendarview.styles.MonthViewStyle;

import java.util.Calendar;

/**
 * Created by CallMeXYZ on 2016/3/22.
 */
public class PageItem extends ViewGroup {

// TODO: 2016/4/1  reset first day mechanism
    /**
     * the first day the month in month view or the first day add several weeks in week view
     */
    private Calendar mFirstDay;
    // the first day of MontItem
    private Calendar mMonthStartDay;
    private CalendarView mCalendarView;
    private MapDayEvent mMonthDayEvents = new MapDayEvent();

    public PageItem(Context context, Calendar monthFirstDay, CalendarView calendarView) {
        super(context);
        mCalendarView = calendarView;
        mFirstDay = monthFirstDay;
        initViews();
    }

    private void initViews() {
        mMonthStartDay = Utils.getMonthViewStart(mFirstDay, mCalendarView.getFirstDayOfWeek());

        Calendar end = (Calendar) mMonthStartDay.clone();
        if (MonthViewStyle.MonthType.MONTH_VIEW == mCalendarView.getMonthViewStyle().getMonthType())
            end.add(Calendar.DAY_OF_YEAR, Utils.MONTH_VIEW_DAY_SIZE);
        else end.add(Calendar.DAY_OF_YEAR, Utils.WEEK_SIZE);
        if (null != mCalendarView.getMonthEventProvider())
            mMonthDayEvents = mCalendarView.getMonthEventProvider().getMonthDayEvents((Calendar) mMonthStartDay.clone(), end);
        final Calendar viewStart = (Calendar) mMonthStartDay.clone();
        int size = 0;
        switch (mCalendarView.getMonthViewStyle().getMonthType()) {
            case WEEK_VIEW:
                size = Utils.WEEK_SIZE;
                break;
            default:
                size = Utils.MONTH_VIEW_DAY_SIZE;
                break;
        }
        for (int i = 0; i < size; i++) {
            Integer eventNum = mMonthDayEvents.getEventNum(viewStart);
            final boolean ifSameMonth = Utils.ifSameMonth(viewStart, mFirstDay);
            final DayView dayView = new DayView(getContext(), (Calendar) viewStart.clone(), mFirstDay, mCalendarView.getDayViewStyle(), mCalendarView.getMonthViewStyle(), ifSameMonth, null!=eventNum?eventNum:0, mCalendarView.getDayEventProvider());
            dayView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //when in week view mode, there is no so called in month or out month
                    if (MonthViewStyle.MonthType.WEEK_VIEW == mCalendarView.getMonthViewStyle().getMonthType() || ifSameMonth)
                        mCalendarView.handleDayClick((DayView) v);
                    else if ((mCalendarView.getDayViewStyle().isOutMonthVisible() && mCalendarView.getDayViewStyle().isOutMonthClickable())) {
                        mCalendarView.selectDay(dayView.getDate());
                    }
                }


            });
            //here restore day click state
            // TODO: 2016/3/24 in the future, dayview tag will be introduced. be careful of the tag state when restoring
            if (null != mCalendarView.getSelectedCalendar() && Utils.ifSameDay(viewStart, mCalendarView.getSelectedCalendar()) && (MonthViewStyle.MonthType.WEEK_VIEW == mCalendarView.getMonthViewStyle().getMonthType() || ifSameMonth) && null != mCalendarView.getDayClickListener())
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
        long diff = Utils.getDayDifference(mMonthStartDay, c);
        if ((MonthViewStyle.MonthType.MONTH_VIEW == mCalendarView.getMonthViewStyle().getMonthType() && diff > Utils.MONTH_VIEW_DAY_SIZE)
                || (MonthViewStyle.MonthType.WEEK_VIEW == mCalendarView.getMonthViewStyle().getMonthType() && diff > Utils.WEEK_SIZE))
            return null;
        return (DayView) getChildAt((int) diff);
    }

    public Calendar getFirstDay() {
        return mFirstDay;
    }

    public Calendar getMonthStartDay() {
        return mMonthStartDay;
    }
}
