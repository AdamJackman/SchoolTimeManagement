package csc301.assignment2;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.googlecode.lanterna.screen.ScreenCharacterStyle;

/**
 * Interpreter is responsible for interpreting user input, and 
 * taking actions accordingly. Furthermore, Interpreter will ask
 * user for additional information when required, and interpret the
 * given information.
 * 
 * Additionally interpreter will check if provided information is correct
 * and follows the specified format.
 * 
 * @author Shameel Khan
 *
 */
public class Interpreter {
	
	private EnhancedTerminal terminal = new EnhancedTerminal();
	
	public void interpret (String userInput) throws InterruptedException, FileNotFoundException
	{
		CalendarDatabase calDatabase = new CalendarDatabase();
		Display display = new Display();
		String input;
		int yPosition = EnhancedTerminal.yPosition;
		int x = 5;
		String message;
		
		// check what the user would like to add
		if(userInput.equals("add")){
			EnhancedTerminal.currentCommandLevel = "add";
			message = "What would you like to add (course, assignment, lecture, personal): ";
			terminal.print(x, yPosition, message, terminal.cyan, terminal.black, ScreenCharacterStyle.Bold);
			
			input = terminal.getInput(message.length() + x, EnhancedTerminal.yPosition - 1);
			input = input.trim();
			
			if(input.toLowerCase().equals("course")){
				makeCourse();
			}
			else if(input.toLowerCase().equals("assignment")){
				makeAssignment();
			}
			else if(input.toLowerCase().equals("personal")){
				makePersonalEvent();
			}
			else if(input.toLowerCase().equals("lecture")){
				makeLecture();
			}
		}
		//check what the user will like to remove
		else if (userInput.equals("remove")) {
			EnhancedTerminal.currentCommandLevel = "remove";
			message = "What would you like to remove (course, lecture, personal): ";
			terminal.print(x, yPosition, message, terminal.cyan, terminal.black, ScreenCharacterStyle.Bold);
			
			input = terminal.getInput(message.length() + x, EnhancedTerminal.yPosition - 1);
			input = input.trim();
			
			if(input.toLowerCase().equals("course")){
				removeCourse();
			}
			else if(input.toLowerCase().equals("personal")){
				EnhancedTerminal.currentCommandLevel = "remove personal";
				removePersonalEvent();
			}
			else if(input.toLowerCase().equals("lecture")){
				removeLecture();
			}
		}
		// check what the user will like to view
		else if(userInput.equals("show")){
			EnhancedTerminal.currentCommandLevel = "show";
			message = "What would you like to view (assignments, lectures, month): ";
			terminal.print(x, yPosition, message, terminal.cyan, terminal.black, ScreenCharacterStyle.Bold);
			
			input = terminal.getInput(message.length() + x, EnhancedTerminal.yPosition - 1);
			input = input.trim();
			
			if(input.toLowerCase().equals("assignments")){ 
				String[] type = {"assignment"};

				ArrayList<CalendarEvent> assignments = calDatabase.getEvent(type, TimetableSystem.student);
				display.displayObjects(assignments,"assignment");

			}
			else if(input.toLowerCase().equals("month")){
				String[] type = {"assignment", "personal"};
				ArrayList<CalendarEvent> events = calDatabase.getEvent(type, TimetableSystem.student);
				//display.displayMonth(10, events);
				int monthIndex = 10;
				String responce;
				EnhancedTerminal.returnMode = true;
				// to print the month on a new page
				terminal.print(0, terminal.getHeight(), "", EnhancedTerminal.white, EnhancedTerminal.black, EnhancedTerminal.bold);
				while(true) {
					// print to clear everything
					for(int i = 2; i <= 30; i++) {
						terminal.print(0, i, "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t", EnhancedTerminal.white, EnhancedTerminal.black);
					}
					
					EnhancedTerminal.yPosition = 2;
					display.displayMonth(monthIndex, events);
					
					terminal.print(5, EnhancedTerminal.yPosition,
							"Please press [Right] or [Left] arrow keys to see different months. Press Enter to return.", 
							EnhancedTerminal.green, EnhancedTerminal.black, EnhancedTerminal.bold);
					
					responce = terminal.getInput(0, -1);
					if (responce.equals("ArrowRight")) {
						if(monthIndex < 11)
							monthIndex++;
					}
					else if (responce.equals("ArrowLeft")) {
						if(monthIndex > 0) 
							monthIndex--;
					}
					else
						break;
					
					if(!EnhancedTerminal.returnMode)
						break;
				}
				
				// to print the month on a new page
				terminal.print(0, terminal.getHeight(), "", EnhancedTerminal.white, EnhancedTerminal.black, EnhancedTerminal.bold);
				
				EnhancedTerminal.returnMode = false;
				
			}else if(input.toLowerCase().equals("lectures")){
				String[] type = {"lecture"};
				ArrayList<CalendarEvent> lectures = calDatabase.getEvent(type, TimetableSystem.student);
				display.displayObjects(lectures,"lecture");
			}
			
		}
		else if (userInput.equals("change")) {
			change();
		}
		else if(userInput.equals("logout")) {
			EnhancedTerminal.currentCommandLevel = "1";
			terminal.print(5, yPosition, "You have been logged out", terminal.red, terminal.black,
					ScreenCharacterStyle.Bold);
			TimetableSystem.loggedIn = false;
		}
		else if(userInput.equals("exit")){
			terminal.print(5, yPosition, "Exit", terminal.red, terminal.black,
					ScreenCharacterStyle.Bold);
			TimetableSystem.isRunning = false;
		}		
		
		terminal.printDivider("-+");
	}
	
