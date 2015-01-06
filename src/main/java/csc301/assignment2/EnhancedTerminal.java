package csc301.assignment2;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Hashtable;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.ScreenCharacterStyle;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalSize;
import com.googlecode.lanterna.terminal.swing.SwingTerminal;
import com.googlecode.lanterna.terminal.swing.TerminalAppearance;
import com.googlecode.lanterna.terminal.swing.TerminalPalette;

/**
 * EnhancedTerminal is responsible for creating a "GUI-Like" interface, which
 * is in a enhanced terminal. This terminal will use the Lantera Library, and
 * thus have extra features such as colors, styles effects (bold, underline).
 * 
 * Once the printing on the window passes below the screen, the screen is cleared
 * and printing starts from the top. The User can use the up and down arrow keys to
 * see old buffers (old screens).
 * 
 * @author Shameel Khan
 *
 */
public class EnhancedTerminal {
	
	// Get monitor sizes
	private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static double width = screenSize.getWidth();
	public static double height = screenSize.getHeight();
	// Apply monitor sizes and create terminal screen
	private static TerminalSize terminalSize = new TerminalSize((int)(width/9), (int)(height/20));
	
	
	static Font font = new Font(Font.DIALOG_INPUT, Font.CENTER_BASELINE, 15);

	static TerminalAppearance tApp = new TerminalAppearance(font, font, TerminalPalette.DEFAULT, false); 
	
	private static Screen screen = TerminalFacade.createScreen(new SwingTerminal(terminalSize)); // create screen
	
	
	//private static Screen screen = TerminalFacade.createScreen(new SwingTerminal(tApp, (int)(width/9), (int)(height/20))); // create screen
	public static boolean onCurrentBuffer = true, storeHistory = false, returnMode = false;
	// yPostion tracks the y position on the screen of the last print statement
	// bufferNumber tracks the current buffer(screen) the user is viewing
	public static int yPosition = 0, bufferNumber = 0; 
	// allBuffers keeps a record of all previous buffers.
	private static ArrayList<ArrayList<String>> allBuffers = new ArrayList<ArrayList<String>>();
	// the current buffer on which the user is typing
	private static ArrayList<String> currentBuffer = new ArrayList<String>();

	public static Terminal.Color black = Terminal.Color.BLACK, green = Terminal.Color.GREEN, white = Terminal.Color.WHITE, red = Terminal.Color.RED,
			cyan = Terminal.Color.CYAN;
	public static ScreenCharacterStyle bold = ScreenCharacterStyle.Bold, underline = ScreenCharacterStyle.Underline, 
			blink = ScreenCharacterStyle.Blinking;

	private static ArrayList<String> historyBuffer = new ArrayList<String>(); // stores all inputs types by user
	private static int historyNumber = 0;
	
	// set up for auto filling
	private static Hashtable<String, String[]> commandsMap;
	public static String currentCommandLevel;
	public static int currentCommandIndex;
	
	//Used by printg to move the x position
	int xCounter = 0;
	public EnhancedTerminal () {};
	
	public static void addCommandMap(Hashtable<String, String[]> commandsMap, String defaultCommand)
	{
		currentCommandLevel = defaultCommand;
		EnhancedTerminal.commandsMap = commandsMap;
	}
	/**
	 * Start the screen in order for window to load	
	 */
	public void startScreen() {
		System.out.println("width: " + width + ", Height: " + height);
		screen.startScreen();
	}
	
	/**
	 * Stop the screen in order for window to exit
	 */
	public void stopScreen() {
		screen.stopScreen();
	}
	
	public void clearScreen() {
		screen.clear();
	}
	
	public int getHeight() {
		return screen.getTerminalSize().getRows();
	}
	
	public int getWidth() {
		return screen.getTerminalSize().getColumns();
	}
	
