package projC;
/*Written by
 * Name: Shuwei Liu
 * Stduent number: 847046
 * Username: shuweil
 */
import java.util.Arrays;

public class NimAIPlayer extends NimPlayer
                         implements Testable{
    //defalut constructor, information is all null or 0
	public NimAIPlayer() 
	{
		super();
	}
	
	public NimAIPlayer(String userName, String familyName, String givenName) 
	{
		super(userName, familyName, givenName);
	}
	
	/* AI's auto move, will guarantee to win if the conditions are satisfied.
	 * Otherwise, only remove 1 stone.*/
	public int removeStone(int amount_remain, int upper_bound) 
	{
		System.out.printf("\n%s's turn - remove how many?%n", super.getGivenName());
		int remove_amount;
		//no guarantee win move
		if(amount_remain % (upper_bound + 1) == 1) 
		{
			remove_amount = 1;
		}
		//guarantee win move exists
		else 
		{
			if(amount_remain % (upper_bound + 1) == 0) 
				remove_amount = upper_bound;
			else
				remove_amount = amount_remain % (upper_bound + 1) - 1;
		}
		
		return remove_amount;
	}
	
	/* AI's auto move for advancedgame, will guarantee to win if the conditions are satisfied.
	 * Otherwise, only remove 1 stone.*/
	public String advancedMove(boolean[] available, String lastMove) {
		//ask for move, no need for test?
		//System.out.printf("\n%s's turn - which to remove?%n", super.getGivenName());
		
		int remove_amount = 1; //must remove at least 1
		int position = -1;     //if after all test, is still -1, means no startegy to win
		
		//First to move
		//Take the middle, make the rest sysmetric
		if(lastMove == null) 
		{
			position = (available.length + 1) / 2;
			if(available.length % 2 == 0)
				remove_amount = 2;
			else
				remove_amount = 1; 
		}
		//Not first move
		//attempt every possible position, and check if it is a guarantee win strategy
		else
		{
			int j = 0; //index of available
			while(j < available.length)
			{
				//deepcopy available for attempting
				boolean[] attempt = available.clone();
				//skip the position where the stone has been removed
				//maybe can use try catch?
				while(! available[j]) 
				{
					j++;
					if(j == available.length)
						break;
				}
				//if all possibilties are attempted, end loop
				if(j == available.length)
					break;

				//for each position, attempt both remove 1 and 2 stones 
				for(int amount = 1; amount <=2 ; amount++) 
				{
					//ATTEAMPT SECTION
					//attempt remove 1
					if(amount == 1) 
						attempt[j] = false;
					//attempt remove 2 if possible
					else if(amount == 2 && j < available.length - 1 && available[j+1])
					{
						attempt[j] = false;
						attempt[j + 1] = false;
					}
					//end if next adjacent stone has been removed
					else
						break;
					
					//COLLECT INFORMATION AFTER ATTEMPT
					//check contains amount of isolated connected stones
					//check[0] == amount of isolated 1 stone, and so on
					//assume a game has max 11 stones
					int[] check = new int[10];
					Arrays.fill(check, 0); //initially all 0
					int i = 0;
					while(i < available.length) 
					{
						int count_connected = 0;
						while(attempt[i]) 
						{
							i++;
							count_connected++;
							if(i == attempt.length)
								break;
						}
						if(count_connected > 0)
							check[count_connected - 1] += 1;
						i++;
					}
					
					//LOOK FOR STRATEGY
					//test 1, if all isolated connected stones are in pairs
					//eg:[**X*X**X*X] and check = [2, 2, 0, ...]
					boolean guarantee_win1 = true;
					for(int element: check) 
					{
						if(element % 2 != 0) 
						{
							guarantee_win1 = false;
							break;
						}
					}
					
					//test 2, if isolated 1 stones is odd, and there is 1 connected 4 or 8 stones
					//eg:[*X****XX] and check = [1, 0, 0, 1, 0, 0....]
					boolean guarantee_win2 = true;
					if((check[0] % 2 != 0 && check[3] == 1 && check[7] == 0)
					   || (check[0] % 2 != 0 && check[3] == 0 && check[7] == 1))
					{
						//check if rest are all 0
						for(int k = 0; k < 10; k++) 
						{
							if(k != 0 && k != 3 && k!= 7 && check[k] != 0) 
							{
								guarantee_win2 = false;
								break;
							}
						}
					}
					else
						guarantee_win2 = false;
				
				
					//test3, if there is 1 1 stone, 1 2 connected stones, 1 3 connected stones, 
					//rest are 0 
					//eg [*XX***X**X]
					boolean guarantee_win3 = true;
					if(check[0] == 1 && check[1] == 1 && check[2] == 1) 
					{
						//check if rest are all 0
						for(int k = 0; k < 10; k++) 
						{
							if(k != 0 && k != 1 && k!= 2 && check[k] != 0) 
							{
								guarantee_win3 = false;
								break;
							}
						}
					}
					else
						guarantee_win3 = false;
					
					
					//if one of above test passed, assign postion and remove_amount and end attempting
					if(guarantee_win1 || guarantee_win2 || guarantee_win3) 
					{
						position = j + 1;    //real position is index + 1
						remove_amount = amount;
						break;
					}
				}
				//stop attempting if strategy is found
				if(position != -1) 
					break;
				//keep looking next one
				j++;
			}
		}
		
		//if no strategy found, move the left most stone
		if(position == -1) 
		{
			for(int i = 0; i < available.length; i++) 
			{
				if(available[i]) 
				{
					position = i + 1;
					remove_amount = 1;
					break;
				}
			}
		}
		//concat both postion and amount to a string
		String move = Integer.toString(position) + " " + Integer.toString(remove_amount);
		return move;
	}
}
