package projC;
/*Written by
 * Name: Shuwei Liu
 * Stduent number: 847046
 * Username: shuweil
 */
import java.io.Serializable;


//a class of player(user), contains all attributes of users
public abstract class NimPlayer implements Serializable
{
	//attributes
	private String user_name;
	private String family_name;
	private String given_name;
	private int game_win;
	private int game_played;
	private double win_rate;
	
	//default constructor
	public NimPlayer() 
	{
		user_name = null;
		family_name = null;
		given_name = null;
		game_win = 0;
		game_played = 0;
		win_rate = 0.0;
	}
	
	//a constructor to initialize NimPlayer
	public NimPlayer(String userName, String familyName, String givenName) 
	{
		user_name = userName;
		family_name = familyName;
		given_name = givenName;
		game_win = 0;
		game_played = 0;
		win_rate = 0.0;
	}
	
	//return all information, except win rate
	public String toString() 
	{
		return user_name + "," + given_name + "," + family_name 
			   + "," + game_played + " games," + game_win + " wins";
	}
	
	//override equals method, return true if their user_names are same
	public boolean equals(Object other_user) 
	{
		return (other_user instanceof NimPlayer)
		        ? user_name.equals(((NimPlayer) other_user).getUserName())
		        : super.equals(other_user);
	}
	
	//remove stone for normal game
	public abstract int removeStone(int amount_remain, int upper_bound);
	
	//remove stone for advance game
	public abstract String advancedMove(boolean[] available, String lastMove);
	
	//return user name
	public String getUserName() 
	{
		return user_name;
	}
	
	//return given name
	public String getGivenName() 
	{
		return given_name;
	}
	
	//return full name, given name + family name
	public String getFullName() 
	{
		return given_name + ' ' + family_name;
	}
	
	//return how many games played by the user
	public int getGamePlayed() 
	{
		return game_played;
	}
	
	//return type Double of win rate, 
	//Since Double has methods can be used later
	public Double getWinRate() 
	{
		Double win_rate_obj = win_rate;
		return win_rate_obj;
	}
	
	//reset family name and given name, new names can be same
	public void resetName(String new_family_name, String new_given_name) 
	{
		this.family_name = new_family_name;
		this.given_name = new_given_name;
	}
	
	//reset all stats of the user
	public void resetStats() 
	{
		this.game_played = 0;
		this.game_win = 0;
		this.win_rate = 0.0;
	}
	
	//when the user wins a game, update stats
	public void winGame() 
	{
		this.game_played += 1;
		this.game_win += 1;
		countWinRate();
	}
	
	//when the user loses a game, update stats
	public void loseGame() 
	{
		this.game_played += 1;
		countWinRate();
	}
	
	//calculate and update the win rate of the user, in percentage form
	private void countWinRate() 
	{
		this.win_rate = ((double) this.game_win / this.game_played) * 100;
	}
}