	/**
	 * Prints the given 'output' string onto the screen at position 'x' and 'y'
	 * with foreground color of 'fgColor' and background color or 'bgColor', and
	 * applies ScreenCharacterSyles of bold and underline.
	 * 
	 * After printing increments the yPostion so that next print call will print on new line.
	 * 
	 * Also if the current print call will print below the screen, the screen is refreshed, with
	 * old buffer stored in allBuffers for future access. The printing continues from top.
	 * @param x
	 * @param y
	 * @param output
	 * @param bgColor
	 * @param fgColor
	 * @param styles
	 */
	public void printg(int x, int y, String output, Terminal.Color bgColor, Terminal.Color 
			fgColor, ScreenCharacterStyle... styles)
	{		
		if(xCounter != 0)
			x = xCounter;
		
		// if current print call passes the below the screen, refresh
		// and continue from top
		if(y >= screen.getTerminalSize().getRows()){			
			allBuffers.add(currentBuffer); // add the old buffer to allBuffers
			currentBuffer = new ArrayList<String>(); // make a new currentBuffer
			// reset yPosition
			yPosition = 1;
			y = 1;
			screen.clear();
			bufferNumber = allBuffers.size(); // update to latest buffer
		}
						
		String stylesRecord = "";		
		
		// Apply the styles
		if(styles.length == 0){
			screen.putString(x, y, output, bgColor, fgColor);
		}
		else if(styles.length == 1){
			screen.putString(x, y, output, bgColor, fgColor, styles[0]);
			stylesRecord = "/#/" + styles[0].name();
		}
		else if(styles.length == 2){
			screen.putString(x, y, output, bgColor, fgColor, styles[0], styles[1]);
			stylesRecord = "/#/" + styles[0].name() + "/#/" + styles[1].name();
		}
		
		currentBuffer.add(x + "/#/" + y + "/#/" + output + "/#/" 
				+ bgColor.name() + "/#/" + fgColor.name() + stylesRecord); // store line printed to current buffer
		
		xCounter = x + output.length();
		screen.refresh();
	}
			
	
	/**
	 * Prints the given 'output' string onto the screen at position 'x' and 'y'
	 * with foreground color of 'fgColor' and background color or 'bgColor', and
	 * applies ScreenCharacterSyles of bold and underline.
	 * 
	 * After printing increments the yPostion so that next print call will print on new line.
	 * 
	 * Also if the current print call will print below the screen, the screen is refreshed, with
	 * old buffer stored in allBuffers for future access. The printing continues from top.
	 * @param x
	 * @param y
	 * @param output
	 * @param bgColor
	 * @param fgColor
	 * @param styles
	 */
	public void print(int x, int y, String output, Terminal.Color bgColor, Terminal.Color 
			fgColor, ScreenCharacterStyle... styles)
	{		
		// if current print call passes the below the screen, refresh
		// and continue from top
		if(y >= screen.getTerminalSize().getRows()){			
			allBuffers.add(currentBuffer); // add the old buffer to allBuffers
			currentBuffer = new ArrayList<String>(); // make a new currentBuffer
			// reset yPosition
			yPosition = 1;
			y = 1;
			screen.clear();
			bufferNumber = allBuffers.size(); // update to latest buffer
		}
		
		yPosition += 1;
		
		String stylesRecord = "";		
		
		// Apply the styles
		if(styles.length == 0){
			screen.putString(x, y, output, bgColor, fgColor);
		}
		else if(styles.length == 1){
			screen.putString(x, y, output, bgColor, fgColor, styles[0]);
			stylesRecord = "/#/" + styles[0].name();
		}
		else if(styles.length == 2){
			screen.putString(x, y, output, bgColor, fgColor, styles[0], styles[1]);
			stylesRecord = "/#/" + styles[0].name() + "/#/" + styles[1].name();
		}
		
		
		currentBuffer.add(x + "/#/" + y + "/#/" + output + "/#/" 
				+ bgColor.name() + "/#/" + fgColor.name() + stylesRecord); // store line printed to current buffer
		
		xCounter = 0; // because we are new line
		screen.refresh();
	}
	
