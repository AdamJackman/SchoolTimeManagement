package csc301.assignment2;

import java.sql.*;

import encryption.BCrypt;


public class UserInDatabase implements UserDAO {
	//manually change in order to allow / disallow debug prints true==ON, false==OFF
	final boolean DEBUG = false;
	
	@Override
	public Student registerUser(String name, String email, String password){
		
		//Check the form of email
		if (!email.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")){
			return null;
		}
		
		//Make the connection
		Connection con = null;
		try{
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection("jdbc:postgresql://ec2-54-83-196-7.compute-1.amazonaws.com/d5d7u2801e8s1c?user=kisdjufljkdkoc&password=JPMtcrooqYqaaHc-YSjumnB97v&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory");
		}
		catch(ClassNotFoundException e){
			if (DEBUG){
				System.err.println("Class not found exception: " + e.getMessage());
			}
			con = null;
		}
		catch(SQLException e){
			if(DEBUG){
				System.err.println("SQLException: " + e.getMessage());
			}
			con = null;
		}
		
		//First check if the user is already in the database
		//SELECT * FROM user WHERE email= email
		try{
			String existQuery = "SELECT COUNT(*) FROM users WHERE email=?;";
			PreparedStatement existPS = con.prepareStatement(existQuery);
			existPS.setString(1, email);
			ResultSet rs = existPS.executeQuery();
			while (rs.next()){
				if (rs.getString("count").equals("1")){
					con.close();
					return null; // user must exist, so return null
				}
			}
			rs.close();
			existPS.close();
		}
		catch(SQLException e){
			if (DEBUG){
				System.err.println("SQLException: " + e.getMessage());
			}
		}
		
						
		//As the user is not in the database we now add them
		try{
			String hashed_password = BCrypt.hashpw(password, BCrypt.gensalt());
			if(DEBUG){
				System.out.println(hashed_password);
			}
			String insertQuery = "INSERT INTO users VALUES (?,?,?);";
			PreparedStatement insertPS = con.prepareStatement(insertQuery);
			
			insertPS.setString(1, name);
			insertPS.setString(2, email);
			insertPS.setString(3, hashed_password);
			
			insertPS.executeUpdate();
			insertPS.close();
			
			//Insert is successful therefore create the student object and return it
			Student student = new Student(name, email, hashed_password);
			con.close();
			return student;
		}
		catch(SQLException e){
			if (DEBUG){
				System.err.println("SQLException: " + e.getMessage());
			}
		}
		//catching returning null
		con = null;
		return null;
	}
	
	@Override
	public boolean logIn(String email, String password){
		
		if (!email.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")){
			if (DEBUG){
				System.err.println("Email of Invalid form");
			}
			return false;
		}
		
		//Check if the provided information is a match
		//Make the connection
		Connection con = null;
		try{
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection("jdbc:postgresql://ec2-54-83-196-7.compute-1.amazonaws.com/d5d7u2801e8s1c?user=kisdjufljkdkoc&password=JPMtcrooqYqaaHc-YSjumnB97v&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory");
		}
		catch(ClassNotFoundException e){
			if(DEBUG){
				System.err.println("Class not found exception: " + e.getMessage());
			}
			con = null;
		}
		catch(SQLException e){
			if(DEBUG){
				System.err.println("SQLException: " + e.getMessage());
			}
			con = null;
		}
		
		//Check to see if a matched result appears
		try{
			String query = "SELECT password FROM users WHERE email=?;";			
			PreparedStatement ps = con.prepareStatement(query);
			
			ps.setString(1, email);
			
			ResultSet rs2 = ps.executeQuery();
			
			String hashed_password = null; 
			while (rs2.next()){
				hashed_password = rs2.getString("password");
			}
			rs2.close();
			ps.close();
			if (hashed_password != null && BCrypt.checkpw(password, hashed_password)){
				return true;
			}
		}
		catch(SQLException e){
			if(DEBUG){
				System.err.println("SQLException: " + e.getMessage());
			}
		}
		
		return false;
	}
}
