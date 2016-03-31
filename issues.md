# 2016.3.24
- ~~when click,size get smaller~~ *2016.3.31*

    ~~because `setBackGround(null)`,may be when set background drawable slide will cause resize~~
    true reason is CalenderView's padding.I used to set the height of TitleView and MonthItem is times of & and the surplus divided to CalenderView's left padding and right padding.Now remove this idea.
    
- ~~store day select state~~ *2016.3.24*

    the cache page is only 3 ,so the stored selected DayView in CalendarView is most likely not the same instance after slide back!!!

# 2016.3.21
- out-of-month day click event
- month collapse to WeekView
- when month view was added to a scrollable view,listen to the scroll state and can smooth collapse to weekview
