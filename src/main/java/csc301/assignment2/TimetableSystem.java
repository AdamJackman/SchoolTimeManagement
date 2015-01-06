package csc301.assignment2;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;

import com.googlecode.lanterna.screen.ScreenCharacterStyle;
import com.googlecode.lanterna.terminal.Terminal;

/**
 * TimetableSystem is responsible for running the main program. Asks
 * user initial command input, and calls Interpreter to handle the given
 * input.
 * 
 * @author Shameel Khan
 *
 */
public class TimetableSystem {
	
	private static EnhancedTerminal eTerminal = new EnhancedTerminal();
	public static Student student;
	private String username = "", password, email;	
	public static boolean isRunning = true, loggedIn = false;	
	// load screen character styles from EnhancedTerminal in order to
	// use them easily (i.e so that can have simpler names)
	private Terminal.Color black = EnhancedTerminal.black, green = EnhancedTerminal.green,
			white = EnhancedTerminal.white, red = EnhancedTerminal.red, cyan = EnhancedTerminal.cyan;
	private static ScreenCharacterStyle bold = EnhancedTerminal.bold, underline = EnhancedTerminal.underline, 
			blink = EnhancedTerminal.blink;
	private Interpreter interpreter = new Interpreter();
	
	/**
	 * Runs the main program. Asks user to log in and type other commands such as add, show, exit.
	 * Keeps looping until user types exit, then isRunning becomes false.
	 * @param args
	 * @throws InterruptedException
	 * @throws FileNotFoundException
	 */
	public static void main (String[] args) throws InterruptedException, FileNotFoundException{
			
		TimetableSystem timeTable = new TimetableSystem();
		String[] commandsArray1 = {"login", "register"};
		String[] commandsArray2 = {"add", "show", "change", "remove", "logout", "exit"};
		String[] commandsArray3 = {"course", "assignment", "lecture", "personal"};
		String[] commandsArray4 = {"assignments", "lectures", "month"};
		String[] commandsArray5 = {"course", "lecture", "personal"};
		String[] commandsArray6 = {"title", "list"};
		Hashtable<String, String[]> commandsMap = new Hashtable<String, String[]>();
		
		commandsMap.put("1", commandsArray1);
		commandsMap.put("2", commandsArray2);
		commandsMap.put("add", commandsArray3);
		commandsMap.put("show", commandsArray4);
		commandsMap.put("remove", commandsArray5);
		commandsMap.put("remove personal", commandsArray6);
		EnhancedTerminal.addCommandMap(commandsMap, "1");		
		
		
		eTerminal.startScreen(); // open window
		
		// Print welcome Message
		timeTable.printWelcomeMsg();
		int height = eTerminal.getHeight();
		Thread.sleep(200);
		
		// Set up asking user for input
		String blinker = ">> ";
		String commandMsg = "Type One of the Commands (Add, Show, Change, Remove, Logout, Exit):  ";
		String userInput, input;		
		Interpreter interpreter = new Interpreter();	
		
		// 2 placed down
		eTerminal.print(2, EnhancedTerminal.yPosition, "", timeTable.white, timeTable.black);
		eTerminal.print(2, EnhancedTerminal.yPosition, "", timeTable.white, timeTable.black);
		
		timeTable.loadScreen();
		eTerminal.print(0, height, "", timeTable.white, timeTable.black);
		
		while(isRunning){
			if(loggedIn){ // if user is logged in		
				// print command message to screen
				EnhancedTerminal.currentCommandLevel = "2";
				eTerminal.printg(2, EnhancedTerminal.yPosition, blinker, eTerminal.cyan, eTerminal.black, bold, blink);
				eTerminal.printg(4, EnhancedTerminal.yPosition, commandMsg, eTerminal.white, eTerminal.black, bold);		
				
				// Get user input
				userInput = eTerminal.getInput(commandMsg.length() + 5, EnhancedTerminal.yPosition).trim();
				eTerminal.print(2, EnhancedTerminal.yPosition, blinker, eTerminal.white, eTerminal.black, bold); // to stop the blinking
				input = userInput.toLowerCase();	
				// Send user input to Interpreter, which will interpret the command typed by user
				if(!input.trim().equals("")){
					interpreter.interpret(input); 
				}
				EnhancedTerminal.currentCommandIndex = 0;
			}
			else{		
				timeTable.startOptions();					
			}	
		}		
		
		Thread.sleep(1000);
		eTerminal.stopScreen(); // close screen
	}
	
	
	/**
	 * Ask user to login or register.
	 * 
	 * @throws InterruptedException
	 */
	public void startOptions() throws InterruptedException
	{
		
		String msg = "What would you like do to? (login, register, exit): ";
		eTerminal.print(2, EnhancedTerminal.yPosition, msg, white, black, bold);
		
		String userInput = eTerminal.getInput(msg.length() + 2, EnhancedTerminal.yPosition - 1); // 4
		
		userInput = userInput.trim().toLowerCase();
		if(userInput.equals("login")){
			logInMessage();
		}
		else if(userInput.equals("register")){
			registerMessage();
		}
		else if(userInput.equals("exit")){
			isRunning = false;
		}
	}
	
	
	
