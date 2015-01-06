package csc301.assignment2;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * This class prints out the month view of a particular month, and the
 * events due in that month.
 * 
 * @author Shameel Khan (khansh51)
 *
 */

public class DisplayCalendar {
	
	private static int x = 5;
	private static EnhancedTerminal eTerminal = new EnhancedTerminal();
	
	
	public static void displayMonth(int month, ArrayList<CalendarEvent> events) {
		
		// code from: http://stackoverflow.com/questions/8997228/how-to-get-the-first-day-of-a-month
		// Gets the day the month starts with (i.e: Monday, Tuesday...)
		Calendar cal = Calendar.getInstance();
	    cal.set(Calendar.DATE, 1);
	    cal.set(Calendar.MONTH, month);
	    cal.set(Calendar.YEAR, 2014);
	    cal.set(Calendar.DAY_OF_MONTH, 1);
	    Date firstDayOfMonth = cal.getTime();  

	    DateFormat sdf = new SimpleDateFormat("EEEEEEEE");
	    String firstDay = sdf.format(firstDayOfMonth);
	    
	    printMonth(month, firstDay, events);
	}
	
	public static void printMonth(int month, String firstDay, ArrayList<CalendarEvent> events) {
		int screenWidth = 92;
		int widthDiff = 12;
		
		eTerminal.print(screenWidth/2, EnhancedTerminal.yPosition, getMonthString(month), 
				EnhancedTerminal.green, EnhancedTerminal.black, EnhancedTerminal.bold, EnhancedTerminal.underline);
		eTerminal.print(x, EnhancedTerminal.yPosition, "", EnhancedTerminal.green, EnhancedTerminal.black);
		
		printChar("-", screenWidth);
		printDays(firstDay, widthDiff);
		printChar("-", screenWidth);		
		int count = 1, space = 8;
		
		ArrayList<CalendarEvent> eventsInDay = new ArrayList<CalendarEvent>();
		boolean inDay = false;
		String spaces = new String(new char[space/2]).replace("\0", " ");
		
		for(int i = 1; i <= 31; i++) {						
			
			if(count++ > 7) {
				
				eTerminal.print(x, EnhancedTerminal.yPosition, "", EnhancedTerminal.white, EnhancedTerminal.black);
				printChar("-", screenWidth);
				
				eTerminal.printg(x, EnhancedTerminal.yPosition, "|", EnhancedTerminal.white,
						EnhancedTerminal.black, EnhancedTerminal.bold);
				eTerminal.printg(x, EnhancedTerminal.yPosition, spaces, EnhancedTerminal.white,
						EnhancedTerminal.black, EnhancedTerminal.bold);
				
				count = 2;
			}
			
			if(i == 1) {
				eTerminal.printg(x, EnhancedTerminal.yPosition, "|", EnhancedTerminal.white,
						EnhancedTerminal.black, EnhancedTerminal.bold);
				eTerminal.printg(x, EnhancedTerminal.yPosition, spaces, EnhancedTerminal.white,
						EnhancedTerminal.black, EnhancedTerminal.bold);
			}
			
			//Remember 10 means November not October 
			inDay = getEventsWithThisDay(i, month, 2014, events, eventsInDay);			
			
			if(i >= 10) {
				if(inDay)
					eTerminal.printg(x, EnhancedTerminal.yPosition, "[" + i + "]", EnhancedTerminal.cyan,
							EnhancedTerminal.black, EnhancedTerminal.bold);
				else
					eTerminal.printg(x, EnhancedTerminal.yPosition, " " + i + " ", EnhancedTerminal.white,
							EnhancedTerminal.black, EnhancedTerminal.bold);					
			}
			else {
				if(inDay)
					eTerminal.printg(x, EnhancedTerminal.yPosition, "[" + i + "] ", EnhancedTerminal.cyan,
							EnhancedTerminal.black, EnhancedTerminal.bold);
				else
					eTerminal.printg(x, EnhancedTerminal.yPosition, "  " + i + " ", EnhancedTerminal.white,
							EnhancedTerminal.black, EnhancedTerminal.bold);	
			}
			
			eTerminal.printg(x, EnhancedTerminal.yPosition, spaces, EnhancedTerminal.white,
					EnhancedTerminal.black, EnhancedTerminal.bold);
			eTerminal.printg(x, EnhancedTerminal.yPosition, "|", EnhancedTerminal.white,
					EnhancedTerminal.black, EnhancedTerminal.bold);
			eTerminal.printg(x, EnhancedTerminal.yPosition, spaces, EnhancedTerminal.white,
					EnhancedTerminal.black, EnhancedTerminal.bold);
		}
		
		eTerminal.print(x, EnhancedTerminal.yPosition, "", EnhancedTerminal.white, EnhancedTerminal.black);

		printChar("-", 40);
		eTerminal.print(x, EnhancedTerminal.yPosition, "", EnhancedTerminal.white, EnhancedTerminal.black);
			
		eTerminal.print(x, EnhancedTerminal.yPosition, "~Event Listings~", EnhancedTerminal.green, 
				EnhancedTerminal.black, EnhancedTerminal.bold, EnhancedTerminal.underline);
		
		eTerminal.print(x, EnhancedTerminal.yPosition, "", EnhancedTerminal.white, EnhancedTerminal.black);
		
		for(CalendarEvent event : eventsInDay) {
			eTerminal.print(x, EnhancedTerminal.yPosition, eventToString(event),
					EnhancedTerminal.white, EnhancedTerminal.black, EnhancedTerminal.bold);
		}
		
	}
	
