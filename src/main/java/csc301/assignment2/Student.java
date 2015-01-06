package csc301.assignment2;

public class Student extends User {
	
	private int priority;
	
	/*
	 * A Student is a type of user with a priority level of 0, which means that
	 * a student has less privileges than other types of users. Student is currently
	 * the only type of User.
	 */
	public Student (String name, String email, String password) {
		super(name, email, password);		
		this.priority = 0;
	}
	
	public int getPriority() {
		return this.priority;
	}
}
