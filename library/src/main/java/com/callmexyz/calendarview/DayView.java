package com.callmexyz.calendarview;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import com.callmexyz.calendarview.styles.DayViewStyle;

import java.util.Calendar;

/**
 * Created by CallMeXYZ on 2016/3/22.
 */
public class DayView extends TextView {

    private Calendar mDate;
    private Calendar mMonthStart;
    private DayViewStyle mDayViewStyle;
    //textSize in pix
    private float mTextSize;
    private
    @ColorInt
    int mTextColor;
    private
    @ColorInt
    int mBgColor;

    public DayView(Context context, Calendar mDate, Calendar mMonthStart, DayViewStyle dayViewStyle) {
        super(context);
        init(mDate, mMonthStart, dayViewStyle);

    }

    private void init(Calendar mDate, Calendar mMonthStart, DayViewStyle dayViewStyle) {
        this.mDate = mDate;
        this.mMonthStart = mMonthStart;
        this.mDayViewStyle = dayViewStyle;
        mTextSize = mDayViewStyle.getTextSize();
        if (Utils.ifSameMonth(mDate, mMonthStart)) {
            mTextColor = dayViewStyle.getTextColorInMonth();
            mBgColor = dayViewStyle.getBgColorInMonth();
        } else {
            mTextColor = dayViewStyle.getTextColorOutMonth();
            mBgColor = dayViewStyle.getBgColorInMonth();
        }
        if (Utils.ifSameMonth(mDate, mMonthStart) || dayViewStyle.isOutMonthVisible()) {
            setGravity(Gravity.CENTER);
            setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            setText(mDate.get(Calendar.DAY_OF_MONTH) + "");
            setTextColor(mTextColor);
            setBackgroundColor(mBgColor);
        }
    }

    public Calendar getDate() {
        return mDate;
    }

    public Calendar getMonthStart() {
        return mMonthStart;
    }

    public float getTextSize() {
        return mTextSize;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public int getBgColor() {
        return mBgColor;
    }

    public DayViewStyle getDayViewStyle() {
        return mDayViewStyle;
    }
}
