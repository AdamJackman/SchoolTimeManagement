package csc301.assignment2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class UserInMemory implements UserDAO{

	String PATH="src/main/resources/";
	
	@Override
	public Student registerUser(String name, String email, String password){
		
		boolean alreadyExists = false;
		name = name.trim().toLowerCase();
		email = email.trim().toLowerCase();
		password = password.trim();
		
		// First check if such a user already exists
		try {
			Scanner userScanner = new Scanner(new File("src/main/resources/users.txt"));
			while(userScanner.hasNext()){
				
				String userData = userScanner.nextLine();
				String[] userDateList = userData.split(",");
				
				if(email.equals(userDateList[1].trim())){ //if such an email is already present
					alreadyExists = true;
					break;	
				}
			}
			
			userScanner.close();
			// if no such user exists, then add a user
			if(!alreadyExists){
				PrintWriter writer = new PrintWriter(new FileOutputStream(new File("src/main/resources/users.txt"),true));
				writer.println(name + ", " + email + ", " + password);	
				writer.close();
				// make the user file in user folder
				File userUserFile = new File("src/main/resources/users/" + name + ".txt");
				userUserFile.createNewFile();
				
				// make the user file in event folder
				File userEventFile = new File("src/main/resources/events/" + name + ".txt");
				userEventFile.createNewFile();
			}
			else 
				return null;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new Student(name, email, password);
	}
	
	@Override
	public boolean logIn(String email, String password)
	{
		boolean loggedIn = false;
		Scanner userScanner;
		
		try {
			userScanner = new Scanner(new File("src/main/resources/users.txt"));
			
			while(userScanner.hasNext())
			{
				String userData = userScanner.nextLine();
				String[] userDateList = userData.split(",");
				
				if(email.equals(userDateList[1].trim()) 
						&& password.equals(userDateList[2].trim())){
					loggedIn = true;
					break;
				}
			}
			userScanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		return loggedIn;
	}
	
}