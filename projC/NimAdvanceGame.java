package projC;
/*Written by
 * Name: Shuwei Liu
 * Stduent number: 847046
 * Username: shuweil
 */
import java.util.Arrays;
import java.util.InputMismatchException;

public class NimAdvanceGame extends NimGame{
	
	//inheritance, upper_bound is default 2 here
	public NimAdvanceGame(int amount_remain_input,
			              NimPlayer user1, NimPlayer user2) 
	{
		super(amount_remain_input, 2, user1, user2);
	}
	
	public void gameSystem() 
	{
		//initialize simulated stones by a boolean array
		//eg:<X**> equivalent to [false true true]
		boolean[] available = new boolean[amount_remain];
		Arrays.fill(available, true); //initially all true
		
		//print game information
		System.out.println("\nInitial stone count: " + amount_remain);
		System.out.print("Stones display:");
		displayStones(available);
		System.out.println("\nPlayer 1: " + player1.getFullName());
		System.out.println("Player 2: " + player2.getFullName());
		
		//side counter to decide whose turn
		int side_counter = 0; 
		//the move before first move is null
		String move = null;
		int position;    //which position to remove
		int amount;      //amount to remove
	    //Game process
		while (amount_remain > 0) 
		{
			System.out.printf("\n%d stones left:", amount_remain);
			displayStones(available);
			
			if((side_counter % 2) == 0) 
				move = player1.advancedMove(available, move);
			else
				move = player2.advancedMove(available, move);
			
			//split positin and amount into an array
			String[] moves = move.split(" ");
			//detect all invalid assigned moves
			try 
			{
				position = Integer.parseInt(moves[0]);
				amount = Integer.parseInt(moves[1]);
				//if position and adjacent next one is out of bound
				//or amount is not 1 or 2
				if(position + amount - 1 > available.length
						   || amount > 2 || amount < 1) 
					throw new InputMismatchException();
			}
			catch(InputMismatchException e) 
			{
				//skip the rest, restart this round
				System.out.println("\nInvalid move.");
				continue;
			}
			
			//if move is valid, make the position or (position and next postion) false 
			available[position - 1] = false;
			available[position - 1 + amount - 1] = false;
			amount_remain -= amount;
			side_counter ++; //switch side
		}
		
		//Game over section
		System.out.println("\nGame Over");
		//when counter is odd, last turn is player 1's turn and even for player 2's turn
		//update stats and print who wins
		if ((side_counter % 2) == 0)     
		{
			player2.winGame();
			player1.loseGame();
			System.out.printf("%s wins!%n", player2.getFullName());
		}
		else
		{
			player1.winGame();
			player2.loseGame();
			System.out.printf("%s wins!%n", player1.getFullName());
		}
		
		//Consuming left over new line from above
		//To avoid the nextLine method, which is in the while loop of main method, 
		//reads enter key as input.
		Nimsys.SCANNER.nextLine();
	}
	
	private static void displayStones(boolean[] available) 
	{
		for(int i = 1; i <= available.length; i++) 
		{
			String stone = "*";
			//change to "x" if it has been removed
			if(! available[i - 1])
				stone = "x";
			
			System.out.printf(" <%d,%s>", i, stone);
		}
	}
}
