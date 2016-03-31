#Screenshots

<img src="./resources/screen-1.gif" width="30%" height="30%">

#CalendarView
As the CalendarView projects in Github seems to be not so much **customizable**,this CalendaView was born and here is what i achieve.
- basically,`WeekView` (the title)  background color  customizable, `DayView` text   background color customizable
- what exactly important is, DayView **click** click customizable by implenting [DayClickListener](https://github.com/CallMeXYZ/CalendarView/blob/master/library/src/main/java/com/callmexyz/calendarview/dayclicklistener/DayClickListener.java) and calling  `setDayClickListener`. 

  both `onDayClick(DayView view, Calendar c, boolean ifRestoring)` and `onDayUnClick(DayView view, Calendar c)` hold the instance of the specific dayview, so u can custom whatever click effect u want.
  
  [CicleColor](https://github.com/CallMeXYZ/CalendarView/blob/master/library/src/main/java/com/callmexyz/calendarview/dayclicklistener/CilcleColor.java) is a commen click effect as a exmple.So we won't worried about the same issue of cicle size and padding when using most calendar view libraries.
  
  Since that `DayView` is extended from `RelativeLayout`, u can add any view u like to `DayView`
  
# logs 
## 2015.3.31
- rewrite the onDayUnClick mechanism 
- use RelativeLayout instead of TextView as the container of DayView,so that u can add whatever effect u want to the DayView;


#Issues 
# 2016.3.31
- out-of-month day click event
- month collapse to WeekView
- when month view was added to a scrollable view,listen to the scroll state and can smooth collapse to weekview

# 2016.3.24
- ~~when click,size get smaller~~ *2016.3.31*

    ~~because `setBackGround(null)`,may be when set background drawable slide will cause resize~~
    true reason is CalenderView's padding.I used to set the height of TitleView and MonthItem is times of & and the surplus divided to CalenderView's left padding and right padding.Now remove this idea.
    
- ~~store day select state~~ *2016.3.24*

    the cache page is only 3 ,so the stored selected DayView in CalendarView is most likely not the same instance after slide back!!!




