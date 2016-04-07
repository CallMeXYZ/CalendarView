package com.callmexyz.calendarview.interfaces;

import com.callmexyz.calendarview.DayView;

/**
 * Created by CallMeXYZ on 2016/4/7.
 */

/**
 * interface for {@link DayView} to handle the event related things
 */
public interface DayEventHandler {

    /**
     * @param dayView
     * @param ifSameMonth note that when in week mode u should ignore the ifSameMonth while in month mode
     *                    u should consider the difference
     * @param eventNum
     */
    void handleDayEvent(DayView dayView, boolean ifSameMonth, int eventNum);
}