	/**
	 * Given a message ask user for the response of that message and return
	 * user response.
	 * @param message
	 * @return
	 * @throws InterruptedException
	 */
	public String getUserInput(String message) throws InterruptedException
	{
		int x = 5;
		// Get title for assignment
		terminal.print(x, EnhancedTerminal.yPosition, message, terminal.cyan, terminal.black, ScreenCharacterStyle.Bold);
		String input = clean(terminal.getInput(message.length() + x, EnhancedTerminal.yPosition - 1));
		return input;
	}
	
	/**
	 * Asks user for information about the assignment they want to create. Then calls the
	 * addAssignment() method inside CalendarFileSystem to add that assignment.
	 * 
	 * @throws InterruptedException
	 */
	public void makeAssignment() throws InterruptedException
	{
		int x = 5;
		
		// get course code for assignment		
		String courseCode = getCorrectInput("Provide course code for assignment: ");		
		if(courseCode.toLowerCase().equals("quit")) return;
		// Get title for assignment		
		String title = getCorrectInput("Provide assignment title: ");		
		if(title.toLowerCase().equals("quit")) return;
		// Get date assignment due on		
		String date = getCorrectInput("Provide assignment due date in correct Format: 00 00 0000 (day month year): ");	
		if(date.toLowerCase().equals("quit")) return;
		// Get startTime and endtime for assignment		
		String time = getCorrectInput("What time does the assignment start and end? Format 00:00 00:00 (start-time end-time): ");
		if(time.toLowerCase().equals("quit")) return;
		// Get description for assignment		
		String description = getUserInput("Provide assignment description: ");	
		if(description.toLowerCase().equals("quit")) return;
		// Get weight for assignment		
		String weight = getCorrectInput("What is the weight of the assignment?:  ");
		if(weight.toLowerCase().equals("quit")) return;
		
		String[] parseDate = date.split(" ");
		String[] parseTime = time.split(" ");
		
		// Start the checks
		
		if(!checkDate(date)){
	      	terminal.print(x, EnhancedTerminal.yPosition, "Error with provided date 00 00 00", terminal.red, terminal.black, ScreenCharacterStyle.Bold);
			return;
		}
		
		if(parseTime.length != 2 || !checkTimeFormat(parseTime[0]) || !checkTimeFormat(parseTime[1])){
	      	terminal.print(x, EnhancedTerminal.yPosition, "Error with provided Start or End Time", terminal.red, terminal.black, ScreenCharacterStyle.Bold);
			return;
	    }		
		
		if(!checkWeight(weight)){
	      	terminal.print(x, EnhancedTerminal.yPosition, "Error with provided weight", terminal.red, terminal.black, ScreenCharacterStyle.Bold);
			return;
		}
		
		Assignment assignment = new Assignment(title, Integer.parseInt(parseDate[0]), Integer.parseInt(parseDate[1])-1, 
				Integer.parseInt(parseDate[2]), parseTime[0], parseTime[1], description, Integer.parseInt(weight));
		
		CalendarDAO calDAO = new CalendarDatabase();
		calDAO.addAssignment(assignment, new Course(courseCode));				
	}
	
	
	/**
	 * Asks user for information about the personal event they want to create. Then calls the
	 * addAssignment() method inside CalendarFileSystem to add that personal event.
	 * 
	 * @throws InterruptedException
	 */
	public void makePersonalEvent() throws InterruptedException
	{
		int yPosition = EnhancedTerminal.yPosition + 1;		
		int x = 5;	
		
		// Get title for event		
		String title = getCorrectInput("Please provide event title: ");	
		if(title.toLowerCase().equals("quit")) return;
		// Get date event due on		
		String date = getCorrectInput("Please provide event date, Format: 00 00 0000 (day month year): ");		
		if(date.toLowerCase().equals("quit")) return;
		// Get startTime and endtime for event		
		String time = getCorrectInput("What time does the event start and end? Format: 00:00 00:00 (start-time end-time): ");
		if(time.toLowerCase().equals("quit")) return;
		// Get description for event		
		String description = getUserInput("Please provide event description: ");
		if(description.toLowerCase().equals("quit")) return;
		
		String[] parseDate = date.split(" ");
		String[] parseTime = time.split(" ");
		
		if(!checkDate(date)){
			yPosition = EnhancedTerminal.yPosition +1;
	      	terminal.print(x, yPosition, "Error with provided date 00 00 0000", terminal.red, terminal.black, ScreenCharacterStyle.Bold);
			return;
		}
		
		if(parseTime.length != 2 || !checkTimeFormat(parseTime[0]) || !checkTimeFormat(parseTime[1])){
	      	yPosition = EnhancedTerminal.yPosition +1;
	      	terminal.print(x, yPosition, "Error with provided Start or End Time", terminal.red, terminal.black, ScreenCharacterStyle.Bold);
			return;
	    }		
		
		CalendarEvent personalEvent = new CalendarEvent(title, Integer.parseInt(parseDate[0]), 
				Integer.parseInt(parseDate[1])-1, Integer.parseInt(parseDate[2]), parseTime[0], parseTime[1], description);
	
		CalendarDAO calDAO = new CalendarDatabase();
		calDAO.addEvent(personalEvent, TimetableSystem.student);		
	}
	
	
	/**
	 * Ask user for course code in order to make a new course by using CourseDAO.
	 * 
	 * @throws InterruptedException
	 */
	public void makeCourse() throws InterruptedException
	{
		// get course code for course		
		String courseCode = getCorrectInput("Provide the course code to be added: ");	

		if(courseCode.toLowerCase().equals("quit")) return;
        CourseDAO courseFileSystem = new CourseDatabaseDAO();
		courseFileSystem.addCourse(TimetableSystem.student, courseCode.toLowerCase());
	}
	
		
	/**
	 * Ask user for input in order to make a new lecture by using CalendarDAO
	 * 
	 * @throws InterruptedException
	 */
	public void makeLecture() throws InterruptedException
	{	
		int x = 5;	
		String courseCode;
		// Get courseCode for lecture			
		courseCode = getCorrectInput("Please provide course code for lecture: ");
		if(courseCode.toLowerCase().equals("quit")) return;
		
		
		CalendarDAO calDatabase = new CalendarDatabase();
		
		ArrayList<Lecture> courseLectures = calDatabase.getLectures(TimetableSystem.student, courseCode.toLowerCase());
		
		
		try { 
			
			if(courseLectures != null && courseLectures.size() != 0)
			{
				System.out.println("im here");
				int count = 0;
				for(CalendarEvent lecture : courseLectures)
				{
					terminal.print(x, EnhancedTerminal.yPosition, "[" + count++ + "] " + lecture.getTitle() + ", " 
							+ lecture.getDateObject() + " From: " + lecture.getStartTime() + ", To: " 
							+ lecture.getEndTime(), terminal.green, terminal.black, terminal.bold);
				}
				
				String option = getCorrectInput("Please pick one of the lectures from above OR type 'add' to add your own: ");
				
				if(option.toLowerCase().equals("quit")) return;
				
				if(!option.toLowerCase().equals("add")){
					calDatabase.addLecture(courseLectures.get(Integer.parseInt(option)), TimetableSystem.student);
					return;
				}
			}
	        
	    } catch(NumberFormatException e) { 
	        terminal.print(x, EnhancedTerminal.yPosition, "Error with provided lecture", 
	        		terminal.red, terminal.black, terminal.bold);
	        return;
	    }
		
		// Get title for lecture		
		String title = getCorrectInput("Please provide lecture title: ");
		if(title.toLowerCase().equals("quit")) return;
		// Get days lecture  on		
		String days = getCorrectInput("Please provide days lectures are held, (mon tue wed...): ");	
		if(days.toLowerCase().equals("quit")) return;
		// Get months lecture held on
		String months = getCorrectInput("Provide month(s) lecture held, (jan feb mar apr may jun jul aug sept oct nov dec ): ");
		if(months.toLowerCase().equals("quit")) return;
		// Get year lecture on
		String year = getCorrectInput("Please provide year of lecture (0000): ");
		if(year.toLowerCase().equals("quit")) return;
		// Get startTime and endtime for assignment		
		String time = getCorrectInput("What time does the lecture start and end? Formart: 00:00 00:00 (start-time end-time): ");
		if(time.toLowerCase().equals("quit")) return;
		// Get description for assignment		
		String description = getCorrectInput("Please provide lecture description (Which room? Who's the prof): ");
		if(description.toLowerCase().equals("quit")) return;
				
		ArrayList<Integer> listOfDays = getListOfDays(days.split(" "));
		ArrayList<Integer> listOfMonths = getListOfMonth(months.split(" "));
		
		if(listOfDays.isEmpty() || listOfMonths.isEmpty()){
			terminal.print(x, EnhancedTerminal.yPosition, "Error with provided Days or Months", terminal.red, terminal.black, ScreenCharacterStyle.Bold);
			return;
		}
		
		if(!checkYear(year)){
        	terminal.print(x, EnhancedTerminal.yPosition, "Error with provided Year", terminal.red, terminal.black, ScreenCharacterStyle.Bold);
			return;
		}
		
		String[] parseTime = time.split(" ");
		
        if(parseTime.length != 2 || !checkTimeFormat(parseTime[0]) || !checkTimeFormat(parseTime[1])){
        	terminal.print(x, EnhancedTerminal.yPosition, "Error with provided Start or End Time", terminal.red, terminal.black, ScreenCharacterStyle.Bold);
			return;
        }
        
		Lecture lecture = new Lecture(title, listOfDays, 
				listOfMonths, Integer.parseInt(year), parseTime[0], parseTime[1], description, clean(courseCode).toLowerCase());
	
		
		calDatabase.addLecture(lecture, null);
		calDatabase.addLecture(lecture, TimetableSystem.student);
	}
	
