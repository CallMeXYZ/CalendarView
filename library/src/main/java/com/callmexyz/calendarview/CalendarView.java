package com.callmexyz.calendarview;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

/**
 * Created by CallMeXYZ on 2016/3/22.
 */
public class CalendarView extends ViewGroup {
    private final String TAG = CalendarView.class.getSimpleName();
    //in dp
    private final int default_week_title_height = 40;
    private MonthSelectedListener mMonthSelectedListener;
    private DayClickListener mDayClickListener;
    // date for the current showing month's start
    private Calendar mCurrentMonthStart;
    private int mCurrentPosition;
    private WeekView mWeekView;
    private MonthViewPager mMonthViewPager;
    private MonthPagerAdapter mMonthPagerAdapter;
    private DayView mSelectedDayView;

    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        if (isInEditMode()) {
            addView(new WeekView(getContext(), Calendar.SUNDAY, this));
            addView(new MonthItem(getContext(), Calendar.getInstance(), this));
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //If we're on good Android versions, turn off clipping for cool effects
            setClipToPadding(false);
            setClipChildren(false);
        } else {
            //Old Android does not like _not_ clipping view pagers, we need to clip
            setClipChildren(true);
            setClipToPadding(true);
        }

        mWeekView = new WeekView(getContext(), Calendar.SUNDAY, this);
        mMonthViewPager = new MonthViewPager(getContext());

        mMonthPagerAdapter = new MonthPagerAdapter(this);
        mMonthViewPager.setAdapter(mMonthPagerAdapter);
        mMonthViewPager.setCurrentItem(mMonthPagerAdapter.getPosition(Calendar.getInstance()));
        mMonthViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentMonthStart = mMonthPagerAdapter.getItemCalendar(position);
                mCurrentPosition = position;
                if (null != mMonthSelectedListener)
                    mMonthSelectedListener.onMonthSelected((Calendar) mCurrentMonthStart.clone());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        addView(mWeekView);
        addView(mMonthViewPager);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int resizedWidth = widthSize - widthSize % Utils.WEEK_SIZE;
        int defaultTitleHeight = Utils.dpToPx(default_week_title_height, getContext());
        int minHeight = resizedWidth / 7 * 5 + defaultTitleHeight;
        if (MeasureSpec.UNSPECIFIED == heightMode) heightSize = Math.max(heightSize, minHeight);
        else if (MeasureSpec.AT_MOST == heightMode) {
            heightSize = Math.min(heightSize, minHeight);
        }
        setPadding(widthSize % Utils.WEEK_SIZE / 2, 0, widthSize % Utils.WEEK_SIZE - widthSize % Utils.WEEK_SIZE / 2, 0);
        setMeasuredDimension(resizedWidth, heightSize);
        getChildAt(0).measure(MeasureSpec.makeMeasureSpec(resizedWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(defaultTitleHeight, MeasureSpec.EXACTLY));
        getChildAt(1).measure(MeasureSpec.makeMeasureSpec(resizedWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(heightSize - defaultTitleHeight, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View child1 = getChildAt(0);
        child1.layout(0, 0, child1.getMeasuredWidth(), child1.getMeasuredHeight());
        View child2 = getChildAt(1);
        child2.layout(0, child1.getMeasuredHeight(), child2.getMeasuredWidth(), child1.getMeasuredHeight() + child2.getMeasuredHeight());
    }

    protected void handleDayClick(DayView view) {
        if (null != mDayClickListener&&null!=mSelectedDayView)
            mDayClickListener.onDayUnClick(mSelectedDayView, view.getCalendar());
        mSelectedDayView = view;
        if (null != mDayClickListener)
            mDayClickListener.onDayClick(mSelectedDayView, view.getCalendar());
    }


    public void setMonthSelectedListener(MonthSelectedListener listener) {
        mMonthSelectedListener = listener;
    }


    public void setDayClickListener(DayClickListener listener) {
        mDayClickListener = listener;
    }

    public void navToMonth(Calendar c) {
        navToMonth(mMonthPagerAdapter.getPosition(c));
    }

    public void navToNext() {
        navToMonth(mCurrentPosition + 1);
    }

    public void navToFormer() {
        navToMonth(mCurrentPosition - 1);
    }

    private void navToMonth(int position) {
        if (position < 0) {
            position = 0;
            Log.w(TAG, "the given position is former than the range start");
        } else if (position > mMonthPagerAdapter.getCount()) {
            position = mMonthPagerAdapter.getCount();
            Log.w(TAG, "the given position is latter than the range start");
        }
        mMonthViewPager.setCurrentItem(position);

    }

    public void setRange(Calendar start, Calendar end) {
        mMonthPagerAdapter.setRange(start, end);
        navToMonth(mCurrentMonthStart);
    }
    /////////////////////////////////////////////////////////////////////////////
    //////                                                                   ////
    //////     interfaces                                                    ////
    //////                                                                   ////
    /////////////////////////////////////////////////////////////////////////////

    /**
     * interface for month showing on the screen,return the calendar of month start
     */
    public interface MonthSelectedListener {
        void onMonthSelected(Calendar c);
    }

    /**
     * when a day is clicked, first the {@link #onDayUnClick(DayView view, Calendar c) } will be called
     * to handle the former DayView's unselected state then the {@link #onDayClick(DayView view, Calendar c) }
     * is called to handle the selected state
     */
    public interface DayClickListener {
        void onDayClick(DayView view, Calendar c);

        void onDayUnClick(DayView view, Calendar c);
    }
}
