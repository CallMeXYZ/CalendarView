package com.callmexyz.calendarview;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by CallMeXYZ on 2016/4/7.
 */
public class MapDayEvent extends HashMap<String, Integer> {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public void putEvent(Date d, int num) {
        if (null == d) return;
        put(sdf.format(d), num);
    }

    public void putEvent(Calendar c, int num) {
        if (null == c) return;
        put(sdf.format(new Date(c.getTimeInMillis())), num);
    }

    public Integer getEventNum(Date d) {
        return get(sdf.format(d));
    }

    public Integer getEventNum(Calendar c) {
        return get(sdf.format(new Date(c.getTimeInMillis())));
    }
}
