package csc301.assignment2;

import java.util.ArrayList;

/**
 * Created by Darren on 10/21/2014.
 */
public class testForDisplay {
    public static void main(String[] args) {
       // CalendarEvent a = new CalendarEvent("assignment1oct",30,9,2014,"14:00","20:00","Test");

        CalendarEvent b = new CalendarEvent("assignment2",29,11,2014,"14:00","20:00","Test1");

        CalendarEvent c = new CalendarEvent("assignment3",27,2,2010,"2:00","20:00","Test2");

        CalendarEvent d = new CalendarEvent("assignment4",12,7,2013,"14:00","20:00","Test3");

        CalendarEvent e = new CalendarEvent("assignment5",29,11,2014,"12:00","20:00","Test4");


        CalendarEvent l = new CalendarEvent("assignment1a",30,9,2014,"14:00","20:00","Test");
        CalendarEvent f = new CalendarEvent("assignment1",31,9,2014,"14:00","20:00","Test");
        CalendarEvent g = new CalendarEvent("assignment1",21,9,2014,"14:00","20:00","Test");
        CalendarEvent h = new CalendarEvent("assignment1",21,9,2014,"14:00","20:00","Test");
        CalendarEvent i = new CalendarEvent("assignment1",29,9,2014,"14:00","20:00","Test");
        CalendarEvent j = new CalendarEvent("assignment1",18,9,2014,"14:00","20:00","Test");
        CalendarEvent k = new CalendarEvent("assignment1",1,9,2014,"14:00","20:00","Test");

        
        
        
        ArrayList<CalendarEvent> listOfEvents = new ArrayList<CalendarEvent>();
      //  listOfEvents.add(a);
        listOfEvents.add(b);
        listOfEvents.add(c);
        listOfEvents.add(d);
        listOfEvents.add(e);

        Display myDisObject = new Display();
        //myDisObject.displayAssignments(listOfEvents);

        
        
        listOfEvents.add(f);
        listOfEvents.add(g);
        listOfEvents.add(h);
        listOfEvents.add(i);
        listOfEvents.add(j);
        listOfEvents.add(k);
        listOfEvents.add(l);
        
        //myDisObject.displayAssignments(listOfEvents);
        myDisObject.displayMonth(10, listOfEvents);
    }

}
