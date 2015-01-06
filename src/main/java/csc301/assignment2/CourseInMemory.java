package csc301.assignment2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class CourseInMemory implements CourseDAO {

	/**
	 * This method is used add a course to courses folder
	 * 
	 * @param s
	 * @param courseCode
	 */
	@Override
	public boolean addCourse(Student s, String courseCode) {

		
		String PATH="src/main/resources/courses/";		

		File course = new File(PATH + courseCode.toLowerCase() + ".txt");

		if (course.exists()) {
			// add to Students course file
			System.out.println("True");
			writeToStudent(s, courseCode);
			return true;
		} else {
			try {
				course.createNewFile();
				writeToStudent(s, courseCode);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}
	}

	/**
	 * This method is used write a course to students personal file, if the
	 * course isn't already in there
	 * 
	 * @param s
	 * @param courseCode
	 */
	@Override
	public boolean writeToStudent(Student s, String courseCode) {

		File doc = new File("src/main/resources/users/"
				+ s.getName().replaceAll(" ", "").toLowerCase() + ".txt");
		try {
			Scanner scanner = new Scanner(doc);

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.toLowerCase().contains(courseCode.toLowerCase())) {
					scanner.close();
					return true;
				}
			}
			scanner.close();

			FileWriter fw;
			fw = new FileWriter(doc, true);
			fw.write(courseCode.toLowerCase());
			fw.close();
			return true;

		} catch (IOException e) {
			return false;
		}

	}

	/**
	 * This method parses through given course file, adds lecture or assignment
	 * depending on what is in file
	 * 
	 * @param courseCode
	 * @return Course
	 */
	@Override
	public Course findCourse(String courseCode) {
		try {
			Scanner scanner = new Scanner(new File(
					"src/main/resources/courses/" + courseCode.toLowerCase()
							+ ".txt"));
			Course course = new Course(courseCode.toLowerCase());

			while (scanner.hasNextLine()) {
				String[] t = scanner.nextLine().split(",");

				if (t.length != 8) {
					scanner.close();
					return null;
				}
				try {
					if (t[0].toLowerCase().startsWith("lecture")) {
						Lecture x = new Lecture(t[0], convertStrToList(t[1]),
								convertStrToList(t[2]), Integer.parseInt(t[3]),
								t[4], t[5], t[6], t[7]);
						course.addLecture(x);
					} else {
						Assignment x = new Assignment(t[0],
								Integer.parseInt(t[1]), Integer.parseInt(t[2]),
								Integer.parseInt(t[3]), t[4], t[5], t[6],
								Integer.parseInt(t[7]), true);
						course.addAssignment(x);
					}
				} catch (NumberFormatException e) {
					scanner.close();
					return null;
				}
			}
			scanner.close();
			return course;
		} catch (FileNotFoundException e) {
			return null;
		}

	}

	/**
	 * Helper method to convert Strings to ArrayList
	 * 
	 * @param str
	 * @return ArrayList<Integer>
	 */
	@Override
	public ArrayList<Integer> convertStrToList(String str) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		String[] items = str.split(" ");

		for (String item : items) {
			item = item.trim();
			if (!item.equals(""))
				list.add(Integer.parseInt(item));
		}

		return list;
	}

	public void removeCourse() {
	}


	@Override
	public void removeCourse(Student s, String courseCode) {
		// TODO Auto-generated method stub
		
	}

}
