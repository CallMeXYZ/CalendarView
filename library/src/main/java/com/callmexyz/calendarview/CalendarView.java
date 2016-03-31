package com.callmexyz.calendarview;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.callmexyz.calendarview.dayclicklistener.DayClickListener;
import com.callmexyz.calendarview.styles.DayViewStyle;
import com.callmexyz.calendarview.styles.WeekViewStyle;

import java.util.Calendar;

/**
 * Created by CallMeXYZ on 2016/3/22.
 */
public class CalendarView extends ViewGroup {
    private final String TAG = CalendarView.class.getSimpleName();
    //in dp
    private final int default_week_title_height = 40;
    private final int default_week_text_size = 16;
    private final int default_day_text_size = 16;
    private MonthSelectedListener mMonthSelectedListener;
    private DayClickListener mDayClickListener;
    // date for the current showing month's start
    private Calendar mCurrentMonthStart;
    private int mCurrentPosition;

    //styles
    private DayViewStyle mDayViewStyle;
    private WeekViewStyle mWeekViewStyle;
    private int mFirstDayOfWeek;


    private WeekView mWeekView;
    private MonthViewPager mMonthViewPager;
    private MonthPagerAdapter mMonthPagerAdapter;
    private Calendar mSelectedCalendar;


    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        initStyles(context, attrs);
//        if (isInEditMode()) {
//            addView(new WeekView(getContext(), Calendar.SUNDAY, this));
//            addView(new MonthItem(getContext(),Utils.getNowMonthStart() , this));
//            return;
//        }
        initViews();
    }

    // TODO: 2016/3/24
    private void initStyles(Context context, AttributeSet attr) {
        mDayViewStyle = new DayViewStyle();
        TypedArray ta = getContext().obtainStyledAttributes(attr, R.styleable.xyz_calendarview, 0, 0);
        try {
            mFirstDayOfWeek = ta.getInt(R.styleable.xyz_calendarview_xyz_firstDayOfWeek, Calendar.SUNDAY);
            //day view
            mDayViewStyle.setTextColorInMonth(ta.getColor(R.styleable.xyz_calendarview_xyz_textColorInMonth, getResources().getColor(android.R.color.black)));
            mDayViewStyle.setTextColorOutMonth(ta.getColor(R.styleable.xyz_calendarview_xyz_textColorOutMonth, getResources().getColor(android.R.color.darker_gray)));
            mDayViewStyle.setBgColorInMonth(ta.getColor(R.styleable.xyz_calendarview_xyz_bgColorInMonth, getResources().getColor(android.R.color.transparent)));
            mDayViewStyle.setBgColorOutMonth(ta.getColor(R.styleable.xyz_calendarview_xyz_bgColorOutMonth, getResources().getColor(android.R.color.transparent)));
            mDayViewStyle.setOutMonthVisible(ta.getBoolean(R.styleable.xyz_calendarview_xyz_outMonthVisible, true));
            mDayViewStyle.setOutMonthClickable(ta.getBoolean(R.styleable.xyz_calendarview_xyz_outMonthClickable, true));
            mDayViewStyle.setTextSize(ta.getDimension(R.styleable.xyz_calendarview_xyz_dayTextSize, Utils.dpToPx(default_day_text_size, context)));
            //week view
            mWeekViewStyle = new WeekViewStyle();
            mWeekViewStyle.setHeight((int) ta.getDimension(R.styleable.xyz_calendarview_xyz_weekNameHeight, Utils.dpToPx(default_week_title_height, context)));
            mWeekViewStyle.setTextSize(ta.getDimension(R.styleable.xyz_calendarview_xyz_weekTextSize, Utils.dpToPx(default_week_text_size, context)));
            mWeekViewStyle.setNames(ta.getTextArray(R.styleable.xyz_calendarview_xyz_weekNames));
            mWeekViewStyle.setBgColor(ta.getColor(R.styleable.xyz_calendarview_xyz_weekBgColor, getResources().getColor(android.R.color.transparent)));
            mWeekViewStyle.setTextColor(ta.getColor(R.styleable.xyz_calendarview_xyz_weekTextColor, getResources().getColor(android.R.color.black)));

        } finally {
            ta.recycle();
        }
    }

    private void initViews() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //If we're on good Android versions, turn off clipping for cool effects
            setClipToPadding(false);
            setClipChildren(false);
        } else {
            //Old Android does not like _not_ clipping view pagers, we need to clip
            setClipChildren(true);
            setClipToPadding(true);
        }

        mWeekView = new WeekView(getContext(), this);
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

