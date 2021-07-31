package projC;

public class NimHumanPlayer extends NimPlayer{

	public NimHumanPlayer(String userName, String familyName, String givenName) 
	{
		super(userName, familyName, givenName);
	}
	
	/* following method prompts players to enter the value to remove
	   and return the input.*/
	public int removeStone(int amount_remain, int upper_bound) 
	{
		System.out.printf("\n%s's turn - remove how many?%n", super.getGivenName());
		int remove_amount = Nimsys.SCANNER.nextInt();
		return remove_amount;
	}
	
	
	/* following method prompts players to enter the value to remove
	   and return the combined input as string.*/
	public String advancedMove(boolean[] available, String lastMove) {
		System.out.printf("\n%s's turn - which to remove?%n", super.getGivenName());
		int position = Nimsys.SCANNER.nextInt();
		int amount = Nimsys.SCANNER.nextInt();
		//combine both inputs
		String move = Integer.toString(position) + " " + Integer.toString(amount);
		return move;
	}
}