	// Given a day (i.e 12th = 12) all the user events and a list of user events to display at the end
	// (eventsInDay), add to eventsInDay if that day can be found in those events.
	public static boolean getEventsWithThisDay(int day, int month, int year, ArrayList<CalendarEvent> events, ArrayList<CalendarEvent> eventsInDay) {
		boolean added = false;
		int eventDay = 0, eventMonth, eventYear = 0;
		for(CalendarEvent event : events) {
			eventDay = event.getDay().get(0);
			eventMonth = event.getMonth().get(0);
			eventYear = event.getYear();
			
			if(day == eventDay && 
			   month == eventMonth && 
			   year == eventYear) {
				eventsInDay.add(event);
				added = true;
			}
		}
				
		return added;
	}
	
	public static void printChar(String c, int times) {
		for(int i = 0; i < times; i++) {
			eTerminal.printg(x, EnhancedTerminal.yPosition, c, 
					EnhancedTerminal.white, EnhancedTerminal.black, EnhancedTerminal.bold);
		}
		eTerminal.print(x, EnhancedTerminal.yPosition, "", EnhancedTerminal.white, EnhancedTerminal.black);
	}
	
	public static void printDays(String from, int widthDiff) {
		ArrayList<String> daysList = new ArrayList<String>();
		daysList.add("Monday");
		daysList.add("Tuesday");
		daysList.add("Wednesday");
		daysList.add("Thursday");
		daysList.add("Friday");
		daysList.add("Saturday");
		daysList.add("Sunday");
		int index = daysList.indexOf(from);
		int done = 0, wordSpaces = 0;
		String spaces = "", dayToPrint = "";
		
		for(int i = index; i < daysList.size(); i++) {			
			dayToPrint = daysList.get(i);
			eTerminal.printg(x, EnhancedTerminal.yPosition, "|", eTerminal.white, eTerminal.black, eTerminal.bold);
			
			wordSpaces = widthDiff - dayToPrint.length();
			spaces = new String(new char[wordSpaces/2]).replace("\0", " ");
			eTerminal.printg(x, EnhancedTerminal.yPosition, spaces, eTerminal.white, eTerminal.black, eTerminal.bold);
			eTerminal.printg(x, EnhancedTerminal.yPosition, dayToPrint, eTerminal.green, eTerminal.black, eTerminal.bold);
			eTerminal.printg(x, EnhancedTerminal.yPosition, spaces, eTerminal.white, eTerminal.black, eTerminal.bold);
			done++;
		}
		
		int leftOver = daysList.size() - done;
		for(int i = 0; i < leftOver; i++) {
			dayToPrint = daysList.get(i);
			eTerminal.printg(x, EnhancedTerminal.yPosition, "|", eTerminal.white, eTerminal.black, eTerminal.bold);
			
			wordSpaces = widthDiff - dayToPrint.length();
			spaces = new String(new char[wordSpaces/2]).replace("\0", " ");
								
			eTerminal.printg(x, EnhancedTerminal.yPosition, spaces, eTerminal.white, eTerminal.black, eTerminal.bold);
			eTerminal.printg(x, EnhancedTerminal.yPosition, dayToPrint, eTerminal.green, eTerminal.black, eTerminal.bold);
			eTerminal.printg(x, EnhancedTerminal.yPosition, spaces, eTerminal.white, eTerminal.black, eTerminal.bold);		
			
			if(dayToPrint.length() % 2 != 0)
				eTerminal.printg(x, EnhancedTerminal.yPosition, " ", eTerminal.white, eTerminal.black, eTerminal.bold);
		}
		
		eTerminal.printg(x, EnhancedTerminal.yPosition, "|", eTerminal.white, eTerminal.black, eTerminal.bold);
		eTerminal.print(x, EnhancedTerminal.yPosition, "", EnhancedTerminal.white, EnhancedTerminal.black);
	}
	
	public static String getMonthString(int month) {
		if(month == 0)
			return "January";
		else if (month == 1)
			return "February";
		else if (month == 2)
			return "March";
		else if (month == 3)
			return "April";
		else if (month == 4)
			return "May";
		else if (month == 5)
			return "June";
		else if (month == 6)
			return "July";
		else if (month == 7)
			return "August";
		else if (month == 8)
			return "September";
		else if (month == 9)
			return "October";
		else if (month == 10)
			return "November";
		else if (month == 11)
			return "December";
					
		return "";
	}
	
	public static String eventToString(CalendarEvent event) {
		String eventString = "";
		
		eventString += "["+ event.getDay().get(0) + "] : ";
		eventString += event.getTitle();
		eventString += " Starts From: " + event.getStartTime() + " Ends At: " + event.getEndTime();
		return eventString;
	}

}
