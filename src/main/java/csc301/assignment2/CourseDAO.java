package csc301.assignment2;

import java.util.ArrayList;

public interface CourseDAO {
	
	/**
	 * This method is used to create Course in courses folder,
	 * then add the course to the users personal folder.
	 * @param s
	 * @param courseCode
	 * @return boolean
	 */
	boolean addCourse(Student s, String courseCode);
	
	/**
	 * Helper method to put course into users personal file,
	 * @param s
	 * @param courseCode
	 * @return boolean
	 */
	boolean writeToStudent(Student s, String courseCode);
	
	/**
	 * This method is used to remove a course.
	 * @param s
	 * @param courseCode
	 */
	void removeCourse(Student s, String courseCode);
	
	/**
	 * Find and returns course
	 * @param courseCode
	 * @return Course
	 */
	Course findCourse(String courseCode);

	/**
	 * Helper method to convert Strings to ArrayList
	 * @param str
	 * @return ArrayList<Integer>
	 */
	ArrayList<Integer> convertStrToList(String str);
}
