package csc301.assignment2;

public class Assignment extends CalendarEvent {
	private int weight;
	private boolean verified;
		
	public Assignment(String title, int day, int month, int year, String startTime, String endTime, String description, int weight,  boolean verified) {
		super(title, day, month, year, startTime, endTime, description);
		this.weight = weight;
		this.verified = verified;
	}
	public Assignment(String title, int day, int month, int year, String startTime, String endTime, String description, int weight) {
		this(title, day, month, year, startTime, endTime, description,weight, false);
	}
	
	public int getWeight(){
		return weight;
	}
	public boolean getVerified(){
		return verified;
	}
	
}
