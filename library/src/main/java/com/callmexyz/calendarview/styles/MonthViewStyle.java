package com.callmexyz.calendarview.styles;

import java.io.Serializable;

/**
 * Created by CallMeXYZ on 2016/4/1.
 */
public class MonthViewStyle  implements Serializable{
    private MonthType mMonthType;

    public MonthType getMonthType() {
        return mMonthType;
    }

    public void setMonthType(MonthType mMonthType) {
        this.mMonthType = mMonthType;
    }

    public enum MonthType {
        MONTH_VIEW, WEEK_VIEW;
    }
}
