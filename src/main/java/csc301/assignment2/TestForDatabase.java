package csc301.assignment2;

import java.util.ArrayList;

public class TestForDatabase {
	public static void main(String[] args) {
		
		/*
		CalendarDatabase database = new CalendarDatabase();
		Student darren = new Student("dp","dp@gmail.com","123");
		
		String[] type = {"assignment"};
		ArrayList<CalendarEvent> assignments = database.getEvent(type,darren);
		for (CalendarEvent objects: assignments){
			System.out.println(objects);
		}
		*/
		CalendarDatabase database = new CalendarDatabase();
		Student darren = new Student("darren","darrenpepper55@gmail.com","123");
		
		String[] type = {"assignment"};
		ArrayList<CalendarEvent> assignments = database.getEvent(type,darren);
		Email email = new Email();
		Course course = new Course("tst111");
		Assignment assignment = (Assignment) assignments.get(0);
		email.sendEmails(assignment,"tst111","");//,"due_date: 09/09/14, due_time: 19:00, weight: 20");
		System.out.println("done");
		
		
		
		
		
		
		
		
		
		
		//CalendarEvent personal = new CalendarEvent("my title", 4, 4, 2014, "11:00", "15:00","my description");
		//database.addEvent(personal, darren);

		
		//database.removePersonal(personal, darren);
		//database.addAssignment(,);

	}
}
