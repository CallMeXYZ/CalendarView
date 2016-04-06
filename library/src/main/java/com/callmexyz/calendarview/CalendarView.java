package com.callmexyz.calendarview;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.callmexyz.calendarview.dayclicklistener.DayClickListener;
import com.callmexyz.calendarview.styles.DayViewStyle;
import com.callmexyz.calendarview.styles.MonthViewStyle;
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
    boolean collapsing = false;
    private MonthSelectedListener mMonthSelectedListener;
    private DayClickListener mDayClickListener;
    // date for the current showing month's start
    // TODO: 2016/4/5 should use week start when in week mode
    private Calendar mCurrentMonthStart;
    private int mCurrentPosition;
    //styles
    private DayViewStyle mDayViewStyle;
    private WeekViewStyle mWeekViewStyle;
    private MonthViewStyle mMonthViewStyle;
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
            //Month view
            int montMode = ta.getInt(R.styleable.xyz_calendarview_xyz_monthViewMode, 1);
            mMonthViewStyle = new MonthViewStyle();
            if (montMode == 1) mMonthViewStyle.setMonthType(MonthViewStyle.MonthType.MONTH_VIEW);
            else mMonthViewStyle.setMonthType(MonthViewStyle.MonthType.WEEK_VIEW);

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
        mCurrentPosition = mMonthPagerAdapter.getPosition(Calendar.getInstance());
        mMonthViewPager.setCurrentItem(mCurrentPosition);
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

        setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());

        int resizedWidth = widthSize - getPaddingLeft() - getPaddingRight();
        int resizedHeight = heightSize - getPaddingTop() - getPaddingBottom();
        // TODO: 2016/3/24
