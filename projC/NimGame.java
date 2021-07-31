package projC;
/*Written by
 * Name: Shuwei Liu
 * Stduent number: 847046
 * Username: shuweil
 */

import java.util.InputMismatchException;

public class NimGame {
	//conditions needed for a game
	protected NimPlayer player1;
	protected NimPlayer player2;
	protected int amount_remain;
	protected int upper_bound;
	
	public NimGame(int amount_remain_input, int upper_bound_input, 
                   NimPlayer user1, NimPlayer user2) 
	{
		player1 = user1;
		player2 = user2;
		amount_remain = amount_remain_input;
		upper_bound = upper_bound_input;
	}
	
	//game_function
	public void gameSystem() 
	{
		//print all game information
		System.out.println("\nInitial stone count: " + amount_remain);
		System.out.println("Maximum stone removal: " + upper_bound);
		System.out.println("Player 1: " + player1.getFullName());
		System.out.println("Player 2: " + player2.getFullName());
		
		/* The side_counter +1 after every move of either player
		   when counter is odd, last turn is player 1's turn and even for player 1's turn */
		int side_counter = 0;   
		
		//2 players move stones alternatively until amount_remain is 0
		while (amount_remain > 0)
		{
			//prompt how many stones left
			System.out.printf("\n%d stones left:", amount_remain);
			//print stones as * * * *
			for (int i = 0; i < amount_remain; i++) 
			{
				System.out.print(" *");
			}
			
			//check whose turn and record the input
			int remove_amount;
			if((side_counter % 2) == 0) 
			{
				remove_amount = player1.removeStone(amount_remain, upper_bound);
			}
			else
			{
				remove_amount = player2.removeStone(amount_remain, upper_bound);
			}
			
			//check if the input is valid
			try 
			{
				if(! (remove_amount >= 1
				      && remove_amount <= upper_bound
				      && remove_amount <= amount_remain)) 
					throw new InputMismatchException();
				else
				{
					amount_remain -= remove_amount;
					side_counter++;
				}	
			}
			catch(InputMismatchException e)
			{
				System.out.printf("\nInvalid move. " 
			                      + "You must remove between 1 and %d stones.%n", 
			                         Math.min(amount_remain, upper_bound));
			}
		}
		
		//Game over section
		System.out.println("\nGame Over");
		//when counter is odd, last turn is player 1's turn and even for player 2's turn
		//update stats and print who wins
		if ((side_counter % 2) == 0)     
		{
			player1.winGame();
			player2.loseGame();
			System.out.printf("%s wins!%n", player1.getFullName());
		}
		else
		{
			player2.winGame();
			player1.loseGame();
			System.out.printf("%s wins!%n", player2.getFullName());
		}
		
		//Consuming left over new line from above
		//To avoid the nextLine method, which is in the while loop of main method, 
	    //reads enter key as input.
		Nimsys.SCANNER.nextLine();
     }
}

