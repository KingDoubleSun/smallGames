package ethicalengine;

/**
 * This class contains all relevant information about a presented scenario, 
 * including the car's passengers and the pedestrians on the street as well as whether
 * the pedestrians are crossing legally.
 * @author Shuwei Liu, studentID: 847046
 *
 */

public class Scenario {
	// attributes of the scenario
	private Character[] passengers;
	private Character[] pedestrians;
	private boolean isLegalCrossing;
	
	/**
	 * The constructor to create a scenario.
	 * @param passengers an array contains instances of Character which are passengers.
	 * @param pedestrians an array contains instances of Character which are pedestrians.
	 * @param isLegalCrossing a boolean indicates whether pedestrians is legal crossing.
	 */
	public Scenario(Character[] passengers, 
			        Character[] pedestrians, boolean isLegalCrossing) 
	{
		this.passengers = passengers;
		this.pedestrians = pedestrians;
		this.isLegalCrossing = isLegalCrossing;
	}
	
	/**
	 * returns a boolean indicating whether you(the user) are in the car.
	 * @return a boolean indicating whether you(the user) are in the car.
	 */
	public boolean hasYouInCar() 
	{
		for(Character p: passengers) 
		{
			if(p instanceof Person) 
			{
				if(((Person) p).isYou())
					return true;
			}
		}
		return false;
	}
	
	/**
	 * returns a boolean indicating whether you(the user) are in the lane.
	 * @return a boolean indicating whether you(the user) are in the lane.
	 */
	public boolean hasYouInLane() 
	{
		for(Character p: pedestrians) 
		{
			if(p instanceof Person)
				if(((Person) p).isYou())
					return true;
		}
		return false;
	}
	
	/**
	 * returns the cars' passengers as a Character array.
	 * @return the cars' passengers as a Character array.
	 */
	public Character[] getPassengers()
	{
		return this.passengers;
	}
	
	/**
	 * return the pedestrians as a Character array.
	 * @return the pedestrians as a Character array.
	 */
	public Character[] getPedestrians()
	{
		return this.pedestrians;
	}
	
	/**
	 * returns whether the pedestrians are legally crossing at the traffic light
	 * @return a boolean indicated whether 
	 *         the pedestrians are legally crossing at the traffic light
	 */
	public boolean isLegalCrossing() 
	{
		return this.isLegalCrossing;
	}
	
	/**
	 * sets whether the pedestrians are legally crossing the street.
	 * @param isLegalCrossing a boolean indicates 
	 *        whether the pedestrians are legally crossing the street.
	 */
	public void setLegalCrossing(boolean isLegalCrossing) 
	{
		this.isLegalCrossing = isLegalCrossing;
	}
	
	/**
	 * returns the number of passengers in the car.
	 * @return an integer number of passengers in the car.
	 */
	public int getPassengerCount() 
	{
		return passengers.length;
	}
	
	/**
	 * returns the number of pedestrians on the street.
	 * @return an integer number of pedestrians on the street.
	 */
	public int getPedestrianCount() 
	{
		return pedestrians.length;
	}
	
	/**
	 * returns a summary of the scenario, 
	 * used Character.toString() to print passengers and pedestrians information.
	 * @return a summary of the scenario, 
	 *         includes passengers, pedestrians information and if pedestrians are legal crossing.
	 */
	public String toString() 
	{
		// header
		String header = "";
		header += "======================================";
		header += "\n# Scenario\n";
		header += "======================================";
		
		String isLegalCrossing = this.isLegalCrossing() ? "yes" : "no";
		
		// passengers information line by line in a string
		String passengersInfo = "";
		for(Character p: passengers) 
		{
			passengersInfo += String.format("\n- %s", p.toString());
		}
		// pedestrians information line by line in a string
		String pedestriansInfo = "";
		for(Character p: pedestrians) 
		{
			pedestriansInfo += String.format("\n- %s", p.toString());
		}
		
		return header 
			   + "\nLegal Crossing: " + isLegalCrossing
			   + "\nPassengers " + "(" + this.getPassengerCount() + ")"
			   + passengersInfo
			   + "\nPedestrians " + "(" + this.getPedestrianCount() + ")"
			   + pedestriansInfo;
	}
}