//        int minHeight = resizedWidth / Utils.WEEK_SIZE * Utils.MONTH_VIEW_WEEK_NUM + mWeekViewStyle.getHeight();
        int minHeight = 0;
        if (MonthViewStyle.MonthType.MONTH_VIEW == mMonthViewStyle.getMonthType())
            minHeight = resizedWidth / Utils.WEEK_SIZE * Utils.MONTH_VIEW_WEEK_NUM + mWeekViewStyle.getHeight();
        else minHeight = resizedWidth / 7 + mWeekViewStyle.getHeight();

        if (MeasureSpec.UNSPECIFIED == heightMode)
            resizedHeight = Math.max(resizedHeight, minHeight);
        else if (MeasureSpec.AT_MOST == heightMode) {
            resizedHeight = Math.min(resizedHeight, minHeight);
        }
        if (collapsing)
            setMeasuredDimension(widthSize + getPaddingLeft() + getPaddingRight(), heightSize + getPaddingTop() + getPaddingBottom());
        else
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
        SavedState s = new SavedState(super.onSaveInstanceState());
        s.weekViewStyle = mWeekViewStyle;
        s.dayViewStyle = mDayViewStyle;
        s.monthViewStyle = mMonthViewStyle;
        s.firstDayOfWeek = mFirstDayOfWeek;
        s.currentPosition = mCurrentPosition;
        s.rangeStart = mMonthPagerAdapter.getRangeStart();
        s.rangeEnd = mMonthPagerAdapter.getRangeEnd();
        s.selectCalendar = mSelectedCalendar;
        return s;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable p) {
        SavedState s = (SavedState) p;
        super.onRestoreInstanceState(s.getSuperState());
        mWeekViewStyle = s.weekViewStyle;
        mDayViewStyle = s.dayViewStyle;
        mMonthViewStyle = s.monthViewStyle;
        mFirstDayOfWeek = s.firstDayOfWeek;
        //'d better call this at first, this will remove the existing DayViews and WeekView items
        // TODO: 2016/4/1  pagerAdapter need to notify??
        refreshUI();

        setRange(s.rangeStart, s.rangeEnd);
        navToPage(s.currentPosition);
        restoreSelect(s.selectCalendar);
    }

    private void restoreSelect(Calendar c) {
        mSelectedCalendar = c;
        DayView selectView = mMonthPagerAdapter.getDayView(c);
        if (null != selectView && null != mDayClickListener)
            mDayClickListener.onDayClick(selectView, (Calendar) selectView.getDate().clone(), true);
    }

    protected void handleDayClick(DayView view) {
        if (null == view) {
            Log.w(TAG, "u are handleDayClick of a NULL DayView");
            return;
        }
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

    public void navToPage(Calendar c) {
        navToPage(mMonthPagerAdapter.getPosition(c));
    }

    public void navToNext() {
        navToPage(mCurrentPosition + 1);
    }

    public void navToFormer() {
        navToPage(mCurrentPosition - 1);
    }

    private void navToPage(int position) {
        if (position < 0) {
            position = 0;
            Log.w(TAG, "the given position is former than the range start");
        } else if (position > mMonthPagerAdapter.getCount()) {
            position = mMonthPagerAdapter.getCount();
            Log.w(TAG, "the given position is latter than the range start");
        }
        if (mCurrentPosition != position)
            mMonthViewPager.setCurrentItem(position);

    }

    /**
     * may not work when the CalendarView is creating  or initiating (such as u just get it from the layout)
     * On this condition,{@link #selectDayAtInit(Calendar)} is suggested;
     * or u can still use this method after waiting several millis
     *
     * @param c
     */
    public void selectDay(Calendar c) {
        if (!mMonthPagerAdapter.checkCalendar(c)) {
            Log.w(TAG, "the given Calendar is out of range");
            return;
        }

        navToPage(c);
        handleDayClick(mMonthPagerAdapter.getDayView(c));
    }

    /**
     * this method only works for select Day when CalendarView is just initiating otherwise u should use {@link #selectDay(Calendar)}
     *
     * @param
     */
    public void selectDayAtInit(Calendar c) {
        mSelectedCalendar = c;
        navToPage(mMonthPagerAdapter.getPosition(c));
    }

    /**
     * refresh all the current view
     */
    public void refreshUI() {
        mMonthPagerAdapter.refreshUI();
        mWeekView.refreshUI();
    }

    public void setRange(Calendar start, Calendar end) {
        mMonthPagerAdapter.setRange(start, end);
        if (null != mCurrentMonthStart) navToPage(mCurrentMonthStart);
    }

    public DayViewStyle getDayViewStyle() {
        return mDayViewStyle;
    }

    public void setDayViewStyle(DayViewStyle mDayViewStyle) {
        this.mDayViewStyle = mDayViewStyle;
        refreshUI();
    }

    public WeekViewStyle getWeekViewStyle() {
        return mWeekViewStyle;
    }

    public void setWeekViewStyle(WeekViewStyle mWeekViewStyle) {
        this.mWeekViewStyle = mWeekViewStyle;
        refreshUI();
    }

    public MonthViewStyle getMonthViewStyle() {
        return mMonthViewStyle;
    }

    // TODO: 2016/4/5 animation
    public void setMonthViewStyle(MonthViewStyle mMonthViewStyle) {
        this.mMonthViewStyle = mMonthViewStyle;
        mMonthPagerAdapter = new MonthPagerAdapter(this);
        mMonthViewPager.setAdapter(mMonthPagerAdapter);
        if (null == mSelectedCalendar)
            mCurrentPosition = mMonthPagerAdapter.getPosition(Calendar.getInstance());
        else mCurrentPosition = mMonthPagerAdapter.getPosition(mSelectedCalendar);
        mMonthViewPager.setCurrentItem(mCurrentPosition);
//        if (null != mSelectedCalendar) {
//            mMonthViewPager.setCurrentItem(mMonthPagerAdapter.getPosition(mSelectedCalendar));
//            return;
//        }
//        if (null != mCurrentMonthStart) mMonthViewPager.setCurrentItem(mMonthPagerAdapter.getPosition(mCurrentMonthStart));
//
    }

    public Calendar getSelectedCalendar() {
        return mSelectedCalendar;
    }

    public int getFirstDayOfWeek() {
        return mFirstDayOfWeek;
    }

    /**
     * @param firstDayOfWeek same as setFirstDayOfWeek of Calendar
     */
    public void setFirstDayOfWeek(int firstDayOfWeek) {
        mFirstDayOfWeek = firstDayOfWeek;
        refreshUI();
    }

    public MonthPagerAdapter getMonthPagerAdapter() {
        return mMonthPagerAdapter;
    }

    // TODO: 2016/4/6
    public void startCollapse() {
        collapsing = true;
        MyAnimation animation = new MyAnimation();
        animation.setFillAfter(true);
        startAnimation(animation);

    }

    /**
     * interface for month showing on the screen,return the calendar of month start
     */
    public interface MonthSelectedListener {
        void onMonthSelected(Calendar c);
    }

    public static class SavedState extends BaseSavedState {


        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        WeekViewStyle weekViewStyle;
        DayViewStyle dayViewStyle;
        MonthViewStyle monthViewStyle;
        int firstDayOfWeek;
        int currentPosition;
        Calendar rangeStart;
        Calendar rangeEnd;
        Calendar selectCalendar;

        public SavedState(Parcelable p) {
            super(p);
        }

        protected SavedState(Parcel in) {
            super(in);
            this.weekViewStyle = (WeekViewStyle) in.readSerializable();
            this.dayViewStyle = (DayViewStyle) in.readSerializable();
            this.monthViewStyle = (MonthViewStyle) in.readSerializable();
            this.firstDayOfWeek = in.readInt();
            this.currentPosition = in.readInt();
            this.rangeStart = (Calendar) in.readSerializable();
            this.rangeEnd = (Calendar) in.readSerializable();
            this.selectCalendar = (Calendar) in.readSerializable();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeSerializable(this.weekViewStyle);
            dest.writeSerializable(this.dayViewStyle);
            dest.writeSerializable(this.monthViewStyle);
            dest.writeInt(this.firstDayOfWeek);
            dest.writeInt(this.currentPosition);
            dest.writeSerializable(this.rangeStart);
            dest.writeSerializable(this.rangeEnd);
            dest.writeSerializable(this.selectCalendar);
        }
    }

    class MyAnimation extends Animation {
        int mCollapseHeight;
        int mCalendarHeight;
        ViewGroup.LayoutParams mLayoutParams;

        public MyAnimation() {
            setDuration(5000);

            mCollapseHeight = mMonthViewPager.getMeasuredHeight() - mMonthViewPager.getMeasuredHeight() / 5;
            mLayoutParams = getLayoutParams();
            mCalendarHeight = getMeasuredHeight();
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            mMonthViewPager.setPadding(getPaddingLeft(), (int) (getPaddingTop() - mCollapseHeight * interpolatedTime), getPaddingRight(), getPaddingBottom());
            mLayoutParams.height = (int) (mCalendarHeight - mCollapseHeight * interpolatedTime);
            requestLayout();
        }
    }


}
