package com.callmexyz.calendarview;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.callmexyz.calendarview.interfaces.DayEventHandler;
import com.callmexyz.calendarview.styles.DayViewStyle;
import com.callmexyz.calendarview.styles.MonthViewStyle;

import java.util.Calendar;

/**
 * Created by CallMeXYZ on 2016/3/22.
 */
public class DayView extends RelativeLayout {
    public static final
    @IdRes
    int SELECT_VIEW_ID = 1;
    public static final
    @IdRes
    int TEXT_VIEW_ID = 2;
    private MonthViewStyle mMonthViewStyle;
    private Calendar mDate;
    private Calendar mMonthStart;
    private DayViewStyle mDayViewStyle;
    /**
     * textSize in pix
     */
    private float mTextSize;
    private
    @ColorInt
    int mTextColor;
    private
    @ColorInt
    int mBgColor;
    private int mEventNum;
    private boolean mIfSameMonth;
    private TextView mText;
    /**
     * view to add when DayView is Clicked,have to add this before {@link DayView#mText}.
     * so that the click effect view won't cover the day text
     */
    private View mSelectView;

    public DayView(Context context, Calendar mDate, Calendar mMonthStart, DayViewStyle dayViewStyle, MonthViewStyle monthViewStyle,boolean ifSameMonth, Integer eventNum, DayEventHandler dayEventHandler) {
        super(context);
        init(mDate, mMonthStart, dayViewStyle, monthViewStyle,ifSameMonth,eventNum, dayEventHandler);

    }

    private void init(Calendar mDate, Calendar mMonthStart, DayViewStyle dayViewStyle, MonthViewStyle monthViewStyle, boolean ifSameMonth,Integer eventNum, DayEventHandler dayEventHandler) {
        this.mDate = mDate;
        this.mMonthStart = mMonthStart;
        this.mDayViewStyle = dayViewStyle;
        this.mMonthViewStyle = monthViewStyle;
        this.mEventNum =eventNum;
        this.mIfSameMonth = ifSameMonth;
        mTextSize = mDayViewStyle.getTextSize();

        if (MonthViewStyle.MonthType.WEEK_VIEW == mMonthViewStyle.getMonthType() || Utils.ifSameMonth(mDate, mMonthStart)) {
            mTextColor = dayViewStyle.getTextColorInMonth();
            mBgColor = dayViewStyle.getBgColorInMonth();
        } else {
            mTextColor = dayViewStyle.getTextColorOutMonth();
            mBgColor = dayViewStyle.getBgColorOutMonth();
        }
        if (null != dayEventHandler&&null!=eventNum) dayEventHandler.handleDayEvent(this, ifSameMonth, eventNum);
        setBackgroundColor(mBgColor);
        if (Utils.ifSameMonth(mDate, mMonthStart) || dayViewStyle.isOutMonthVisible()) {
            RelativeLayout.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            lp.addRule(RelativeLayout.CENTER_IN_PARENT);
            mSelectView = new View(getContext());
            mSelectView.setId(SELECT_VIEW_ID);
            addView(mSelectView, lp);
            mText = new TextView(getContext());
            mText.setId(TEXT_VIEW_ID);
            mText.setGravity(Gravity.CENTER);
            mText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            mText.setText(mDate.get(Calendar.DAY_OF_MONTH) + "");
            mText.setTextColor(mTextColor);
            RelativeLayout.LayoutParams lp2 = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            lp2.addRule(RelativeLayout.CENTER_IN_PARENT);
            addView(mText, lp2);
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

    public MonthViewStyle getMonthViewStyle() {
        return mMonthViewStyle;
    }

    public DayViewStyle getDayViewStyle() {
        return mDayViewStyle;
    }

    public TextView getText() {
        return mText;
    }

    public View getSelectView() {
        return mSelectView;
    }
}
