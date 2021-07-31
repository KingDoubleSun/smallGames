/**
 * Exception class for invalid data format
 * @author Shuwei Liu, studentID: 847046
 */
public class InvalidDataFormatException extends Exception {
	
	public InvalidDataFormatException() 
	{
		super("WARNING: invalid data format in config file in line ");
	}
	
	public InvalidDataFormatException(String message) 
	{
		super(message);
	}
}
