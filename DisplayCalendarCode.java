import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Simple calendar viewing class. The purpose is to provide a more 
 * calendar-like experience. So the calendar is printed out depending on which
 * month your viewing. Those days such have events due on them (such as assignments and/or personal)
 * are represented by [day]. At the bottom the the calendar we list all events due in the month.
 * 
 * This provided user with an easy experience. They just through the calendar until they spot the [day],
 * and look at the bottom to see what events are due on that day.
 * 
 * This is a much better solution then just listing the events of a month.
 * 
 * 
 * Now this code is using System.out.println / System.out.print. The task is to transform this code
 * to use the EnhancedTerminal. I have provided methods print() and printg. 
 * 
 * Second the code is a bit specific, such as I have hard coded the year to 2014 and month to nov. Also
 * I treat events as a string.
 * 
 * Overall there is still good amount of work need to make this work with enhanced terminal. But the general
 * idea and presentation is the key.
 * 
 * Feel free to change up the code.
 * @author Shameel Khan (khansh51)
 *
 */
public class MonthDisplay {

	public static void main (String[] args) {
		 
		ArrayList<String> events = new ArrayList<String>();
		events.add("1, 10, 2014, Assignment 1 for CSC301, 10:00, 11:00");
		events.add("1, 10, 2014, Assignment 1 for CSC207, 02:00, 12:00");
		events.add("25, 10, 2014, Party At My Place, 09:00, 12:00");
		events.add("10, 10, 2014, Exam for Pol112, 09:00, 12:00");
		events.add("25, 11, 2014, Party At My Place, 09:00, 12:00"); // this will not get printed
		displayMonth(10, events);
	}
	
	// events here are just in string form: day, month, year title, from_time, to_time
	public static void displayMonth(int month, ArrayList<String> events) {
		
		// code from: http://stackoverflow.com/questions/8997228/how-to-get-the-first-day-of-a-month
		Calendar cal = Calendar.getInstance();
	    cal.set(Calendar.DATE, 1);
	    cal.set(Calendar.MONTH, month);
	    cal.set(Calendar.YEAR, 2014);
	    cal.set(Calendar.DAY_OF_MONTH, 1);
	    Date firstDayOfMonth = cal.getTime();  

	    DateFormat sdf = new SimpleDateFormat("EEEEEEEE");
	    String firstDay = sdf.format(firstDayOfMonth);
	    
	    printMonth(firstDay, events);
		
	}
	
	public static void printMonth(String firstDay, ArrayList<String> events) {
		int screenWidth = 125;
		int widthDiff = 50/7;
		printChar("-", screenWidth);
		printDays(firstDay, widthDiff);
		printChar("-", screenWidth);		
		int count = 1, space = 14;
		
		ArrayList<String> eventsInDay = new ArrayList<String>();
		boolean inDay = false;
		
		for(int i = 1; i <= 31; i++) {
			if(i >= 10)
				space = 13;
			if(count++ <= 7) {
				
				//Remember 10 means November not October 
				inDay = getEventsWithThisDay(i, 10, 2014, events, eventsInDay);
				if(inDay)
					System.out.print("[" + i + "]");
				else
					System.out.print(" " + i + " ");
				String spaces = new String(new char[space/2]).replace("\0", " ");
				System.out.print(spaces);
				System.out.print("|");				
				System.out.print(spaces);

			}
			else {
				System.out.println();
				printChar("-", screenWidth);
				
				inDay = getEventsWithThisDay(i, 10, 2014, events, eventsInDay);
				if(inDay)
					System.out.print("[" + i + "]");
				else
					System.out.print(" " + i + " ");
				
				String spaces = new String(new char[space/2]).replace("\0", " ");
				System.out.print(spaces);
				System.out.print("|");				
				System.out.print(spaces);
				count = 2;
			}
		}
		System.out.println();
		printChar("-", screenWidth);
		System.out.println();
		System.out.println();
		

		for(String event : eventsInDay) {
			System.out.println("- " + event);
		}
		
	}
	
	// Given a day (i.e 12th = 12) all the user events and a list of user events to display at the end
	// (eventsInDay), add to eventsInDay if that day can be found in those events.
	public static boolean getEventsWithThisDay(int day, int month, int year, ArrayList<String> events, ArrayList<String> eventsInDay) {
		String[] parse = null;
		boolean added = false;
		for(String event : events) {
			parse = event.split(",");
			
			if(day == Integer.parseInt(parse[0].trim()) && 
			   10 == Integer.parseInt(parse[1].trim()) && 
			   2014 == Integer.parseInt(parse[2].trim())) {
				eventsInDay.add(event);
				added = true;
			}
		}
				
		return added;
	}
	
	public static void printChar(String c, int times) {
		for(int i = 0; i < times; i++) {
			System.out.print(c);
		}
		System.out.println();
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
		int done = 0;
		
		
		for(int i = index; i < daysList.size(); i++) {
			System.out.print(daysList.get(i));
			String spaces = new String(new char[widthDiff]).replace("\0", " ");
			System.out.print(spaces);
			done++;
		}
		
		int leftOver = daysList.size() - done;
		for(int i = 0; i < leftOver; i++) {
			System.out.print(daysList.get(i));
			String spaces = new String(new char[widthDiff]).replace("\0", "  ");
			System.out.print(spaces);
		}
		
		System.out.println();
	}

}
