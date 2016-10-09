//  import java.util.Scanner;

/**
 * Supports information for a game where a human plays as X and an AI plays as
 * O. Player can restart the game once it's over
 * @author mwwie
 *
 */
public class ConsoleGame
{
	public static final String WELCOME_MESSAGE = "Welcome to Em-En-Kay!";
	public static final String INPUT_MESSAGE = "Select your position by "
			+ "entering [row] [col] in simple format.\n"
			+ "0 0 is the top-left of the board";
	public static final String GOODBYE_MESSAGE = "Thanks for playing! "
			+ "Goodbye!";
	public static final String PLAYER_WIN_MESSAGE = "KANYE IS THE WINNER!";
	public static final String PLAYER_LOSE_MESSAGE = "KIM IS VICTORIOUS!";
	public static final String PLAYER_TIE_MESSAGE = "Aw shucks! Nobody wins! "
			+ "That's no fun.";
	public static final String AI_MOVE_MESSAGE = "%s's move:";
	public static final String WIN_CONDITION_MESSAGE = "%d in a row to win!";

	public static final String INPUT_PROMPT = "Your move (or \"q\" to quit):";
	public static final String REPLAY_PROMPT = "Enter q to quit. Enter any "
			+ "other key to continue.";

	public static final String INPUT_EXCEPTION_MESSAGE = "Input not recognized. "
			+ "Enter a position's row and column in simple \"[row] [col]\" "
			+ "format or \"q\" to quit";
	public static final String INVALID_POSITION_MESSAGE = "That position is "
			+ "not valid";

	/**
	 * Play as many games of tic-tac-toe as the player likes!
	 * @param args not necessary
	 */
	public static void main(String[] args)
	{
		Board board = new Board();
		AI kanye = new AI(board, 1, "Kanye");
		AI kimKardashian = new AI(board, 2, "Kim");
		// @SuppressWarnings("resource")
		//Scanner reader = new Scanner(System.in);

		System.out.println(WELCOME_MESSAGE);
		System.out.println(INPUT_MESSAGE);
		System.out.println(String.format(WIN_CONDITION_MESSAGE, board.getWinLength()));
		displayBoard(board);

		while(!board.gameOver())
		{
			//Position nextPos = null;
			boolean playerHasFinished = false;

			while(!playerHasFinished)
			{
				/*
				try
				{
					System.out.println(INPUT_PROMPT);
					String input = reader.nextLine();
					if(input.equals("q")) { quitGame(); }
					String[] tokens = input.split("\\s+");
					nextPos = new Position(Integer.parseInt(tokens[0]),
							Integer.parseInt(tokens[1]));

					playerHasFinished = board.addNewPiece(1, nextPos);
					if(!playerHasFinished)
					{ System.out.println(INVALID_POSITION_MESSAGE); }
				}
				catch (IndexOutOfBoundsException e)
				{ System.out.println(INVALID_POSITION_MESSAGE); }
				catch (NullPointerException e)
				{ System.out.println(INPUT_EXCEPTION_MESSAGE); }
				catch (NumberFormatException e)
				{ System.out.println(INPUT_EXCEPTION_MESSAGE); }
				*/
				playerHasFinished = playAI(board, kanye);
			} // end player turn
			
			displayBoard(board);
			if (playAI(board, kimKardashian))
			{ displayBoard(board); }
			if(board.gameOver())
			{ 
				displayGameOverMessage(board.getWinner());
				board.reset();
			}
		} // end while game not over
		
		endGame(board.getWinner());
	}

	/**
	 * If possible, play the AI's best move at this point.
	 * Will do nothing if game is over
	 * @param b the board to play on
	 * @param ai is the AI playing the game
	 * @return whether the AI played
	 */
	private static boolean playAI(Board b, AI ai)
	{
		if (b.addNewPiece(ai.getPlayerIndex(), ai.bestMove()))
		{
			System.out.println(String.format(AI_MOVE_MESSAGE, ai.getName()));
			return true;
		}
		return false;
	}

	/**
	 * End the game. Display the game over message and quit.
	 * @param winner the player index that won this game
	 */
	private static void endGame(int winner)
	{
		displayGameOverMessage(winner);
		quitGame();
	}

	private static void displayGameOverMessage(int winner)
	{
		if(winner == 1)
		{
			System.out.println(PLAYER_WIN_MESSAGE);
		}
		else if(winner == 2)
		{
			System.out.println(PLAYER_LOSE_MESSAGE);
		}
		else
		{
			System.out.println(PLAYER_TIE_MESSAGE);
		}
	}

	private static void quitGame()
	{
		System.out.println(GOODBYE_MESSAGE);
		System.exit(0);
	}

	/**
	 * Print the board in console format
	 * Example:
	 * 
	 * X|O| 
	 * —+—+—
	 *  |X| 
	 * —+—+—
	 * O|X|O
	 * 
	 * @param b the board to display
	 */
	private static void displayBoard(Board b)
	{
		String s = "";
		for(int r = 0; r < b.getRows(); r++)
		{
			for(int c = 0; c < b.getCols(); c++)
			{
				s += displayFor(b.get(new Position(r, c))); // display position
				if(c < b.getCols() - 1) { s += "|"; } // add divider
			}

			s += "\n";

			// add divider after every row except the last
			if(r < b.getRows() - 1)
			{
				for(int i = 0; i < b.getCols() - 1; i++)
				{
					s += "—+";
				}
				s += "—\n";
			} // end divider
		}
		System.out.println(s);
	}

	/**
	 * the char to display for the given player
	 * @param player the player's index
	 * @return X for 1, O for 2, ' ' for anything else
	 */
	private static char displayFor(int player)
	{
		if(player == 0 ) {return ' '; }
		if(player == 1) { return 'X'; }
		if(player == 2) { return 'O'; }
		return '?';
	}
}
