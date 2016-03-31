package com.callmexyz.calendarview.dayclicklistener;

import com.callmexyz.calendarview.DayView;

import java.util.Calendar;

/**
 * Created by CallMeXYZ on 2016/3/31.
 */

/**
 * when a day is clicked, first the {@link #onDayUnClick(DayView view, Calendar c) }
 * will be called to handle the former DayView's unselected state then the
 * {@link #onDayClick(DayView view, Calendar c, boolean ifRestoring) }
 * is called to handle the selected state.<p/>
 * note
 */
public interface DayClickListener {

    /**
     * this will be called when a DayView is called or a pre-clicked DayView is recreated
     *
     * @param view
     * @param c
     * @param ifRestoring
     */
    void onDayClick(DayView view, Calendar c, boolean ifRestoring);

    void onDayUnClick(DayView view, Calendar c);
}

