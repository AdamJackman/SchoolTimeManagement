package csc301.assignment2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public interface CalendarDAO {
	
	/**
	 * Returns a list of CalendarEvents that the user has.
	 * @param type : the type of CalendarEvent to return (example Assignments, Lectures)
	 * @param user : the user that the events belong to
	 * @return A list of CalendarEvents of type 'type' that belong to the user 'user'
	 * @throws FileNotFoundException 
	 */
	public ArrayList<CalendarEvent> getEvent(String[] type, User user) throws FileNotFoundException;
	
	/**
	 * Add the CalendarEvent 'event' to user's timetable.
	 * @param event : the CalendarEvent to add
	 * @param user : the user for which the event is added
	 */
	public void addEvent(CalendarEvent event, User user);
	
	/**
	 * Remove the CalendarEvent 'personal event' from user's timetable
	 * @param event : the event to remove
	 * @param user : the user for which the event will be removed
	 */
	public void removePersonal(CalendarEvent event, User user);
	
	/**
	 * Remove an Assignment from a course
	 * @param assignment : the assignment to be removed
	 * @param courseCode : the course for which the assignment will be removed
	 */
	public void removeAssignment(Assignment assignment, String courseCode);
	
	/**
	 * Remove a Lecture from a course
	 * @param lecture : the lecture to be removed
	 */
	public void removeLecture(Lecture lecture, Student student);
	
	/**
	 * Add an event to the corresponding course
	 * @param assignment : the assignment to add
	 * @param course : the course for which the assignment belongs to
	 */
    public void addAssignment(Assignment assignment, Course course);
    
	/**
	 * Add a lecture slot to its corresponding course
	 * @param lecture : the lecture to add to the course
	 */ 
    public void addLecture(Lecture lecture, Student student);
    
    public ArrayList<Lecture> getLectures(Student student, String courseCode);
    
    public ArrayList<Assignment> getAssignments(Student student, String courseCode);

}
