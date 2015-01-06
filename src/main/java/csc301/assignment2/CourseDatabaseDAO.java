package csc301.assignment2;

import java.util.ArrayList;
import java.net.URISyntaxException;
import java.sql.*;


public class CourseDatabaseDAO implements CourseDAO {
	//manually change in order to allow / disallow debug prints true==ON, false==OFF
	final boolean DEBUG = true;
	
	@Override
	public boolean addCourse(Student s, String courseCode) {
		Connection c;
		Statement stmt = null;
		boolean pass = false;
		try {
			 c = getConnection();
			 stmt = c.createStatement();
			 stmt.executeUpdate(
					 String.format("INSERT INTO course VALUES ('%s')", courseCode.toLowerCase())
					 );
			 pass = true;
			 stmt.close();
			 c.close();
		} catch (SQLException | URISyntaxException e) {
			if(DEBUG){
				e.printStackTrace();
			}
		}
		writeToStudent(s, courseCode);
		return pass;
	}

	@Override
	public boolean writeToStudent(Student s, String courseCode) {
		PreparedStatement prepared = null;
		Connection con = null;
		try {
			con = DriverManager.getConnection("jdbc:postgresql://ec2-54-83-196-7.compute-1.amazonaws.com/d5d7u2801e8s1c?user=kisdjufljkdkoc&password=JPMtcrooqYqaaHc-YSjumnB97v&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory");
	    } 
		
		catch (Exception e) {
			 if (DEBUG){
		         e.printStackTrace();
		         System.err.println(e.getClass().getName()+": "+e.getMessage());
			 }
	         System.exit(0);
	    }
		//Create new connection
		
		try {
			prepared = con.prepareStatement("INSERT INTO enrolled(cid, email)" + "VALUES (?,?) ");
			prepared.setString(1, courseCode);
			prepared.setString(2, s.getEmail());
			prepared.executeUpdate();
			con.close();
			return true;
		} catch (SQLException e) {
			if (DEBUG){
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public void removeCourse(Student s, String courseCode) {
		PreparedStatement prepared = null;
		Connection con = null;
		try {
			con = DriverManager.getConnection("jdbc:postgresql://ec2-54-83-196-7.compute-1.amazonaws.com/d5d7u2801e8s1c?user=kisdjufljkdkoc&password=JPMtcrooqYqaaHc-YSjumnB97v&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory");
	    } 
		
		catch (Exception e) {
			if (DEBUG){
		         e.printStackTrace();
		         System.err.println(e.getClass().getName()+": "+e.getMessage());
			}
	         System.exit(0);
	    }
		
		try {
			prepared = con.prepareStatement("DELETE FROM enrolled WHERE cid=? AND email=?");
			prepared.setString(1, courseCode);
			prepared.setString(2, s.getEmail());
			prepared.executeUpdate();
			con.close();
		} catch (SQLException e) {
			if (DEBUG){
				e.printStackTrace();
			}
		}
	}

	@Override
	public Course findCourse(String courseCode) {
		// TODO Auto-generated method stub
		Connection c;
		Statement stmt = null;
		Course course = new Course(courseCode.toLowerCase());
		try {
			String sql = "select * in users lecture where cid = " + courseCode.toLowerCase() + " ;";
			 c = getConnection();
			 stmt = c.createStatement();
			 ResultSet rs = stmt.executeQuery(sql);
			 while (rs.next()) {
				 course.addLecture(new Lecture(rs.getString("lecture.title"), convertStrToList(rs.getString("lecture.days")), 
						 convertStrToList(rs.getString("lecture.months")), Integer.parseInt(rs.getString("lecture.year")), 
						 rs.getString("lecture.start_time"), rs.getString("lecture.end_time"), rs.getString("lecture.description"),
						 courseCode.toLowerCase()));
			 }
			 sql = "select * in users assignment where cid = " + courseCode.toLowerCase() + " ;";
			 rs = stmt.executeQuery(sql);
			 while (rs.next()) {
				 course.addAssignment(new Assignment(rs.getString("assignment.title"), Integer.parseInt(rs.getString("assignment.due_date").split(",")[0]), 
				 Integer.parseInt(rs.getString("assignment.due_date").split(",")[1]), Integer.parseInt(rs.getString("assignment.due_date").split(",")[2]), 
				 rs.getString("assignment.due_time"), rs.getString("assignment.due_time"), 
				 rs.getString("assignment.description"), Integer.parseInt(rs.getString("assignment.weight")), Boolean.parseBoolean(rs.getString(8))));
			 }
			 stmt.close();
			 rs.close();
			 c.close();
			 return course;
		} catch (SQLException | URISyntaxException e) {
			if (DEBUG){
				e.printStackTrace();
			}
		}
		return null;
	}

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
	
	private Connection getConnection() throws URISyntaxException, SQLException {
		Connection c = null;
		try {
			c = DriverManager
					.getConnection("jdbc:postgresql://ec2-54-83-196-7.compute-1.amazonaws.com/d5d7u2801e8s1c?user=kisdjufljkdkoc&password=JPMtcrooqYqaaHc-YSjumnB97v&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory");
		} catch (Exception e) {
			if (DEBUG){
				e.printStackTrace();
				System.err.println(e.getClass().getName() + ": " + e.getMessage());
			}
			System.exit(1);
		}
		return c;
	}
}