	/**
	 * Given a X and Y position, print the old, stored history inputs.
	 * @param xPosition
	 * @param yPosition
	 * @return
	 */
	public String[] printHistoryLine(int xPosition, int yPosition) 
	{
		String storedLine[];	
		
		System.out.println("history number: " + historyNumber);
		
		storedLine = historyBuffer.get(historyNumber).split("/#/");
		
		System.out.println("Line to add: " + storedLine[2] + " x: " + Integer.parseInt(storedLine[0]) + " y:" + Integer.parseInt(storedLine[1]));
		
		screen.putString(xPosition, yPosition, "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t", white, black);
		screen.putString(xPosition, yPosition, storedLine[2], white, black);
		screen.refresh();
		
		System.out.println("XPOSITON 2: " + storedLine[0]);
		String[] retInfo = new String[2];
		retInfo[0] = storedLine[0];
		retInfo[1] = storedLine[2];
		return retInfo;
	}
	
	public void printAutoCommand(int xPosition, int yPosition, String command)
	{
		screen.putString(xPosition, yPosition, "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t", white, black);
		screen.putString(xPosition, yPosition, command, white, black);
		screen.refresh();
	}
	
	/**
	 * Given a buffer, which is an ArrayList of Strings: each String representing a 
	 * line to print, prints the buffer to screen.
	 * @param buffer
	 */
	public void printBuffer(ArrayList<String> buffer)
	{				
		screen.clear(); // clear old screen
		// for each line in buffer, print line
		for(String line : buffer){
			String[] lineArgs = line.split("/#/");
			
			if(lineArgs.length > 2){
				Terminal.Color bgColor = Terminal.Color.valueOf(lineArgs[3]);
				Terminal.Color fgColor = Terminal.Color.valueOf(lineArgs[4]);
				
				if(lineArgs.length == 5)
					screen.putString(Integer.parseInt(lineArgs[0]), Integer.parseInt(lineArgs[1]), 
						lineArgs[2], bgColor, fgColor);
				
				if(lineArgs.length == 6)
					screen.putString(Integer.parseInt(lineArgs[0]), Integer.parseInt(lineArgs[1]), 
						lineArgs[2], bgColor, fgColor, ScreenCharacterStyle.valueOf(lineArgs[5]));
				
				if(lineArgs.length == 7)
					screen.putString(Integer.parseInt(lineArgs[0]), Integer.parseInt(lineArgs[1]), 
						lineArgs[2], bgColor, fgColor, 
						ScreenCharacterStyle.valueOf(lineArgs[5]), ScreenCharacterStyle.valueOf(lineArgs[6]));
				
				screen.refresh();
			}
		}
	}
	
