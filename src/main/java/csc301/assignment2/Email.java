package csc301.assignment2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Email {
	Connection con = null;
	PreparedStatement prepared = null;
	ResultSet rs = null;
	String changed = "";
	
	public void sendEmails(Assignment assignment, String course, String change){
		changed = change;
		sendEmails(assignment,course);
	}
	
	public void sendEmails(Assignment assignment, String course){		

		final String username = "timetablesystem@gmail.com";
        final String password = "theydontlikemypasswords";

        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
          new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
          });

        try {

            Message message = new MimeMessage(session);
            Connection con = null;
        	PreparedStatement prepared = null;
        	ResultSet rs = null;
        	con = DriverManager
    	            .getConnection("jdbc:postgresql://ec2-54-83-196-7.compute-1.amazonaws.com/d5d7u2801e8s1c?user=kisdjufljkdkoc&password=JPMtcrooqYqaaHc-YSjumnB97v&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory");
    	      
        	prepared = con.prepareStatement("SELECT email FROM enrolled WHERE cid=?");
			
			prepared.setString(1,course);
			rs = prepared.executeQuery();
			String content;
			while (rs.next()){
				
				
	            message.setFrom(new InternetAddress("timetablesystem@gmail.com"));
	            message.setRecipients(Message.RecipientType.TO,
	                InternetAddress.parse(rs.getString(1)));
	            if (changed == ""){ 
	            	message.setSubject("New assignment for course "+course);
	            	content = course+" "+assignment.getDescription()+" "+assignment.getTitle()+" Due on: "+assignment.getDay()+"/"+assignment.getMonth()+"/"+assignment.getYear()+" Worth:"+assignment.getWeight();
	            }
	            else{
	            	message.setSubject("Changed assignment information for course "+course);
	            	changed = changed.replaceAll(": ","=");
	            	//changed = changed.replaceAll(", ", "AND");
	            	changed = changed.replaceAll(" ", "");

	            	content = "The following items have been changed" + changed;

	            }
	   //            message.setText("Dear Mail Crawler,"
	  //                  + "\n\n No spam to my email, please!",);
	
	            Random random = new Random();
	            String chars = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789";
	
	            String randomString = "";
	            for (int i=0; i<15; i++)
	            {
	                int index = (random.nextInt(chars.length()));
	                randomString += chars.charAt(index);
	            }
	            System.out.println(randomString);
	            //we can now have them vote from their emails.
	            message.setContent("Dear Timetable user, please vote on if the information is correct. <br/>"
	            	+ content	
	                +  "<br /> <a href=\"http://timetableplanner-votingsystem.rhcloud.com/index.php?course="+course+"&string="+randomString+"&assignment="+assignment.getTitle()+"&changed="+changed+"\" target=\"_blank\">Vote Yes</a>"
	            	+  "<br /> <a href=\"http://timetableplanner-votingsystem.rhcloud.com/voteno.php?course="+course+"&string="+randomString+"&assignment="+assignment.getTitle()+"&changed="+changed+"\" target=\"_blank\">Vote No</a>","text/html");
	
	           addGeneratedString(randomString,course,assignment.getTitle());
	           Transport.send(message);
	           System.out.println("Sent Emails");
	        }
        } catch (MessagingException | SQLException e) {
            throw new RuntimeException(e);
        }
		
		
	}
	
	private void addGeneratedString(String string, String course,String Assignmentname){
		try {
			con = DriverManager
		            .getConnection("jdbc:postgresql://ec2-54-83-196-7.compute-1.amazonaws.com/d5d7u2801e8s1c?user=kisdjufljkdkoc&password=JPMtcrooqYqaaHc-YSjumnB97v&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory");
		    PreparedStatement prepared2 = con.prepareStatement("SELECT aid FROM assignment WHERE title=? and cid=?");
			prepared2.setString(1,Assignmentname);
		    prepared2.setString(2,course);
			rs = prepared2.executeQuery();
			while(rs.next()){
			
				int aid = rs.getInt(1);
				prepared = con.prepareStatement(
						"INSERT INTO generated(cid, String, assignment)" +
                        "VALUES (?,?,?)");

				prepared.setString(1, course);
				prepared.setString(2, string);
				prepared.setInt(3,aid);
				prepared.executeUpdate();
			}
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
