package csc301.assignment2;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
//import java.util.stream.IntStream;
import java.sql.PreparedStatement;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class CalendarDatabase implements CalendarDAO{
	//manually change in order to allow / disallow debug prints true==ON, false==OFF
	final boolean DEBUG = false;
	
	Connection con = null;
	PreparedStatement prepared = null;
	ResultSet rs = null;

	public ArrayList<CalendarEvent> getEvent(String[] type, User user)
	{
		
	      try {
	         con = DriverManager
	            .getConnection("jdbc:postgresql://ec2-54-83-196-7.compute-1.amazonaws.com/d5d7u2801e8s1c?user=kisdjufljkdkoc&password=JPMtcrooqYqaaHc-YSjumnB97v&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory");
	      } catch (Exception e) {
	    	 if(DEBUG){
	    		 e.printStackTrace();
	    		 System.err.println(e.getClass().getName()+": "+e.getMessage());
	    	 }
	         System.exit(0);
	      }
	      
		ArrayList<CalendarEvent> events = new ArrayList<CalendarEvent>();
		try {
			
			for (int j=0; j< type.length; j++){
				if (type[j].equals("personal")){
					prepared = con.prepareStatement("SELECT * FROM personal WHERE email =?");
				
				}
				else if (type[j].equals("assignment")){
					prepared = con.prepareStatement("SELECT * FROM assignment NATURAL JOIN enrolled WHERE email =?");
				
				}
				else{
					prepared = con.prepareStatement("SELECT * FROM lecture NATURAL JOIN enrolled WHERE email=?");
				}
				prepared.setString(1,user.getEmail());
				rs = prepared.executeQuery();
				
				if (type[j].equals("personal")){
					while (rs.next()){
						String[] day = splitday(rs.getString(3));
						CalendarEvent myevent = new CalendarEvent(rs.getString(2),  Integer.parseInt(day[0]),  Integer.parseInt(day[1]), Integer.parseInt(day[2]), rs.getString(4), rs.getString(5),rs.getString(6));
						events.add(myevent);
					}
				} else if (type[j].equals("assignment")){
					
					while (rs.next()){
						if(DEBUG){
							System.out.println("hhh");
						}
						String[] day = splitday(rs.getString(4));
						Boolean bool = false;
						
						System.out.println("Bool = " + rs.getString(9));
						
						if(rs.getString(9).equals("true")){
							bool = true;
						}
				
						CalendarEvent myAssignment = new Assignment(rs.getString(3),  Integer.parseInt(day[0]),  Integer.parseInt(day[1]), Integer.parseInt(day[2]), rs.getString(5), rs.getString(5),rs.getString(6),rs.getInt(7), bool);
						events.add(myAssignment);
					}
				}else{
					while (rs.next()){
						ArrayList<Integer> day = splitArray(rs.getString(4));
						ArrayList<Integer> month = splitArray(rs.getString(5));
						CalendarEvent myLecture = new Lecture(rs.getString(3), day,  month, rs.getInt(6), rs.getString(7), rs.getString(8),rs.getString(9),rs.getString(2));
						events.add(myLecture);
					}
				}
				rs.close();
				
			}
			con.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			if(DEBUG){
				e.printStackTrace();
			}
		}
		
		return events;
	
	}
	private String[] splitday(String day){
			String[] newday = day.split(" ");
			return (newday);		
	}
	
	private ArrayList<Integer> splitArray(String day){
		String[] newday = day.split(" ");
		ArrayList<Integer> array = new ArrayList<Integer>();
		for (String objects : newday){
			int objects2 = Integer.parseInt(objects);
			array.add(objects2);
		}
		return (array);		
	}
	
	
	//personal events
	public void addEvent(CalendarEvent event, User user) {
		
		try {
	         con = DriverManager
	            .getConnection("jdbc:postgresql://ec2-54-83-196-7.compute-1.amazonaws.com/d5d7u2801e8s1c?user=kisdjufljkdkoc&password=JPMtcrooqYqaaHc-YSjumnB97v&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory");
	    } 
		
		catch (Exception e) {
	         e.printStackTrace();
	         System.err.println(e.getClass().getName()+": "+e.getMessage());
	         System.exit(0);
	    }

		 try {

	            prepared = con.prepareStatement(
	                        "INSERT INTO personal(title, event_date, start_time, end_time, description, email)" +
	                        "VALUES (?,?,?,?,?,?) ");

	            prepared.setString(1, event.getTitle());
	            prepared.setString(2, Integer.toString(event.getDay().get(0))+" "+Integer.toString(event.getMonth().get(0))+" "+Integer.toString(event.getYear()));
	            prepared.setString(3, event.getStartTime());
	            prepared.setString(4, event.getEndTime());
	            prepared.setString(5, event.getDescription());
	            prepared.setString(6, user.getEmail());
	            prepared.executeUpdate();
	            prepared.close();
	            con.close();
		 }catch (SQLException e) {
				// TODO Auto-generated catch block
			 if(DEBUG){
				e.printStackTrace();
			 }
		}
		
	}

	@Override
	public void removePersonal(CalendarEvent event, User user) {
		// TODO Auto-generated method stub
		
		try{
			con = DriverManager
					.getConnection("jdbc:postgresql://ec2-54-83-196-7.compute-1.amazonaws.com/d5d7u2801e8s1c?user=kisdjufljkdkoc&password=JPMtcrooqYqaaHc-YSjumnB97v&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory");
			prepared = con.prepareStatement("DELETE FROM personal WHERE email=? AND title=? AND event_date=?" +
		" AND start_time=? AND end_time=? AND description=?");
			prepared.setString(1, user.getEmail());
			prepared.setString(2, event.getTitle());
			prepared.setString(3, Integer.toString(event.getDay().get(0))+" "+Integer.toString(event.getMonth().get(0))+" "+Integer.toString(event.getYear()));
			prepared.setString(4, event.getStartTime());
			prepared.setString(5, event.getEndTime());
			prepared.setString(6, event.getDescription());
			prepared.executeUpdate();
			prepared.close();
			con.close();
		}
		catch(SQLException e){
			if(DEBUG){
				e.printStackTrace();
			}
		}
		
	}
	
	public void removeAssignment(Assignment assignment, String courseCode){
		try{
			con = DriverManager
					.getConnection("jdbc:postgresql://ec2-54-83-196-7.compute-1.amazonaws.com/d5d7u2801e8s1c?user=kisdjufljkdkoc&password=JPMtcrooqYqaaHc-YSjumnB97v&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory");
			prepared = con.prepareStatement("DELETE FROM assignment WHERE cid=? AND title=? AND due_date=? AND due_time=?"
					+ " AND description=? AND weight=?");
			prepared.setString(1, courseCode);
			prepared.setString(2, assignment.getTitle());
			prepared.setString(3, Integer.toString(assignment.getDay().get(0))+" "+Integer.toString(assignment.getMonth().get(0))+" "+Integer.toString(assignment.getYear()));
			prepared.setString(4, assignment.getEndTime());
			prepared.setString(5, assignment.getDescription());
			prepared.setInt(6, assignment.getWeight());
			prepared.executeUpdate();
			prepared.close();
			con.close();
			
		}
		catch(SQLException e){
			if(DEBUG){
				e.printStackTrace();
			}
		}
	}

	@Override
	public void addAssignment(Assignment assignment, Course course) {
		
		try {
	         con = DriverManager
	            .getConnection("jdbc:postgresql://ec2-54-83-196-7.compute-1.amazonaws.com/d5d7u2801e8s1c?user=kisdjufljkdkoc&password=JPMtcrooqYqaaHc-YSjumnB97v&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory");
	    } 
		
		catch (Exception e) {
			 if(DEBUG){
				 e.printStackTrace();
				 System.err.println(e.getClass().getName()+": "+e.getMessage());
			 }
	         System.exit(0);
	    }
		
		boolean courseExists = false;
		try{
			prepared = con.prepareStatement("SELECT COUNT(*) FROM course WHERE cid=?");
			prepared.setString(1, course.getCourseCode());
			
			ResultSet rs = prepared.executeQuery();
			while(rs.next()){
				if(rs.getString("count").equals("1")){
					courseExists = true;
				}
			}
			rs.close();
			
		}
		catch(SQLException e){
			if(DEBUG){
				e.printStackTrace();
			}
		}
		
		if(!courseExists){
			return;
		}
		
		 try {
			 	
	            prepared = con.prepareStatement(
	                        "INSERT INTO assignment(cid, title, due_date, due_time, description, weight)" +
	                        "VALUES (?,?,?,?,?,?) ");

	            prepared.setString(1, course.getCourseCode());
	            prepared.setString(2, assignment.getTitle());
	            prepared.setString(3, Integer.toString(assignment.getDay().get(0))+" "+Integer.toString(assignment.getMonth().get(0))+" "+Integer.toString(assignment.getYear()));
	            prepared.setString(4, assignment.getEndTime());
	            prepared.setString(5, assignment.getDescription());
	            prepared.setInt(6, assignment.getWeight());
	            prepared.executeUpdate();
	            prepared.close();
	            con.close();
	            Email email = new Email();
	            email.sendEmails(assignment,course.getCourseCode());
		 }catch (SQLException e) {
				// TODO Auto-generated catch block
			 	if(DEBUG){
			 		e.printStackTrace();
			 	}
		}
		
	}

	@Override
	public void addLecture(Lecture lecture, Student student) {
	
		try {
			con = DriverManager
					.getConnection("jdbc:postgresql://ec2-54-83-196-7.compute-1.amazonaws.com/d5d7u2801e8s1c?user=kisdjufljkdkoc&password=JPMtcrooqYqaaHc-YSjumnB97v&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory");
	 
			prepared = con.prepareStatement("SELECT * FROM course where cid = ?");
			prepared.setString(1, lecture.getCourseCode());
			rs = prepared.executeQuery();
			if (rs.next() == false){
				
			}
			else{
				if(student == null){
			
			
				prepared = con.prepareStatement(
	                        "INSERT INTO lecture(cid, title, days, months, year, start_time, end_time, description)" +
	                        "VALUES (?,?,?,?,?,?,?,?) ");
	
	            prepared.setString(1, lecture.getCourseCode());
	            prepared.setString(2, lecture.getTitle());
	            prepared.setString(3, convertListToString(lecture.getDay()));
	            prepared.setString(4, convertListToString(lecture.getMonth()));
	            prepared.setInt(5, lecture.getYear());
	            prepared.setString(6, lecture.getStartTime());
	            prepared.setString(7, lecture.getEndTime());
	            prepared.setString(8, lecture.getDescription());
	            	           	                     
	            prepared.executeUpdate();

	            
				}
				else{
					int lectureID = -1;
					prepared = con.prepareStatement("SELECT lid FROM lecture WHERE cid=? AND title=? AND days=? AND "
					+ "months=? AND year=? AND start_time=? AND end_time=? AND description=?");
					
					prepared.setString(1, lecture.getCourseCode());
					prepared.setString(2, lecture.getTitle());
					prepared.setString(3, convertListToString(lecture.getDay()));
					prepared.setString(4, convertListToString(lecture.getMonth()));
					prepared.setInt(5, lecture.getYear());
					prepared.setString(6, lecture.getStartTime());
					prepared.setString(7, lecture.getEndTime());
					prepared.setString(8, lecture.getDescription());
					
					ResultSet rs = prepared.executeQuery();
					
					while(rs.next()){
						lectureID = rs.getInt("lid");
					}
					rs.close();
					
					
					 prepared = con.prepareStatement("UPDATE enrolled SET lid =? WHERE cid=? AND email=?");
					 
					 prepared.setInt(1, lectureID);
					 prepared.setString(2,lecture.getCourseCode());
					 prepared.setString(3, student.getEmail());
					 prepared.executeUpdate();
					 
					 
				}
	            prepared.close();
	            con.close();
			}
			
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			if(DEBUG){
				e.printStackTrace();
			}
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
	@Override
	public void removeLecture(Lecture lecture, Student student) {
		
		try {
			con = DriverManager
					.getConnection("jdbc:postgresql://ec2-54-83-196-7.compute-1.amazonaws.com/d5d7u2801e8s1c?user=kisdjufljkdkoc&password=JPMtcrooqYqaaHc-YSjumnB97v&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory");   
			
			int lectureID = -1;
			prepared = con.prepareStatement("SELECT lid FROM lecture WHERE cid=? AND title=? AND days=? AND "
			+ "months=? AND year=? AND start_time=? AND end_time=? AND description=?");
			
			prepared.setString(1, lecture.getCourseCode());
			prepared.setString(2, lecture.getTitle());
			prepared.setString(3, convertListToString(lecture.getDay()));
			prepared.setString(4, convertListToString(lecture.getMonth()));
			prepared.setInt(5, lecture.getYear());
			prepared.setString(6, lecture.getStartTime());
			prepared.setString(7, lecture.getEndTime());
			prepared.setString(8, lecture.getDescription());
			
			ResultSet rs = prepared.executeQuery();
			
			while(rs.next()){
				lectureID = rs.getInt("lid");
			}
			rs.close();
			prepared.close();
			
			
			prepared = con.prepareStatement("DELETE FROM enrolled WHERE cid=? AND email=? AND lid=?");
			
			prepared.setString(1, lecture.getCourseCode());
			prepared.setString(2, student.getEmail());
			prepared.setInt(3, lectureID);

			prepared.executeUpdate();
			prepared.close();
			con.close();
		}
		catch(SQLException e){
			if(DEBUG){
				e.printStackTrace();
			}
		}

	}
	@Override
	public ArrayList<Lecture> getLectures(Student student, String courseCode) {
		  try {
		         con = DriverManager
		            .getConnection("jdbc:postgresql://ec2-54-83-196-7.compute-1.amazonaws.com/d5d7u2801e8s1c?user=kisdjufljkdkoc&password=JPMtcrooqYqaaHc-YSjumnB97v&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory");
		      } catch (Exception e) {
		    	 if(DEBUG){
		    		 e.printStackTrace();
		    		 System.err.println(e.getClass().getName()+": "+e.getMessage());
		    	 }
		         System.exit(0);
		      }
		      
			ArrayList<Lecture> lectures = new ArrayList<Lecture>();
			boolean enrolled = false;
			try {
				
						prepared = con.prepareStatement("SELECT COUNT(*) FROM enrolled where email=? AND cid=?");
						prepared.setString(1, student.getEmail());
						prepared.setString(2, courseCode);
						ResultSet rs = prepared.executeQuery();
						
						
						while(rs.next()){
							if (rs.getString("count").equals("1")){
								enrolled = true;
							}
						}
						prepared.close();
						rs.close();
			}
			catch(SQLException e){
				if(DEBUG){
					e.printStackTrace();
				}
			}
			
			if(!enrolled)
			{
				if(DEBUG){
					System.out.println("Null was returned");
				}
				return null;
			}
			try{
			
						prepared = con.prepareStatement("SELECT cid,title,days,months,year,start_time, end_time, description FROM lecture "
								+ "WHERE cid=?");
						prepared.setString(1,  courseCode);
					
						
						ResultSet rs = prepared.executeQuery();
						
						while(rs.next()){
							ArrayList<Integer> day = splitArray(rs.getString(3));
							ArrayList<Integer> month = splitArray(rs.getString(4));
							Lecture myLecture = new Lecture(rs.getString(2), day,  month, rs.getInt(5), rs.getString(6), rs.getString(7),rs.getString(8),rs.getString(1));
							lectures.add(myLecture);
							
						}
						rs.close();
						prepared.close();
						con.close();
					
				}
				catch(SQLException e){
					if(DEBUG){
						e.printStackTrace();
					}
				}
		
		
		return lectures;
	}
	@Override
	public ArrayList<Assignment> getAssignments(Student student,String courseCode) {
		
		// TODO Auto-generated method stub
		return null;
	}

}