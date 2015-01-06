package csc301.assignment2;

public class User {
	public String name;
	public String email;
	public String password;
	
	/*
	 * A User is anyone that can use the program, currently the only Users are Students. A User
	 * knows its name, email address, and password.
	 */
	public User(String name, String email, String password) {
		this.name = name;
		this.email = email; 
		this.password = password;
	}
	
	/* 
	 * Name of a user cannot change, thus there is no setter for name
	 */
	public String getName() {
		return this.name;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}	
}
