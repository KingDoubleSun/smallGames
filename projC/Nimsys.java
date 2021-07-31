package projC;
/*Written by
 * Name: Shuwei Liu
 * Stduent number: 847046
 * Username: shuweil
 */

import java.util.Scanner;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Nimsys
{
	public static final Scanner SCANNER = new Scanner(System.in);
	private static final String FILENAME = "player.dat";
	//an array list contains all added players as NimPlayer obj
	private static ArrayList<NimPlayer> player_list = new ArrayList<NimPlayer>();
	
	public static void main(String[] args) 
	{   
		//initialize inputStream and player object to be valued by reading file object
		ObjectInputStream inputStream = null;
		NimPlayer player = null;
		//read players from file, add them to player_list
		try 
		{
			inputStream  = new ObjectInputStream(new FileInputStream(FILENAME));
			try 
			{
				while(true) 
				{
					player = (NimPlayer) inputStream.readObject();
					player_list.add(player);
				}
			}
			//handle finish reading by EOFException
			catch(EOFException e) {}
			inputStream.close();
		}
		//catch exceptions do nothing
		catch(FileNotFoundException e) {}
		catch(ClassNotFoundException e) {} 
		catch(IOException e) {}
			
		
		//welcome section
		System.out.println("Welcome to Nim");
		
		//main page prompt for user input
		while(true) 
		{
			System.out.print("\n$");
			String inputs = SCANNER.nextLine();
			//split user input into a array,[0] is main command, then others
			String[] commands = inputs.split(" |,");
			try 
			{
				function(commands);
			}
			//exception for insufficient arguments
			catch(ArrayIndexOutOfBoundsException e) 
			{
				System.out.println("Incorrect number of arguments supplied to command.");
			}
			//exception for invalid command
			catch(IllegalArgumentException e)
			{
				System.out.printf("\'%s\' is not a valid command.%n", commands[0]);
			}
		}
    }
	
	
	//an enum class of groups of user commands
	//for following switch cases
	private enum functions{addplayer, addaiplayer, removeplayer, 
		                   editplayer, resetstats, displayplayer, 
		                   rankings, startgame, exit, startadvancedgame}
	
	/* This method takes whole line of user's input as parameter
	   implements all functions. */
	private static void function(String[] commands)
	{	
		
		//initialize command as enum method
		functions command = functions.valueOf(commands[0]);
		//initialize player as a NimPlayer obj for comparing purposes
		NimPlayer player = null;
		
		//all commands
		switch(command) {
		case addplayer:
			//check if player exists
			player = new NimHumanPlayer(commands[1], commands[2], commands[3]);
			if(player_list.contains(player))
				System.out.println("The player already exists.");
			else
				player_list.add(player);
			break;
			
			
		case addaiplayer:
			//check if player exists
			player = new NimAIPlayer(commands[1], commands[2], commands[3]);
			if(player_list.contains(player))
				System.out.println("The player already exists.");
			else
				player_list.add(player);
			break;
		
			
		case removeplayer:
			//check if it is default command
			if(commands.length == 1) 
			{
				System.out.println("Are you sure you want to remove all players? (y/n)");
			    String response = SCANNER.next();
			    if(response.equals("y")) 
			    	player_list.clear();
			    
			    //Consuming left over new line from above
				//To avoid the nextLine method, which is in the while loop of main method, 
			    //reads enter key as input.
				Nimsys.SCANNER.nextLine();
			}
			//remove certain user
			else 
			{
				//check if the user exist, equals method is overridden, only usernames are compared
				player = new NimHumanPlayer(commands[1], null, null);
				if(! player_list.contains(player)) 
					System.out.println("The player does not exist.");
				else
				{
					player = player_list.get(player_list.indexOf(player));
					player_list.remove(player);		
				}
			}
			
			break;
			
			
		case editplayer:
			//check if the user exist 
			player = new NimHumanPlayer(commands[1], null, null);
			if(! player_list.contains(player))
				System.out.println("The player does not exist.");
			else
			{
				player = player_list.get(player_list.indexOf(player));
				player.resetName(commands[2], commands[3]);
			}

			break;
			
		
		case resetstats:
			//check if it is default command
			if(commands.length == 1) 
			{
				System.out.println("Are you sure you want to " + 
			                       "reset all player statistics? (y/n)");
			    String response = SCANNER.next();
			    if(response.equals("y")) 
			    {
			    	for(NimPlayer p : player_list)
			    		p.resetStats();
			    }
			    
			    //Consuming left over new line from above
				//To avoid the nextLine method, which is in the while loop of main method, 
			    //reads enter key as input.
				Nimsys.SCANNER.nextLine();
			}
			//reset certain user's statistics
			else 
			{
				//check if the user exist
				player = new NimHumanPlayer(commands[1], null, null);
				if(! player_list.contains(player)) 
					System.out.println("The player does not exist.");
				else
				{
					player = player_list.get(player_list.indexOf(player));
					player.resetStats();
				}
			}
			
			break;
		
			
		case displayplayer:
			//sort player list by user name
			player_list.sort((NimPlayer p1, NimPlayer p2)
					          -> p1.getUserName().compareTo(p2.getUserName()));
			
			//check if it is default command
			if(commands.length == 1) 
			{
				for(NimPlayer p : player_list) 
					System.out.println(p.toString());
			}
			
			else 
			{
				//check if the user exist
				player = new NimHumanPlayer(commands[1], null, null);
				if(! player_list.contains(player)) 
					System.out.println("The player does not exist.");
				else 
				{
					player = player_list.get(player_list.indexOf(player));
					System.out.println(player.toString());
				}
			}
			break;
			
		
        case rankings:
        	//sort player list on user_names alphabetically
        	player_list.sort((NimPlayer p1, NimPlayer p2)
        			          -> p1.getUserName().compareTo(p2.getUserName()));
            
        	//check if it is default command or descending command
        	if(commands.length == 1 || commands[1].equals("desc")) 
        	{
        		//sort player list by descending win_rate
        		player_list.sort((NimPlayer p1, NimPlayer p2) 
        				          -> p2.getWinRate().compareTo(p1.getWinRate())); 
        	}
        	//ascending command
        	else if(commands[1].equals("asc"))
        	{
        		//sort player list by ascending win_rate
        		player_list.sort((NimPlayer p1, NimPlayer p2)
        				          -> p1.getWinRate().compareTo(p2.getWinRate())); 
        	}
        	
        	//print first 10 results of sorted player list
        	for(int i = 0; i < 10 && i < player_list.size(); i++)
        		System.out.printf("%-5s| %02d games | %s%n", 
        				          String.format("%.0f%%", player_list.get(i).getWinRate()), 
        				          player_list.get(i).getGamePlayed(), 
        				          player_list.get(i).getFullName());
 
			break;
			
			
		case startgame:
			//initialize players with given user name and check if both players exist
			NimPlayer player1 = new NimHumanPlayer(commands[3], null, null);
			NimPlayer player2 = new NimHumanPlayer(commands[4], null, null);
			if(player_list.contains(player1) 
			   && player_list.contains(player2)) 
			{
				//redefine players by searching the list
				player1 = player_list.get(player_list.indexOf(player1));
				player2 = player_list.get(player_list.indexOf(player2));
				
				//initialize the game
				NimGame game = new NimGame(Integer.parseInt(commands[1]),   //input is string
			                               Integer.parseInt(commands[2]), 
			                               player1, player2);
				game.gameSystem();
			}
			else
				System.out.println("One of the players does not exist.");
			
			break;
		
			
		case startadvancedgame:
			//initialize players and check if both players exist
			player1 = new NimHumanPlayer(commands[2], null, null);
			player2 = new NimHumanPlayer(commands[3], null, null);
			if(player_list.contains(player1) 
			   && player_list.contains(player2)) 
			{
				//redefine players by searching the list
				player1 = player_list.get(player_list.indexOf(player1));
				player2 = player_list.get(player_list.indexOf(player2));
				
				//initialize the game
				NimAdvanceGame game = new NimAdvanceGame(Integer.parseInt(commands[1]),   
			                                             player1, player2);
				game.gameSystem();
			}
			else
				System.out.println("One of the players does not exist.");
			
			break;
			
			
		case exit:
			//rewrite all players record before exit
			ObjectOutputStream outputStream = null;
			try
			{
				outputStream = new ObjectOutputStream(new FileOutputStream(FILENAME));
				for(NimPlayer p: player_list) 
				{
					outputStream.writeObject(p);
				}
				outputStream.close();
			}
			//catch exceptions do nothing
			catch (FileNotFoundException e){}
			catch(IOException e){}
			
			//print a blank line before exit
			System.out.println();
			System.exit(0);
			break;
		}
	}
}
	