package csc301.assignment2;

import java.util.ArrayList;

public class Lecture extends CalendarEvent {

	private String courseCode;
	
	public Lecture(String title, ArrayList<Integer> day, ArrayList<Integer> month, int year, String startTime,
			String endTime, String description, String courseCode) 
	{
		super(title, day, month, year, startTime, endTime, description);		
		this.courseCode = courseCode;
	}
	
	
	public String getCourseCode()
	{
		return this.courseCode;
	}

}