	/**
	 * Remove course by asking user for course ID.
	 * @throws InterruptedException
	 */
	public void removeCourse() throws InterruptedException
	{
		int yPosition = EnhancedTerminal.yPosition + 1;		
		int x = 5;	
		String courseCode;
		// Get courseCode for lecture			
		courseCode = getCorrectInput("Please provide course code: ");
		if(courseCode.toLowerCase().equals("quit")) return;
		// Get title for lecture		
		CourseDAO courseDAO = new CourseDatabaseDAO();

		courseDAO.removeCourse(TimetableSystem.student, courseCode.toLowerCase());

		terminal.print(x, yPosition, "Course Deleted", terminal.red, terminal.black, terminal.bold);
	}
	
	
	public void removeLecture() throws FileNotFoundException, InterruptedException {
		int x = 5, count = 0;
		String option;
		CalendarDAO calDAO = new CalendarDatabase();
		String types[] = {"lecture"};
		ArrayList<CalendarEvent> allLectures = calDAO.getEvent(types, TimetableSystem.student);
		
		if(allLectures.isEmpty())
		{
			terminal.print(x, EnhancedTerminal.yPosition, "You have no lectures to delete", 
					terminal.red, terminal.black, terminal.bold);
			return;
		}
		
		for(CalendarEvent lecture : allLectures)
		{
			terminal.print(x, EnhancedTerminal.yPosition, "[" + count++ + "] " + lecture.getTitle() + ", " 
					+ lecture.getDateObject() + " From: " + lecture.getStartTime() + ", To: " 
					+ lecture.getEndTime(), terminal.green, terminal.black, terminal.bold);
		}
		
		option = getCorrectInput("Please pick one of the lectures from above: ");
		
		try { 
	        int index = Integer.parseInt(option);
	        if(index >= 0 && index < allLectures.size())
	        	calDAO.removeLecture((Lecture)allLectures.get(index), TimetableSystem.student);
	        else{
	        	terminal.print(x, EnhancedTerminal.yPosition, "Error with provided lecture", 
	        			terminal.red, terminal.black, terminal.bold);
	        	return;
	        }
	        
	    } catch(NumberFormatException e) { 
	        terminal.print(x, EnhancedTerminal.yPosition, "Error with provided lecture", 
	        		terminal.red, terminal.black, terminal.bold);
	        return;
	    }
				
		terminal.print(x, EnhancedTerminal.yPosition, "Lecture Deleted", terminal.red, terminal.black, terminal.bold);
	}
	
