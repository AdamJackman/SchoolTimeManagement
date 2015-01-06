package csc301.assignment2;

public interface UserDAO {

	
	/**
	 * This method is to check if the provided information by user
	 * is correct, such that such a user exists
	 * @param email
	 * @param password
	 * @return 
	 */
	public boolean logIn(String email, String password);
	
	/**
	 * This method is used to register a user with a name of name,
	 * email of email, and password or password. Checks if no such user
	 * already exists.
	 * @param name
	 * @param email
	 * @param password
	 * @return
	 */
	public Student registerUser(String name, String email, String password);
	
}


