package csc301.assignment2;

public class Instructor extends User {
	
	//priority is 1 for the instructor
	private int priority;
	
	public Instructor (String n, String e, String p){
		super(n, e, p);
		priority = 1;
	}
	
	public int getPriority(){
		return priority;
	}
}
