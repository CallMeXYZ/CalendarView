package com.callmexyz.calendarview;

import android.content.Context;
import android.support.annotation.StringRes;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by CallMeXYZ on 2016/3/22.
 */
public class WeekView extends ViewGroup {
    private int mFirstDayOfWeek;
    private CalendarView mCalendarView;

    public WeekView(Context context, CalendarView calendarView) {
        super(context);
        mCalendarView = calendarView;
        initViews();
    }

    private void initViews() {
        int firstDayOfWeek = mCalendarView.getFirstDayOfWeek();
        setBackgroundColor(mCalendarView.getWeekViewStyle().getBgColor());
        if (firstDayOfWeek > Calendar.SATURDAY || firstDayOfWeek < Calendar.SUNDAY)
            firstDayOfWeek = 0;
        mFirstDayOfWeek = firstDayOfWeek;
        CharSequence[] names = mCalendarView.getWeekViewStyle().getNames();
        int[] nameRess = new int[]{R.string.sunday_, R.string.monday_, R.string.tuesday_, R.string.wednesday_, R.string.thursday_, R.string.friday_, R.string.saturday_};
        for (int i = 0; i < 7; i++) {
            if (firstDayOfWeek > Calendar.SATURDAY) firstDayOfWeek = Calendar.SUNDAY;
            if (null != names && names.length == nameRess.length)
                addWeekName(names[firstDayOfWeek - 1]);
            else addWeekName(nameRess[firstDayOfWeek - 1]);
            firstDayOfWeek += 1;
        }
    }

    private void addWeekName(CharSequence name) {
        TextView tv = new TextView(getContext());
        tv.setGravity(Gravity.CENTER);
        tv.setText(name);
        tv.setTextColor(mCalendarView.getWeekViewStyle().getTextColor());
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mCalendarView.getWeekViewStyle().getTextSize());
        addView(tv);

    }

    private void addWeekName(@StringRes int res) {
        addWeekName(getContext().getString(res));
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
        int childHeight = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).measure(childWidth, childHeight);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = 0;
        int width = 0;
        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            if (0 == i) {
                width = getChildAt(i).getMeasuredWidth();
                height = getChildAt(i).getMeasuredHeight();
            }
            getChildAt(i).layout(left, 0, left + width, height);
            left += width;
        }
    }
}