	/**
	 * Prompt user to log in. Call Interpreter to check if provided information
	 * is an actual user in system.
	 * @throws InterruptedException
	 */
	public  void logInMessage() throws InterruptedException
	{
		String logInMsg = "Please Log In";
		eTerminal.print(eTerminal.getMiddle(logInMsg), EnhancedTerminal.yPosition, 
				logInMsg, red, black, bold, underline);		
		
		// get email
		eTerminal.print(eTerminal.getMiddle("Email: ") - 4, EnhancedTerminal.yPosition, "Email: ",  white, black);
		email = eTerminal.getInput(eTerminal.getMiddle("Email: ") + 3, EnhancedTerminal.yPosition - 1); // 5
		
		// get password
		eTerminal.print(eTerminal.getMiddle("Password: ") - 4, 
				EnhancedTerminal.yPosition, "Password: ",  white, black);
		password = eTerminal.getInput(eTerminal.getMiddle("Password: ") + 6, EnhancedTerminal.yPosition - 1); // 5
		
		// check if user had provided correct information				
		
		loggedIn = interpreter.checkLogIn(email, password);
		if(loggedIn == false){
			eTerminal.print(0, EnhancedTerminal.yPosition, "",  red, black);
			eTerminal.print(eTerminal.getMiddle("Incorrect Information"), 
					EnhancedTerminal.yPosition, "Incorrect Information",  red, black, bold);
		}
		else{
			student = new Student(username.toLowerCase().trim(), email, password);
			EnhancedTerminal.storeHistory = true; // we can resume storing history
		}
	}
	
	/**
	 * Prompt user to register
	 * @throws InterruptedException
	 */
	public void registerMessage() throws InterruptedException {
		
	
		String msg = "Please provide your name: ";
		eTerminal.print(5, EnhancedTerminal.yPosition, msg, cyan, black, bold);
		String name = eTerminal.getInput(msg.length() + 5, EnhancedTerminal.yPosition - 1);
		
		msg = "Please provide a password: ";
		eTerminal.print(5, EnhancedTerminal.yPosition, msg, cyan, black, bold);
		String password = eTerminal.getInput(msg.length() + 5, EnhancedTerminal.yPosition - 1);
		
		msg = "Please provide your email: ";
		eTerminal.print(5, EnhancedTerminal.yPosition, msg, cyan, black, bold);
		String email = eTerminal.getInput(msg.length() + 5, EnhancedTerminal.yPosition - 1);
		
		Interpreter interpreter = new Interpreter();
		TimetableSystem.student = interpreter.checkRegister(name, email, password);
		
		if(student != null)
		{
			loggedIn = true;
			//username = student.getName();
			//password = student.getPassword();
			//email = student.getEmail();
			//student = new Student(username, email, password);
		}
			
	}
	
	
	
