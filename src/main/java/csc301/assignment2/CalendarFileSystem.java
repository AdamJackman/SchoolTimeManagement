package csc301.assignment2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

public class CalendarFileSystem implements CalendarDAO{
	
	
	public ArrayList<CalendarEvent> getEvent(String[] type, User user) throws FileNotFoundException
	{
		
		ArrayList<CalendarEvent> events = new ArrayList<CalendarEvent>();
		System.out.println("In assigments: " + "src/main/resources/users/" + user.getName().toLowerCase() + ".txt");
		Scanner courseScanner = new Scanner(new File("src/main/resources/users/" + user.getName().toLowerCase() + ".txt"));
		
		// loop through all the user's courses
		while(courseScanner.hasNext()){
			String course = courseScanner.nextLine().toLowerCase();
			Scanner eventScanner = new Scanner(new File("src/main/resources/courses/" + course + ".txt"));
			System.out.println("In COURSE: " + "src/main/resources/courses/" + course + ".txt");
			while(eventScanner.hasNext()){
				String event = eventScanner.nextLine();
				String[] eventArgs = event.split(",");
				
				if(Arrays.asList(type).contains(eventArgs[0])){
					if(eventArgs[0].equals("assignment")){
						Assignment assignment = new Assignment(eventArgs[1], Integer.parseInt(eventArgs[2]),  
								Integer.parseInt(eventArgs[3]),  Integer.parseInt(eventArgs[4]), eventArgs[5], 
								eventArgs[6], eventArgs[7], Integer.parseInt(eventArgs[8]), Boolean.parseBoolean("true"));
						
						events.add(assignment); // add the assignment to the list of CalendarEvents
					}
					else if(eventArgs[0].equals("lecture")){						
						Lecture lecture = new Lecture(eventArgs[1], convertStrToList(eventArgs[2]), 
								convertStrToList(eventArgs[3]), Integer.parseInt(eventArgs[4]), eventArgs[5], eventArgs[6],
								eventArgs[7], eventArgs[8]);
						events.add(lecture);					
					}
				}
			}
			eventScanner.close();
		}
		
		courseScanner.close();
		
		if(Arrays.asList(type).contains("personal")){
			Scanner personalScanner = new 
					Scanner(new File("src/main/resources/events/" + user.getName().toLowerCase() + ".txt"));
			
			while(personalScanner.hasNext()){
				String event = personalScanner.nextLine();
				String eventArgs[] = event.split(",");
				CalendarEvent personalEvent = new CalendarEvent(eventArgs[0], Integer.parseInt(eventArgs[1]),  
						Integer.parseInt(eventArgs[2]),  Integer.parseInt(eventArgs[3]), eventArgs[4], 
						eventArgs[5], eventArgs[6]);
				events.add(personalEvent);
			}

			personalScanner.close();
		}
		
		return events;
	}


 //personal events
	public void addEvent(CalendarEvent event, User user)
	{
		Date date = event.getDateObject();
		String path = "src/main/resources/events/";
		PrintWriter personalWriter;
		try{
			//making a new file and adding the users personal events or just adding to the existing user file
			personalWriter = new PrintWriter(new FileOutputStream(new File(path + user.getName() + ".txt"), true));
			personalWriter.println(event.getTitle() + "," + event.getDay().get(0) + "," + event.getMonth().get(0) + "," +
			event.getYear() + "," + event.getStartTime() + "," + event.getEndTime() + "," +
			event.getDescription());
			personalWriter.close();
		}
		catch (FileNotFoundException e){
			e.printStackTrace();
		}
	}
	
	
	
	@Override
	public void addAssignment(Assignment assignment, Course course)
	{
		int weight = assignment.getWeight();
		Date date = assignment.getDateObject();
		String fname = course.getCourseCode().toLowerCase() + ".txt";
		String path = "src/main/resources/courses/";
		PrintWriter assignmentWriter;
		try{
			//adding assignments to the course file
			assignmentWriter = new PrintWriter(new FileOutputStream(new File(path + fname), true));
			assignmentWriter.println("assignment," + assignment.getTitle() + "," + assignment.getDay().get(0) + "," +
			assignment.getMonth().get(0) + "," + assignment.getYear() + "," + assignment.getStartTime() + "," +
			assignment.getEndTime() + "," +assignment.getDescription() + "," + assignment.getWeight());
			assignmentWriter.close();
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void addLecture(Lecture lecture, Student student)
	{
		Date date = lecture.getDateObject();
		String fname = lecture.getCourseCode().toLowerCase() + ".txt";
		String path = "src/main/resources/courses/";
		PrintWriter lectureWriter;
		try{
			//adding lectures to the course file
			lectureWriter = new PrintWriter(new FileOutputStream(new File(path + fname), true));
								
			lectureWriter.println("lecture," + lecture.getTitle() + "," + convertListToString(lecture.getDay()) + "," 
					+ convertListToString(lecture.getMonth()) + "," + lecture.getYear() + "," + lecture.getStartTime() 
					+ "," + lecture.getEndTime() + "," + lecture.getDescription() + "," + lecture.getCourseCode());
			
			lectureWriter.close();
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
		}
	}
	
	public String convertListToString(ArrayList<Integer> list)
	{
		String listString = "";
		
		for(int item : list)
		{
			listString += item + " ";
		}
		
		return listString;
	}
	
	public ArrayList<Integer> convertStrToList(String str)
	{
		ArrayList<Integer> list = new ArrayList<Integer>();			
		String[] items = str.split(" ");
		
		for(String item : items)
		{
			item = item.trim();
			if(!item.equals(""))
				list.add(Integer.parseInt(item));
		}
		
		return list;
	}


	@Override
	public void removePersonal(CalendarEvent event, User user) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void removeAssignment(Assignment assignment, String courseCode) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void removeLecture(Lecture lecture, Student student) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public ArrayList<Lecture> getLectures(Student student, String courseCode) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ArrayList<Assignment> getAssignments(Student student,
			String courseCode) {
		// TODO Auto-generated method stub
		return null;
	}


}
