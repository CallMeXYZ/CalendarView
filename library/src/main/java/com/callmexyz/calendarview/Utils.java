package com.callmexyz.calendarview;

import android.content.Context;
import android.util.TypedValue;

import java.util.Calendar;

/**
 * Created by CallMeXYZ on 2016/3/22.
 */
public class Utils {
    public static final int WEEK_SIZE = 7;
    public static final int MONTH_VIEW_WEEK_NUM = 5;
    public static final int MONTH_SIZE = WEEK_SIZE * MONTH_VIEW_WEEK_NUM;

    public static Calendar getMonthViewStart(Calendar c, int fisrtDayOfWeek) {
        Calendar r = (Calendar) c.clone();
        r.set(Calendar.DAY_OF_MONTH, 1);
        r.set(Calendar.DAY_OF_WEEK, fisrtDayOfWeek);
        return r;
    }

    /**
     * day of month has been initialized to 1
     *
     * @return
     * @long time
     */
    public static Calendar getRange(long time) {
        Calendar r = Calendar.getInstance();
        r.setTimeInMillis(time);
        r.set(Calendar.DAY_OF_MONTH, 1);
        return r;
    }

    /**
     * day of month has been initialized to 1
     *
     * @return
     */
    public static Calendar getRangeStart() {
        Calendar r = getRange(System.currentTimeMillis());
        r.add(Calendar.YEAR, -200);
        return r;
    }

    /**
     * day of month has been initialized to 1
     *
     * @return
     */
    public static Calendar getRangeEnd() {
        Calendar r = getRange(System.currentTimeMillis());
        r.add(Calendar.YEAR, 200);
        return r;
    }

    public static int getMonthDiff(Calendar small, Calendar big) {
        if (big.before(small)) return 0;
        return (big.get(Calendar.YEAR) - small.get(Calendar.YEAR)) * 12 + big.get(Calendar.MONTH) - small.get(Calendar.MONTH);
    }

    public static int dpToPx(int dp, Context context) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics()
        );
    }

}
