package csc301.assignment2;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.googlecode.lanterna.screen.ScreenCharacterStyle;

/**
 * Created by Darren on 10/21/2014.
 */
public class Display {
    /*pass through an array list of calendarEvents and I will display them properly*/


	EnhancedTerminal t = new EnhancedTerminal();
    public void displayObjects(ArrayList<CalendarEvent> calendarEvents,String type) {

        /*Displays all assignments when past through a list of calendarEvent objects.
        * */
        ArrayList<CalendarEvent> orderedEvents = new ArrayList<CalendarEvent>();
        /* Takes all the assignments and orders them by due dates.
        * */
        for (CalendarEvent objects : calendarEvents) {
            int initalSize = orderedEvents.size();
            for (int i = 0; i < orderedEvents.size(); i++) {

                if (objects.getDateObject().compareTo(orderedEvents.get(i).getDateObject()) < 0) {
                    orderedEvents.add(i, objects);
                    break;
                }

            }
            /*If this is the first time through. i.e nothing to compare too, or if it belongs
            * at the end of the list*/
            if (initalSize == orderedEvents.size()) {
                orderedEvents.add(objects);
            }
        } 
        if (type.equals("assignment")){
        	printEvents(orderedEvents,"assignment");
        }
        else if (type.equals("lecture")){
        	printThisWeek(orderedEvents,"lectures");
        }
       
    }
    
    
    
    public void displayMonth(int month, ArrayList<CalendarEvent> calendarEvents) {
        /*Displays all assignments when past through a list of calendarEvent objects.
        * */
       
    	DisplayCalendar.displayMonth(month, calendarEvents);
    	
    	/*
        Date todaysDate = new Date();
    	
        ArrayList<CalendarEvent> orderedEvents = new ArrayList<CalendarEvent>();
        /* Takes all the assignments and orders them by due dates.
        * 
        for (CalendarEvent objects : calendarEvents) {
            int initalSize = orderedEvents.size();
            for (int i = 0; i < orderedEvents.size(); i++) {

                if ((objects.getDateObject().compareTo(orderedEvents.get(i).getDateObject()) < 0) && (sameMonth(todaysDate,objects.getDateObject()) ==true)) {
                    orderedEvents.add(i, objects);
                    break;
                }

            }
            /*If this is the first time through. i.e nothing to compare too, or if it belongs
            * at the end of the list
            if ((initalSize == orderedEvents.size()) && (sameMonth(todaysDate,objects.getDateObject()) ==true)) {
                orderedEvents.add(objects);
            }
            
        }
        printEvents(orderedEvents,"month");
        */
    }
    
    
    public void displayTimetable(ArrayList <CalendarEvent> calendarEvents){
    	
    	ArrayList<CalendarEvent> orderedEvents = new ArrayList<CalendarEvent>();
    	ArrayList<Integer> indexKeeper = new ArrayList<Integer>();
         /* Takes all the lectures and orders them by due dates.
         * */
        int initalSize = orderedEvents.size();

         for (CalendarEvent objects : calendarEvents) {
             for(int j=0; j<objects.getDay().size(); j++){
            	 int orderedSize = orderedEvents.size();
            	 for (int i = 0; i < orderedSize; i++) {
            		 int day1 = objects.getDay().get(j); 
            		 int day2 = orderedEvents.get(i).getDay().get(indexKeeper.get(i));
            		 if ((day1 <= day2)) {
            			 if (Integer.parseInt((objects.splitStartTimes(objects.getStartTime()))[0]) < (Integer.parseInt(orderedEvents.get(i).splitStartTimes(objects.getStartTime())[0])) || (day1 < day2)){
            				 orderedEvents.add(i, objects);
            				 indexKeeper.add(i,j);
            				 break;
            			 }
            			 else{
            				 orderedEvents.add(i+1,objects);
            				 indexKeeper.add(i+1,j);
            				 break;
            			 }
            		 }
            		 else if (i+1 == orderedEvents.size()){
            			 orderedEvents.add(objects);
            			 indexKeeper.add(j);
            			 break;
            		 }
                	 
            	 }
            	 /*If this is the first time through. i.e nothing to compare too, or if it belongs
                  * at the end of the list*/
        		 if (initalSize == orderedEvents.size()) {
                      orderedEvents.add(objects);
                      indexKeeper.add(0);
                  }
             }
            
             
         }
         printTimetable(orderedEvents,indexKeeper);
    	
    }
    
    

