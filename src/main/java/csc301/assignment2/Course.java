package csc301.assignment2;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/*
 * A student can have many Course objects, which know its course code and has a 
 * HashSet of Lectures and Assignments. 
 */
public class Course {
	private Set<Lecture> lectures;
	private Set<Assignment> assignments;
	private String courseCode;
	
	public Course(String courseCode) {
		this.courseCode = courseCode;
		this.lectures = new HashSet<Lecture>();
		this.assignments = new HashSet<Assignment>();
	}
	
	public Iterator<Lecture> getLectures() {
		return lectures.iterator();
	}
	
	public Iterator<Assignment> getAssignments() {
		return assignments.iterator();
	}
	
	public String getCourseCode() {
		return courseCode;
	}
	
	public void addLecture(Lecture e) {
		lectures.add(e);
	}
	
	public void addAssignment(Assignment e) {
		assignments.add(e);
	}
}
