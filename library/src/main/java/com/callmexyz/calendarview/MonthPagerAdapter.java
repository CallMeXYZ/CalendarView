package com.callmexyz.calendarview;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.callmexyz.calendarview.styles.MonthViewStyle;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by CallMeXYZ on 2016/3/22.
 */
public class MonthPagerAdapter extends PagerAdapter {
    private final String TAG = MonthPagerAdapter.class.getSimpleName();
    private ArrayList<MonthItem> mViewList;
    private Calendar mRangeStart;
    private Calendar mRangeEnd;
    private CalendarView mCalendarView;

    public MonthPagerAdapter(CalendarView calendarView) {
        mCalendarView = calendarView;
        mRangeStart = Utils.getRangeStart();
        mRangeEnd = Utils.getRangeEnd();
        mViewList = new ArrayList<>();
    }

    /**
     * whne start or end is null, the relating rang will be set to default range(200 year former or latter) by the current time
     *
     * @param start
     * @param end
     */
    public void setRange(Calendar start, Calendar end) {
        if (null != start && null != end && end.before(start)) {
            end = (Calendar) start.clone();
            Log.w(TAG, "the range end is former than start u kidding me ");
        }
        if (null != start) mRangeStart = Utils.getRange(start.getTimeInMillis());
        // TODO: 2016/4/1
        if (null != end) mRangeEnd = Utils.getRange(end.getTimeInMillis());
        notifyDataSetChanged();
    }

    public void refreshUI() {
        mViewList.clear();
       notifyDataSetChanged();
    }

    public boolean checkCalendar(Calendar c) {
        return c.before(mRangeEnd) && c.after(mRangeStart);

    }

    @Override
    public int getCount() {
        return getCount(mRangeStart, mRangeEnd);
    }

    // TODO: 2016/4/1 quiete complicated here to get the exact range of differen MonthStyle especially for WeekView 
    private int getCount(Calendar start, Calendar end) {
        if (end.before(start)) {
            Log.w(TAG, "range end is before start");
            return 0;
        }
        if (MonthViewStyle.MonthType.MONTH_VIEW == mCalendarView.getMonthViewStyle().getMonthType())
            return Utils.getMonthDiff(start, end) + 1;
        return Utils.getWeekDiff(start, end,mCalendarView.getFirstDayOfWeek()) + 1;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        MonthItem monthItem = new MonthItem(container.getContext(), getItemCalendar(position), mCalendarView);
        container.addView(monthItem);
        mViewList.add(monthItem);
        return monthItem;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        MonthItem monthItem = ((MonthItem) object);
        container.removeView(monthItem);
        mViewList.remove(monthItem);

    }

    public Calendar getItemCalendar(int position) {
        Calendar c = (Calendar) mRangeStart.clone();
        if (MonthViewStyle.MonthType.MONTH_VIEW == mCalendarView.getMonthViewStyle().getMonthType())
            c.add(Calendar.MONTH, position);
        else c.add(Calendar.WEEK_OF_YEAR, position);
        return c;
    }

    public int getPosition(Calendar c) {
        int i = 0;
        if (MonthViewStyle.MonthType.MONTH_VIEW == mCalendarView.getMonthViewStyle().getMonthType())
            i = Utils.getMonthDiff(mRangeStart, c);
        else i = Utils.getWeekDiff(mRangeStart, c,mCalendarView.getFirstDayOfWeek());
        if (i < 0 || i > getCount() - 1) {
            Log.w(TAG, "the given calendar is out of valid range");
        }
        return i < 0 ? 0 : (i > getCount() ? getCount() : i);
    }

    /**
     * @param c
     * @return null if the day view is out of range or not in the cached MonthItems
     */
    public DayView getDayView(Calendar c) {
        for (int i = 0; i < mViewList.size(); i++) {
            if ((MonthViewStyle.MonthType.MONTH_VIEW == mCalendarView.getMonthViewStyle().getMonthType()
                    && Utils.ifSameMonth(mViewList.get(i).getFirstDay(), c))
                    || (MonthViewStyle.MonthType.WEEK_VIEW == mCalendarView.getMonthViewStyle().getMonthType()
                    && Utils.ifSameWeek(mViewList.get(i).getFirstDay(), c, mCalendarView.getFirstDayOfWeek()))) {
                return mViewList.get(i).getChildAt(c);
            }
        }
        return null;
    }

    public Calendar getRangeEnd() {
        return mRangeEnd;
    }

    public Calendar getRangeStart() {
        return mRangeStart;
    }

    /**
     * @return cached MonthItems
     */
    public ArrayList<MonthItem> getViewList() {
        return mViewList;
    }
}