    public boolean sameMonth(Date date1, Date date2)
    {
    	DateFormat dateFormatter= new SimpleDateFormat("MMyy");
    	return dateFormatter.format(date1).equals(dateFormatter.format(date2));
    }
    
    
    
    public void printEvents(ArrayList<CalendarEvent> orderedEvents, String typeofPrint){
    	 String printing ="Your Assignments";
    	 CalendarEvent prev = null;
         SimpleDateFormat titleDateFormat = new SimpleDateFormat("E MMM d y");
         SimpleDateFormat localDateFormat = new SimpleDateFormat("hh:mm a");
         String time;
         String notime = null; 
         String notime2 = null;
         if (orderedEvents.size() > 0){
        	 notime = titleDateFormat.format(orderedEvents.get(0).getDateObject());
        	 notime2 = titleDateFormat.format(orderedEvents.get(0).getDateObject());
         }
         /*After I finish ordering the objects by date I print it out below
         * */
         if (typeofPrint.equals("month")){
        	 printing = "Your Monthly Timetable";
         }else if(typeofPrint.equals("lecture")){
        	 printing = "Your Weekly Lectures";

         }
         t.print(10, EnhancedTerminal.yPosition, printing, EnhancedTerminal.red, EnhancedTerminal.black, ScreenCharacterStyle.Bold);
         for (CalendarEvent objects : orderedEvents) {
              if (prev != null) {
                  notime = titleDateFormat.format(prev.getDateObject());
                  notime2 = titleDateFormat.format(objects.getDateObject());
              }
             if (notime.compareTo(notime2) != 0 || prev == null) {
            	 
                  /* If it's not the first item in the list check if it is the same date as the
                  previous item and if so only print out the assignment and time. Not the date
                   */
              	time = titleDateFormat.format(objects.getDateObject());
             	t.print(10, EnhancedTerminal.yPosition, time, EnhancedTerminal.red, EnhancedTerminal.black, ScreenCharacterStyle.Bold, ScreenCharacterStyle.Underline);

             }
             
             if (objects.getClass() == Assignment.class){
            	 if (((Assignment)objects).getVerified()){
                    	t.print(10, EnhancedTerminal.yPosition, objects.getTitle()+ " (" + ((Assignment)objects).getWeight() + "%" +")" + "  --  "+objects.getStartTime()+"-"+objects.getEndTime(), EnhancedTerminal.green, EnhancedTerminal.black, ScreenCharacterStyle.Bold);
            	 } else {
                    	t.print(10, EnhancedTerminal.yPosition, objects.getTitle()+ " (" + ((Assignment)objects).getWeight() + "%, unverified" +")" + "  --  "+objects.getStartTime()+"-"+objects.getEndTime(), EnhancedTerminal.green, EnhancedTerminal.black, ScreenCharacterStyle.Bold);
            	 }
            	 
             }
             else {
             
            	 t.print(10, EnhancedTerminal.yPosition, objects.getTitle()+ "  --  "+objects.getStartTime()+"-"+objects.getEndTime(), EnhancedTerminal.green, EnhancedTerminal.black, ScreenCharacterStyle.Bold);
             }
             // Update the prev pointer so I can correctly compare if two assignments occur on the same date.
             prev = objects;
         }
    	
    }
    
    
    public void printThisWeek(ArrayList<CalendarEvent> orderedEvents, String typeofPrint){
    
    	int pos[] = {0,  EnhancedTerminal.yPosition};
    	int pos_current[] = {0,  EnhancedTerminal.yPosition};

    	String[] DaysofWeek = {"Monday   ", "Tuesday  ","Wednesday","Thursday ","Friday   "};

        String[] startTime = {"09:00","10:00","11:00","12:00","13:00","14:00","15:00","16:00","17:00","18:00","19:00"};
        
        for (String str : DaysofWeek){
            pos_current[0]+=10 + 9;
            t.print(pos_current[0], pos_current[1],str, EnhancedTerminal.red, EnhancedTerminal.black, ScreenCharacterStyle.Bold);
        }
        for (String str : startTime){
            pos_current[1]+=1;
            t.print(0, pos_current[1],str, EnhancedTerminal.red, EnhancedTerminal.black, ScreenCharacterStyle.Bold);
        }
        pos[1] += 1;
        
        for (CalendarEvent e : orderedEvents){
        	for(int day : e.getDay()){
        		day+=1;
                t.print(pos[0]+19*day, -9 + pos[1]+Integer.parseInt(e.getStartTime().substring(0,2)),"|", EnhancedTerminal.red, EnhancedTerminal.black, ScreenCharacterStyle.Bold);
                t.print(pos[0]+19*day + 1, -9 + pos[1]+Integer.parseInt(e.getStartTime().substring(0,2)),e.getTitle().substring(0, Math.min(e.getTitle().length(), 9)), EnhancedTerminal.red, EnhancedTerminal.black, ScreenCharacterStyle.Bold);
                t.print(pos[0]+19*day + 10, -9 + pos[1]+Integer.parseInt(e.getStartTime().substring(0,2)),"|", EnhancedTerminal.red, EnhancedTerminal.black, ScreenCharacterStyle.Bold);
                int y = Math.min(Integer.parseInt(e.getEndTime().substring(0,2)), 19);
                int x =  y - (Integer.parseInt(e.getStartTime().substring(0,2))+1);
                for(;x> 0;x--){
                    t.print(pos[0]+19*day, -9 + pos[1]+Integer.parseInt(e.getStartTime().substring(0,2)) + x,"|", EnhancedTerminal.red, EnhancedTerminal.black, ScreenCharacterStyle.Bold);
                    t.print(pos[0]+19*day+ 10, -9 + pos[1]+Integer.parseInt(e.getStartTime().substring(0,2)) + x,"|", EnhancedTerminal.red, EnhancedTerminal.black, ScreenCharacterStyle.Bold);
                }
        	}	
        }
        EnhancedTerminal.yPosition = pos_current[1];
    }
    private String spaces(String titles,String input, int day){
    	int num = (12+day-titles.length());
    	String returned ="";
    	for (int k = 0; k < num; k++){
    		returned += input;
    	}
    	return returned;
    }
    