	/**
	 * Removes a personal event by asking user to either provide a title
	 * or picking from the list provided.
	 * @throws InterruptedException
	 * @throws FileNotFoundException
	 */
	public void removePersonalEvent() throws InterruptedException, FileNotFoundException {
		int x = 5;	
		String option, title = null;
		// Get courseCode for lecture			
		option = getCorrectInput("Please choose an option (Title, List): ");
		option = option.toLowerCase();
		if(option.equals("quit")) return;
		
		CalendarDAO calDAO = new CalendarDatabase();
		String types[] = {"personal"};
		ArrayList<CalendarEvent> personalEvents = calDAO.getEvent(types, TimetableSystem.student);
		
		if(personalEvents.isEmpty())
		{
			terminal.print(x, EnhancedTerminal.yPosition, "You have no personal events to delete", 
					terminal.red, terminal.black, terminal.bold);
			return;
		}
		
		if (option.equals("title")) {
			title = getCorrectInput("Please provide personal event title: ");			
		}
		
		int count = 0;
		for(CalendarEvent personal : personalEvents) {
			
			if(title != null){ //if we have to match a title
				if(personal.getTitle().equals(title)){
					terminal.print(x, EnhancedTerminal.yPosition, "[" + count++ + "] " + personal.getTitle() + ", " 
							+ personal.getDateObject() + " From: " + personal.getStartTime() + ", To: " 
							+ personal.getEndTime(), terminal.green, terminal.black, terminal.bold);
				}
			}
			else {
				terminal.print(x, EnhancedTerminal.yPosition, "[" + count++ + "] " + personal.getTitle() + ", " 
						+ personal.getDateObject() + " From: " + personal.getStartTime() + ", To: " 
						+ personal.getEndTime(), terminal.green, terminal.black, terminal.bold);
			}
		}
		
		
		option = getCorrectInput("Please pick one of the personal events from above: ");
		
		try { 
	        int index = Integer.parseInt(option);
	        if(index >= 0 && index < personalEvents.size())
	        	calDAO.removePersonal(personalEvents.get(index), TimetableSystem.student);
	        else{
	        	terminal.print(x, EnhancedTerminal.yPosition, "Error with provided personal event", 
	        			terminal.red, terminal.black, terminal.bold);
	        	return;
	        }
	        
	    } catch(NumberFormatException e) { 
	        terminal.print(x, EnhancedTerminal.yPosition, "Error with provided personal event", 
	        		terminal.red, terminal.black, terminal.bold);
	        return;
	    }
				
		terminal.print(x, EnhancedTerminal.yPosition, "Personal Event Deleted", terminal.red, terminal.black, terminal.bold);
	}
	