//        setPadding(getPaddingLeft() + widthSize % Utils.WEEK_SIZE / 2, getPaddingTop(), getPaddingRight() + widthSize % Utils.WEEK_SIZE - widthSize % Utils.WEEK_SIZE / 2, getPaddingBottom());

        setPadding(getPaddingLeft() , getPaddingTop(), getPaddingRight(), getPaddingBottom());

        int resizedWidth = widthSize - getPaddingLeft() - getPaddingRight();
        int resizedHeight = heightSize - getPaddingTop() - getPaddingBottom();
        // TODO: 2016/3/24
        int minHeight = resizedWidth / 7 * 5 + mWeekViewStyle.getHeight();
        if (MeasureSpec.UNSPECIFIED == heightMode)
            resizedHeight = Math.max(resizedHeight, minHeight);
        else if (MeasureSpec.AT_MOST == heightMode) {
            resizedHeight = Math.min(resizedHeight, minHeight);
        }
        setMeasuredDimension(resizedWidth + getPaddingLeft() + getPaddingRight(), resizedHeight + getPaddingTop() + getPaddingBottom());
        getChildAt(0).measure(MeasureSpec.makeMeasureSpec(resizedWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(mWeekViewStyle.getHeight(), MeasureSpec.EXACTLY));
        getChildAt(1).measure(MeasureSpec.makeMeasureSpec(resizedWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(resizedHeight - mWeekViewStyle.getHeight(), MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View child1 = getChildAt(0);
        int i = getPaddingLeft();
        int j = child1.getMeasuredWidth();
        child1.layout(getPaddingLeft(), getPaddingTop(), getPaddingLeft() + child1.getMeasuredWidth(), getPaddingTop() + child1.getMeasuredHeight());
        View child2 = getChildAt(1);
        child2.layout(getPaddingLeft(), getPaddingTop() + child1.getMeasuredHeight(), getPaddingLeft() + child2.getMeasuredWidth(), getPaddingTop() + child1.getMeasuredHeight() + child2.getMeasuredHeight());
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        super.onSaveInstanceState();
        Bundle b = new Bundle();
        b.putSerializable("weekStyle", mWeekViewStyle);
        b.putSerializable("dayStyle", mDayViewStyle);
        return b;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable b) {
        super.onRestoreInstanceState(b);
        if (b instanceof Bundle) {
            mWeekViewStyle = (WeekViewStyle) ((Bundle) b).getSerializable("weekStyle");
            mDayViewStyle = (DayViewStyle) ((Bundle) b).getSerializable("dayStyle");
        }
    }

    protected void handleDayClick(DayView view) {
        if (null != mDayClickListener && null != mSelectedCalendar) {
            //get DayView from adapter, if it is not in one of the  cached MonthItems,then no
            //need to handle UnClick
            DayView v = mMonthPagerAdapter.getDayView(mSelectedCalendar);
            if (null != v)
                mDayClickListener.onDayUnClick(v, (Calendar) v.getDate().clone());
        }
        mSelectedCalendar = (Calendar) view.getDate().clone();
        if (null != mDayClickListener)
            mDayClickListener.onDayClick(view, (Calendar) view.getDate().clone(), false);
    }


    public void setMonthSelectedListener(MonthSelectedListener listener) {
        mMonthSelectedListener = listener;
    }

    public DayClickListener getDayClickListener() {
        return mDayClickListener;
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

    public DayViewStyle getDayViewStyle() {
        return mDayViewStyle;
    }

    public void setDayViewStyle(DayViewStyle mDayViewStyle) {
        this.mDayViewStyle = mDayViewStyle;
    }

    public WeekViewStyle getWeekViewStyle() {
        return mWeekViewStyle;
    }

    public Calendar getSelectedCalendar() {
        return mSelectedCalendar;
    }

    public int getFirstDayOfWeek() {
        return mFirstDayOfWeek;
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



}