	public void loadScreen() throws InterruptedException {
		printWelcomeMsg();
		
		int y = 10;
		String msg = "Manage your time the way you want! Here you can add your courses, lectures, assignments, and";
		eTerminal.print(eTerminal.getMiddle(msg), y++, msg, white, black, bold);
		
		msg = "personal events! View your monthly calendar, due dates and much more. Also you don't have to relay";
		eTerminal.print(eTerminal.getMiddle(msg), y++, msg, white, black, bold);
		
		msg = "on your LAZY profs to update assignment due dates. You and your friends can change the due dates yourself!";
		eTerminal.print(eTerminal.getMiddle(msg), y++, msg, white, black, bold);
		
		msg = "Have fun!";
		eTerminal.print(eTerminal.getMiddle(msg), y++, msg, white, black, bold);
		y++; y++; y++; y++;
		
		msg = "Press Enter To Continue";
		eTerminal.print(eTerminal.getMiddle(msg), y++, msg, white, black, bold, blink);
		
		addGraphic();
		
		
		eTerminal.getInput(-1, -1);
		eTerminal.clearScreen();
		EnhancedTerminal.yPosition = 1;
		
	}
	
		
	/**
	 * Prints the welcome message to screen, the message is
	 * stored inside 'titleMsg'
	 * @param titleMsg
	 */
	public void printWelcomeMsg()
	{
		int y = 1;
		String t = "", hold="";
		
		t= "  _    _        _                                _____       __   __                    _____  _                   _          _      _       ";
		hold = t;
		eTerminal.print(eTerminal.getMiddle(hold), y++, t, green, black, 
				bold);
		
		t=" | |  | |      | |                              |_   _|      \\ \\ / /                   |_   _|(_)                 | |        | |    | |      ";
		
		eTerminal.print(eTerminal.getMiddle(hold), y++, t, green, black, 
				bold);
		
		t=" | |  | |  ___ | |  ___  ___   _ __ ___    ___    | |  ___    \\ V / ___   _   _  _ __    | |   _  _ __ ___    ___ | |_  __ _ | |__  | |  ___ ";
		
		eTerminal.print(eTerminal.getMiddle(hold), y++, t, green, black, 
				bold);
		t=" | |/\\| | / _ \\| | / __|/ _ \\ | '_ ` _ \\  / _ \\   | | / _ \\    \\ / / _ \\ | | | || '__|   | |  | || '_ ` _ \\  / _ \\| __|/ _` || '_ \\ | | / _ \\ ";
		
		eTerminal.print(eTerminal.getMiddle(hold), y++, t, green, black, 
				bold);
		
		t=" \\  /\\  /|  __/| || (__| (_) || | | | | ||  __/   | || (_) |   | || (_) || |_| || |      | |  | || | | | | ||  __/| |_| (_| || |_) || ||  __/ ";
		
		eTerminal.print(eTerminal.getMiddle(hold), y++, t, green, black, 
				bold);
		t="  \\/  \\/  \\___||_| \\___|\\___/ |_| |_| |_| \\___|   \\_/ \\___/    \\_/ \\___/  \\__,_||_|      \\_/  |_||_| |_| |_| \\___| \\__|\\__,_||_.__/ |_| \\___| ";
		                                                                                                                                             
		eTerminal.print(eTerminal.getMiddle(hold), y++, t, green, black, 
				bold);
		
	}
	
	public void addGraphic () {
		int x = 5;
		int y = 8;
		
		for (int i = 0; i <= eTerminal.getWidth() - 13; i++) {
			eTerminal.print(x++, y, "  ", EnhancedTerminal.green, EnhancedTerminal.green, EnhancedTerminal.bold);

			try {
				Thread.sleep(7);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for(int i = 0; i <= 15; i++) {
			eTerminal.print(x, y++, "  ", EnhancedTerminal.cyan, EnhancedTerminal.green, EnhancedTerminal.bold);
			try {
				Thread.sleep(7);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		for (int i = 0; i <= eTerminal.getWidth() - 13; i++) {
			eTerminal.print(x--, y, "  ", EnhancedTerminal.cyan, EnhancedTerminal.green, EnhancedTerminal.bold);
			try {
				Thread.sleep(7);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		for(int i = 0; i <= 15; i++) {
			eTerminal.print(x, y--, "  ", EnhancedTerminal.cyan, EnhancedTerminal.green, EnhancedTerminal.bold);
			try {
				Thread.sleep(7);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