	public void change() throws InterruptedException, FileNotFoundException {
		int x = 5;	
		String courseCode, title = null;
		// Get courseCode for lecture			
		courseCode = getCorrectInput("Please provide course code for assignment to change: ");
		courseCode = courseCode.toLowerCase();
		
		if(courseCode.equals("quit")) return;
		
		CalendarDAO calDAO = new CalendarDatabase();
		String types[] = {"assignment"}; // only want assignments
		ArrayList<CalendarEvent> assignments = calDAO.getEvent(types, TimetableSystem.student);
		int count = 0;
		
		if(assignments.size() == 0)
		{
			terminal.print(x, EnhancedTerminal.yPosition, "No assignments to change for "  + courseCode, terminal.red, terminal.black, terminal.bold);
			return;
		}
		
		for(CalendarEvent assignment : assignments) {
			terminal.print(x, EnhancedTerminal.yPosition, "[" + count++ + "] " + assignment.getTitle() + ", "
					+ assignment.getDateObject() + ", " + assignment.getStartTime() + "-->" + assignment.getEndTime(), 
					terminal.green, terminal.black, terminal.bold);
		}
		
		
		String changeOption = getCorrectInput("Please pick one assignment to change from above: ");
		if(changeOption.equals("quit")) return;
		

				
		String[] membersToChange = {"Due Date (00 00 0000)", "Deadline (00:00)", "Weight"};
		String[] newInfo = {"", "", ""}; // each index match to above index
		count = 0;
		for(String member : membersToChange) {
			String data = getUserInput("Please provide new " + member + ": ");
			if(data.equals("quit")) return;
			newInfo[count++] = data;
		}
		String changedInfo = "";
		Assignment assignmentToChange = (Assignment) assignments.get(Integer.parseInt(changeOption));
		
		try { 
			if(newInfo[0].trim().equals("")){
				changedInfo += "due_date: " + assignmentToChange.getFullDate();
			}
			else {
				changedInfo += "due_date: " + formateDate(newInfo[0]);
			}
			
			if(newInfo[1].trim().equals("")){
				changedInfo += ", due_time: " + assignmentToChange.getEndTime();
			}
			else {
				changedInfo += ", due_time: " + newInfo[1];
			}
			
			if(newInfo[2].trim().equals("")){
				changedInfo += ", weight: " + assignmentToChange.getWeight();
			}
			else {
				changedInfo += ", weight: " + newInfo[2];
			}
	    } catch(NumberFormatException e) { 
	    	terminal.print(x, EnhancedTerminal.yPosition, "An Error Occured: " + changedInfo, terminal.red, terminal.black, terminal.bold);
	    	
	    }
				
		
		Email email = new Email();
		email.sendEmails(assignmentToChange, courseCode, changedInfo);				
				
		//terminal.print(x, EnhancedTerminal.yPosition, "New Assignment: " + , bgColor, fgColor, styles);
		terminal.print(x, EnhancedTerminal.yPosition, "Assignment change has been sent for verification: " + changedInfo, terminal.red, terminal.black, terminal.bold);
	
		
	}
	
	
	public String formateDate(String date)
	{
		String dates[] = date.split(" ");
		return dates[0] + "/" + dates[1] + "/" + dates[2];
	}
	
