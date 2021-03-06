package com.callmexyz.calendarview.dayclicklistener;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.callmexyz.calendarview.DayView;
import com.callmexyz.calendarview.R;

import java.util.Calendar;

/**
 * Created by CallMeXYZ on 2016/3/23.
 * margins indicate the colored circle's margin to tge day view;
 * Note:  in landscape mode, the circle may seem big though u have already set the margin;
 * maybe u can write ur own CircleColor and set margins of several percent of the DayView's size,
 * if u r considering screen rotation
 */
public class CircleColor implements DayClickListener {
    @ColorInt
    int mCicleColor;
    @ColorInt
    int mTextColor;
    int mMarginLeft;
    int mMarginRight;
    int mMarginTop;
    int mMarginBottom;

    public CircleColor(@ColorInt int circleColor) {
        this(circleColor, Color.WHITE, 8, 8, 8, 8);
    }

    /**
     * @param circleColor
     * @param textColor
     * @param marginLeft
     * @param marginTop
     * @param marginRight
     * @param marginBottom
     */
    public CircleColor(@ColorInt int circleColor, @ColorInt int textColor, int marginLeft, int marginTop, int marginRight, int marginBottom) {
        mCicleColor = circleColor;
        mTextColor = textColor;
        mMarginLeft = marginLeft;
        mMarginTop = marginTop;
        mMarginRight = marginRight;
        mMarginBottom = marginBottom;
    }

    @Override
    public void onDayClick(DayView view, Calendar c, boolean ifRestoring) {
        Drawable d = view.getResources().getDrawable(R.drawable.blue_circle);
        d.setColorFilter(mCicleColor, PorterDuff.Mode.SRC_ATOP);
        view.getSelectView().setBackgroundDrawable(d);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        lp.setMargins(mMarginLeft, mMarginTop, mMarginRight, mMarginBottom);
        view.getSelectView().setLayoutParams(lp);
        view.getText().setTextColor(mTextColor);
    }


    @Override
    public void onDayUnClick(DayView view, Calendar c) {
        view.getSelectView().setBackgroundColor(view.getResources().getColor(android.R.color.transparent));
        view.getText().setTextColor(view.getTextColor());
    }
}