	/**
	 * Given a bufferIndex, prints the buffer which corresponds to that index
	 * @param bufferIndex
	 */
	public void printBuffer(int bufferIndex)
	{
		printBuffer(allBuffers.get(bufferIndex));
	}
	
	
	/**
	 * Prompts the user to type in an input. An input can be any character or the up/down
	 * arrow keys.
	 * 
	 * The prompt starts on the screen at given position 'xPostion' and 'yPostion'. 
	 * 
	 * If user types characters that is reflected on the screen instantly. This method is 
	 * similar to a scanner.readLine().
	 * 
	 * If user presses the up/down arrow that switches between buffers (old/current)
	 * 
	 * @param xPosition
	 * @param yPosition
	 * @return
	 * @throws InterruptedException
	 */
	public String getInput(int xPosition, int yPosition) throws InterruptedException
	{
		boolean gettingInput = true;
		StringBuilder sb = new StringBuilder();
		int originalXPos = xPosition;
		String[] temp = null;
		while(gettingInput){			
			
			Key key = screen.readInput(); // get key user pressed
			
			while(key == null){ // wait until user presses a key
				key = screen.readInput();
				Thread.sleep(1);
			}
			
			// Check which key user pressed 
			switch(key.getKind()){
				case NormalKey: // normal keys are character keys
					if(!onCurrentBuffer){
						printBuffer(currentBuffer);
						onCurrentBuffer = true;					
					}
					
					bufferNumber = allBuffers.size(); // on the current buffer
					returnMode = false; // we are back to just printing and asking user
					screen.putString(xPosition++, yPosition, String.valueOf(key.getCharacter()), white, black);
					screen.refresh();
					sb.append(key.getCharacter());
					break;
				
				case Backspace:
					if(xPosition != originalXPos){
						screen.putString(--xPosition, yPosition, " ", white, black);
						sb.deleteCharAt(sb.length() - 1);
						screen.refresh();
					}
					returnMode = false;
					break;
					
				case Enter:
					gettingInput = false;
					returnMode = false;
					break;
					
				case ArrowUp: // print the old buffer before this buffer
					
					if(bufferNumber >= 1){
						bufferNumber--;
						printBuffer(bufferNumber);
						onCurrentBuffer = false;
					}
					returnMode = false;
					break;
					
				case ArrowDown: // print the next buffer after this buffer
					
					if(bufferNumber + 1 >= allBuffers.size()){
						bufferNumber = allBuffers.size();
						printBuffer(currentBuffer);
						onCurrentBuffer = true;
					}
					else{
						bufferNumber++;
						printBuffer(bufferNumber);
						onCurrentBuffer = false;
					}					
					returnMode = false;
					break;
					
				case ArrowRight: //down
					if(!returnMode) {
						if(historyNumber > 0)
							historyNumber--;
						
						if(historyBuffer.size() > 0){
							temp = printHistoryLine(originalXPos, yPosition);
							
							xPosition = originalXPos + temp[1].length();
							//xPosition = Integer.parseInt(temp[0]);
							sb = new StringBuilder();
							sb.append(temp[1]);
						}
					}
					else {
						return "ArrowRight";
					}
					break;
					
				case ArrowLeft: 
					if(!returnMode) {
						if(historyNumber < historyBuffer.size() - 1 ){
							historyNumber++;
						
						temp = printHistoryLine(originalXPos, yPosition);
						xPosition = originalXPos + temp[1].length();
						//xPosition = Integer.parseInt(temp[0]);
						sb = new StringBuilder();
						sb.append(temp[1]);
						}
					} 
					else {
						return "ArrowLeft";
					}
					break;
					
				case Tab: 
					String[] currentCommands = commandsMap.get(currentCommandLevel);
					if (currentCommandIndex > currentCommands.length - 1){
						currentCommandIndex = 0;
						String command = currentCommands[currentCommandIndex++];
						xPosition = originalXPos + command.length();
						printAutoCommand(originalXPos, yPosition, command);
						sb = new StringBuilder();
						sb.append(command);
					}
					else {
						String command = currentCommands[currentCommandIndex++];
						xPosition = originalXPos + command.length();
						printAutoCommand(originalXPos, yPosition, command);
						sb = new StringBuilder();
						sb.append(command);
					}
					break;
										
			
			}			
		}
		System.out.println("XPOS: " + xPosition);
		
		String storeStr = originalXPos + "/#/" + yPosition + "/#/" + sb.toString() + "/#/" + "WHITE" + "/#/" + "BLACK";
		
		currentBuffer.add(storeStr);
		
		if(storeHistory) {
			if(!sb.toString().trim().equals("")) {		
				historyBuffer.add(0, xPosition + "/#/" + yPosition + "/#/" + sb.toString().replaceAll("^\\s+","") + "/#/" + "WHITE" + "/#/" + "BLACK");
				historyNumber = 0; // reset last entered data
			}
		}
		return sb.toString().trim();			
	}
	
		
	/**
	 * Given the 'terminalSize' and 'string' return the x postion that will
	 * center the string if it were to be printed.
	 * @param terminalSize
	 * @param string
	 * @return
	 */
	public int getMiddle(String string)
	{
		TerminalSize terminalSize = screen.getTerminalSize();
		return (terminalSize.getColumns() / 2) - (string.length() / 2);
	}	
	
	
	public void printDivider (String divide) {
		String repeated = new String(new char[terminalSize.getColumns()/2]).replace("\0", divide);
		print(getMiddle(repeated), yPosition, repeated, green, black, bold);
	}
}