    public void printTimetable(ArrayList<CalendarEvent> orderedEvents, ArrayList<Integer> indexKeeper){
   	 String printing ="Your Timetable";
   	 CalendarEvent prev = null;
        String time;
        ArrayList<String> daysOfWeek= new ArrayList<String>();
        daysOfWeek.add("Monday");
        daysOfWeek.add("Tuesday");
        daysOfWeek.add("Wednesday");
        daysOfWeek.add("Thursday");
        daysOfWeek.add("Friday");
        daysOfWeek.add("Saturday");
        daysOfWeek.add("Sunday");

        int index1 =0;
        /*After I finish ordering the objects by date I print it out below
        * */
        t.print(10, EnhancedTerminal.yPosition, printing, EnhancedTerminal.red, EnhancedTerminal.black, ScreenCharacterStyle.Bold);
        
        for (CalendarEvent objects : orderedEvents) {
             if (prev == null){
                 /*If it's the first item were looking at print out the Date it's due
                 * */
                time = (daysOfWeek.get(objects.getDay().get(indexKeeper.get(index1))));
                t.print(10, EnhancedTerminal.yPosition, time, EnhancedTerminal.red, EnhancedTerminal.black, ScreenCharacterStyle.Underline, ScreenCharacterStyle.Bold);
                
                t.print(10, EnhancedTerminal.yPosition, objects.getTitle()+ "  --  "+objects.getStartTime()+"-"+objects.getEndTime(), EnhancedTerminal.green, EnhancedTerminal.black, ScreenCharacterStyle.Bold);
            }
            else if (objects.getDay().get(indexKeeper.get(index1)) == (prev.getDay().get(indexKeeper.get(index1-1)))) {
                 /* If it's not the first item in the list check if it is the same date as the
                 previous item and if so only print out the assignment and time. Not the date
                  */
            	t.print(10, EnhancedTerminal.yPosition, objects.getTitle()+ "  --  "+objects.getStartTime()+"-"+objects.getEndTime(), EnhancedTerminal.green, EnhancedTerminal.black, ScreenCharacterStyle.Bold);
           }
            else{
                 /*If this assignment is due on a different date print the date above followed by the assignment
                  */
                time = (daysOfWeek.get(objects.getDay().get(indexKeeper.get(index1))));
            	t.print(10, EnhancedTerminal.yPosition, time, EnhancedTerminal.red, EnhancedTerminal.black, ScreenCharacterStyle.Bold, ScreenCharacterStyle.Underline);

            	t.print(10, EnhancedTerminal.yPosition, objects.getTitle()+ "  --  "+objects.getStartTime()+"-"+objects.getEndTime(), EnhancedTerminal.green, EnhancedTerminal.black, ScreenCharacterStyle.Bold);
           }
            // Update the prev pointer so I can correctly compare if two assignments occur on the same date.
            prev = objects;
            index1 += 1;
        }
   	
   }


}

