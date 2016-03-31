#Screenshot
<img src="./resources/screen-1.gif" width="30%" height="30%">
As the CalendarView projects in Github seems to be not so much **customizable**,this CalendaView was born and here is what i achieve.
- basically,`WeekView` (the title)  background color  customizable, `DayView` text   background color customizable
- what exactly important is, DayView **click** click customizable by implenting [DayClickListener](https://github.com/CallMeXYZ/CalendarView/blob/master/library/src/main/java/com/callmexyz/calendarview/dayclicklistener/DayClickListener.java) and calling  `setDayClickListener`. 

  both `onDayClick(DayView view, Calendar c, boolean ifRestoring)` and `onDayUnClick(DayView view, Calendar c)` hold the instance of the specific dayview, so u can custom whatever click effect u want.
  
  [CicleColor](https://github.com/CallMeXYZ/CalendarView/blob/master/library/src/main/java/com/callmexyz/calendarview/dayclicklistener/CilcleColor.java) is a commen click effect as a exmple.So we won't worried about the same issue of cicle size and padding when using most calendar view libraries.
  
  Since that `DayView` is extended from `RelativeLayout`, u can add any view u like to `DayView`

--
issues [here](https://github.com/CallMeXYZ/CalendarView/blob/master/issues.md)
and logs [here](https://github.com/CallMeXYZ/CalendarView/blob/master/logs.md)