	/**
	 * Given a message ask user for the response of that message and return
	 * user response. Prompt user to enter response until response given or
	 * 'quit' received.
	 * 
	 * @param msg
	 * @return Response by user
	 * @throws InterruptedException
	 */
	public String getCorrectInput(String msg) throws InterruptedException
	{	
		int yPosition = EnhancedTerminal.yPosition + 1;	
		int x = 5;
		// Get courseCode for lecture		
		String input;
		while(true){
			input = getUserInput(msg);
			if (!isEmpty(input))
				break;
			else{
				terminal.print(x, yPosition, "Invalid Input (Type 'quit' to quit command)", terminal.red, terminal.black, ScreenCharacterStyle.Bold);
				yPosition = EnhancedTerminal.yPosition +1;	
			}
		}
		
		return input.trim();
	}
	
	/**
	 * Check if weight is in correct format.
	 * @param weight
	 * @return Return True if correct format, else False
	 */
	public boolean checkWeight(String weight){
		try { 
	        Integer.parseInt(weight);
	        return true;
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	}
	
	/**
	 * Check if year is in correct format.
	 * @param year
	 * @return Return True if correct format, else False
	 */
	public boolean checkYear(String year){
		try { 
	        Integer.parseInt(year);
	        return true;
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	}
	
	/**
	 * Check if date is in correct format.
	 * @param date
	 * @return Return True if correct format, else False
	 */
	public boolean checkDate(String date){
		String[] dayMonthYear = date.split(" ");
		if(dayMonthYear.length != 3)
			return false;
		
		if(dayMonthYear[0].length() != 2 || dayMonthYear[1].length() != 2 || dayMonthYear[2].length() != 4)
			return false;
			
			
		try { 
	        Integer.parseInt(dayMonthYear[0]);
	        Integer.parseInt(dayMonthYear[1]); 
	        Integer.parseInt(dayMonthYear[2]);
	        return true;
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	}
	
	/**
	 * Check if time is in correct format.
	 * @param time
	 * @return Return True if correct format, else False
	 */
	public boolean checkTimeFormat(String time)
	{
		String[] times = time.split(":");
		if(times.length == 2 && times[0].length() == 2 && times[1].length() == 2){
			try { 
		        Integer.parseInt(times[0]);
		        Integer.parseInt(times[1]); 
		        return true;
		    } catch(NumberFormatException e) { 
		        return false; 
		    }
		}
		
		return false;
	}
	
	/**
	 * Given an String Array of Days return an ArrayList where each
	 * day is converted to its numeric value
	 * @param days
	 * @return ArrayList of integer days
	 */
	public ArrayList<Integer> getListOfDays(String[] days)
	{
		ArrayList<Integer> daysList = new ArrayList<Integer>();
		
		for(String day : days){
			day = clean(day.toLowerCase());
			if(day.equals("mon")){
				daysList.add(0);
			}
			else if(day.equals("tue"))
				daysList.add(1);
			else if(day.equals("wed"))
				daysList.add(2);
			else if(day.equals("thur"))
				daysList.add(3);
			else if(day.equals("fri"))
				daysList.add(4);
			else if(day.equals("sat"))
				daysList.add(5);
			else if(day.equals("sun"))
				daysList.add(6);				
		}

		return daysList;
	}
	
	/**
	 * Given an String Array of months return an ArrayList where each
	 * month is converted to its numeric value
	 * @param months
	 * @return ArrayList of integer months
	 */
	public ArrayList<Integer> getListOfMonth(String[] months)
	{
		ArrayList<Integer> monthList = new ArrayList<Integer>();
		
		for(String month : months){
			month = clean(month.toLowerCase());			
			if(month.equals("jan"))
				monthList.add(0);
			else if(month.equals("feb"))
				monthList.add(1);
			else if(month.equals("mar"))
				monthList.add(2);
			else if(month.equals("apr"))
				monthList.add(3);
			else if(month.equals("may"))
				monthList.add(4);
			else if(month.equals("jun"))
				monthList.add(5);			
			else if(month.equals("jul"))
				monthList.add(6);	
			else if(month.equals("aug"))
				monthList.add(7);	
			else if(month.equals("sept"))
				monthList.add(8);	
			else if(month.equals("oct"))
				monthList.add(9);	
			else if(month.equals("nov"))
				monthList.add(10);	
			else if(month.equals("dec"))
				monthList.add(11);	
		}
		return monthList;
	}
	
	
	/**
	 * Check if registration is complete by using UserDAO.
	 * @param username
	 * @param email
	 * @param password
	 * @return
	 */
	public Student checkRegister(String username, String email, String password)
	{
		UserInDatabase userInDatabase = new UserInDatabase();
		return userInDatabase.registerUser(username, email, password);
	}
	
	/**
	 * Checks if user exists by calling logIn method inside
	 * UserDAO
	 * @param username
	 * @param email
	 * @param password
	 * @return
	 */
	public boolean checkLogIn(String email, String password)
	{
		UserInDatabase userInDatabase = new UserInDatabase();
		return userInDatabase.logIn(email, password);
	}
	
	public String clean(String input)
	{
		return input.trim();
	}
		
	/**
	 * Check if given input is empty
	 * @param input
	 * @return True if input is empty, False otherwise
	 */
	public boolean isEmpty (String input) {
		return input.trim().equals("");
	}

}


