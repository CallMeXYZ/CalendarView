package com.callmexyz.calendarview.styles;

import android.support.annotation.ColorInt;

import java.io.Serializable;

/**
 * Created by CallMeXYZ on 2016/3/24.
 */
public class DayViewStyle implements Serializable{
    //inMonth means the day view is in the showing month  while outMonth the opposite
    private @ColorInt int textColorInMonth;
    private @ColorInt int textColorOutMonth;
    private @ColorInt int bgColorInMonth;
    private @ColorInt int bgColorOutMonth;
    private float textSize;

    private boolean outMonthClickable;
    // invisible will be unClickable
    private boolean outMonthVisible;


    public int getTextColorInMonth() {
        return textColorInMonth;
    }

    public void setTextColorInMonth(int textColorInMonth) {
        this.textColorInMonth = textColorInMonth;
    }

    public int getTextColorOutMonth() {
        return textColorOutMonth;
    }

    public void setTextColorOutMonth(int textColorOutMonth) {
        this.textColorOutMonth = textColorOutMonth;
    }

    public int getBgColorInMonth() {
        return bgColorInMonth;
    }

    public void setBgColorInMonth(int bgColorInMonth) {
        this.bgColorInMonth = bgColorInMonth;
    }

    public int getBgColorOutMonth() {
        return bgColorOutMonth;
    }

    public void setBgColorOutMonth(int bgColorOutMonth) {
        this.bgColorOutMonth = bgColorOutMonth;
    }

    public boolean isOutMonthClickable() {
        return outMonthClickable;
    }

    public void setOutMonthClickable(boolean outMonthClickable) {
        this.outMonthClickable = outMonthClickable;
    }

    public boolean isOutMonthVisible() {
        return outMonthVisible;
    }

    public void setOutMonthVisible(boolean outMonthVisible) {
        this.outMonthVisible = outMonthVisible;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }
}
