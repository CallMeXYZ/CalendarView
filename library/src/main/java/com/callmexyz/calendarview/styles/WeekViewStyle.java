package com.callmexyz.calendarview.styles;

import android.support.annotation.ColorInt;

import java.io.Serializable;

/**
 * Created by CallMeXYZ on 2016/3/24.
 */
public class WeekViewStyle implements Serializable {
    private int height;
    private CharSequence[] names;
    private @ColorInt int textColor;
    private @ColorInt int bgColor;
    private float textSize;

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public CharSequence[] getNames() {
        return names;
    }

    public void setNames(CharSequence[] names) {
        this.names = names;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }
}
