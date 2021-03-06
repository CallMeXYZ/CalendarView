package com.callmexyz.calendarview;

import android.content.Context;
import android.util.TypedValue;

import java.util.Calendar;

/**
 * Created by CallMeXYZ on 2016/3/22.
 */
public class Utils {
    public static final int WEEK_SIZE = 7;
    public static final int MONTH_VIEW_WEEK_NUM = 6;
    public static final int MONTH_VIEW_DAY_SIZE = WEEK_SIZE * MONTH_VIEW_WEEK_NUM;

    public static Calendar getMonthViewStart(Calendar c, int fisrtDayOfWeek) {
        Calendar r = (Calendar) c.clone();
        r.set(Calendar.DAY_OF_WEEK, fisrtDayOfWeek);
        if (r.after(c)) r.add(Calendar.WEEK_OF_YEAR, -1);
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

    public static Calendar getNowMonthStart() {
        Calendar r = Calendar.getInstance();
        r.set(Calendar.DAY_OF_MONTH, 1);
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

    public static boolean ifSameMonth(Calendar c1, Calendar c2) {
        if (null == c1 || null == c2) return false;
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH);

    }

    public static boolean ifSameDay(Calendar c1, Calendar c2) {
        if (null == c1 || null == c2) return false;
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR);

    }

    public static boolean ifSameWeekAsWeekStart(Calendar weekStart, Calendar cal) {
        if (ifSameDay(weekStart, cal)) return true;
        if (weekStart.after(cal)) return false;
        return getDayDifference(weekStart, cal) < 7;
    }

    /**
     * @param cc1
     * @param cc2
     * @param firstDayOfWeek
     * @return
     * @deprecated function good when call in normal use ,but problematic when in {@link MonthPagerAdapter}.Why?
     */
    public static boolean ifSameWeek(Calendar cc1, Calendar cc2, int firstDayOfWeek) {
        if (null == cc1 || null == cc2) return false;
        Calendar c1 = (Calendar) cc1.clone();
        Calendar c2 = (Calendar) cc2.clone();
        c1.setFirstDayOfWeek(firstDayOfWeek);
        c2.setFirstDayOfWeek(firstDayOfWeek);
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.WEEK_OF_YEAR) == c2.get(Calendar.WEEK_OF_YEAR);
    }

    public static int getMonthDiff(Calendar small, Calendar big) {
        if (big.before(small)) return 0;
        return (big.get(Calendar.YEAR) - small.get(Calendar.YEAR)) * 12 + big.get(Calendar.MONTH) - small.get(Calendar.MONTH);
    }

    public static int getWeekDiff(Calendar s, Calendar b, int firstDayOfWeek) {
        if (ifSameWeek(s, b, firstDayOfWeek)) return 0;
        if (b.before(s)) return getWeekDiff(s, b, firstDayOfWeek);
        Calendar small = (Calendar) s.clone();
        Calendar big = (Calendar) b.clone();
        small.setFirstDayOfWeek(firstDayOfWeek);
        int weekEnd = (firstDayOfWeek -1 ) % 7;
        if (0 == weekEnd) weekEnd = Calendar.SATURDAY;
        small.set(Calendar.DAY_OF_WEEK, weekEnd);
        return (int) (getDayDifference(small, big) / 7) + 1;
    }

    public static long getDayDifference(Calendar small, Calendar big) {
        if (ifSameDay(small, big)) return 0;
        if (big.before(small)) return getDayDifference(big, small);
        Calendar c1 = (Calendar) small.clone();
        c1.set(Calendar.HOUR_OF_DAY, 23);
        c1.set(Calendar.MINUTE, 59);
        c1.set(Calendar.SECOND, 59);
        return (big.getTimeInMillis() - c1.getTimeInMillis()) / (24 * 3600 * 1000) + 1;
    }

    public static int dpToPx(int dp, Context context) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics()
        );
    }

}
