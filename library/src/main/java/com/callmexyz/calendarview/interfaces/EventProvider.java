package com.callmexyz.calendarview.interfaces;

import com.callmexyz.calendarview.MapDayEvent;

import java.util.Calendar;

/**
 * Created by CallMeXYZ on 2016/4/7.
 */

/**
 * provider for day event numbers of a month or week rather than for every day to avoid frequent calculation  such as database reading.
 * {@link #getMonthDayEvents(Calendar, Calendar)} will only be called when {@link com.callmexyz.calendarview.PageItem} is created.
 */
public interface EventProvider {
    /**
     * @return Map<Integer, Integer> means {@link Calendar#DAY_OF_MONTH} and events number
     */
    MapDayEvent getMonthDayEvents(Calendar start, Calendar end);
}


