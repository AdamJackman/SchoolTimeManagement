package csc301.assignment2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/*
 * A CalendarEvent is any event that can be scheduled in a calendar. The main 
 * purpose for any CalendarEvent object is to store information about its self.  
 */
public class CalendarEvent {
	private ArrayList<Integer> day;
	private ArrayList<Integer> month;
	private int year;
	private String title;
	private String startTime;
	private String endTime;
	private String description;
	
	/*
	 * This private constructor restricts any other class from creating garbage CalendarEvent 
	 * objects (CalendarEvents without start and end times). 
	 */
	private CalendarEvent(String title, int year, String startTime, String endTime, String description) {
		this.title = title;
		this.year = year;
		this.startTime = startTime;
		this.endTime = endTime;
		this.description = description;
	}
	
	/*
	 *  This constructor creates events that occurs on multiple days and in multiple month using
	 *  an ArrayList of days and months, rather than one int representing one day and one month.
	 */
	public CalendarEvent(String title, ArrayList<Integer> day, ArrayList<Integer> month, int year, String startTime, String endTime, String description) {
		this(title, year, startTime, endTime, description);		
		this.day = day;
		this.month = month;
	}	
	
	/*
	 *  This constructor creates events that occur on one day and in many month using
	 *  an int for the day and an ArrayList for the months, rather than an ArrayList of
	 *  int representing many days and one int representing one month.
	 */
	public CalendarEvent(String title, int day, ArrayList<Integer> month, int year, String startTime, String endTime, String description) {
		this(title, year, startTime, endTime, description);
		this.day = new ArrayList<Integer>();
		this.day.add(day);
		this.month = month;
	}	
	
	/*
	 *  This constructor creates an event that happens only once using ints to represent a day and a month.
	 */
	public CalendarEvent(String title, int day, int month, int year, String startTime, String endTime, String description) {
		this(title, year, startTime, endTime, description);
		this.day = new ArrayList<Integer>();
		this.month = new ArrayList<Integer>();
		this.day.add(day);
		this.month.add(month);
	}	
	
	public String getTitle() {
		return title;
	}

	public ArrayList<Integer> getDay() {
		return day;
	}

	public ArrayList<Integer> getMonth() {
		return month;
	}

	public int getYear() {
		return year;
	}
	
	public String getStartTime() {
		return startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public String getDescription() {
		return description;
	}
	
	// 09/09/14
	
	public String getFullDate() {
		return getDay().get(0) + "/" + getMonth().get(0) + "/" + getYear();
	}
	
	/*
	 * The user needs to enter a start time with the format of HH:mm as a string.
     	* This method splits the string into two ints in order to display the date properly.
     	*/
    	public String[] splitStartTimes (String startTime) {
		return startTime.split(":");
    	}

    	/*
     	* Need a date object in order to compare if an assignment due date occurs before or after 
     	* another assignment. Returns a Date object corresponding to the data in this.
     	*/
    	public Date getDateObject() {
		Calendar calendar = Calendar.getInstance();
		String[] time = splitStartTimes(this.getStartTime());
		calendar.set(this.year, this.month.get(0), this.day.get(0), Integer.parseInt(time[0]), Integer.parseInt(time[1]), 0);        
		return calendar.getTime();
    	}
}
