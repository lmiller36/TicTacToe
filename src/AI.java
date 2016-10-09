import java.util.ArrayList;
import java.util.List;

/**
 * This class determines the behavior of the artificial player
 * Block: 
 * Check for opponent having two in a row
 * Block the third position
 * Win: Check for self having two in a row
 * Occupy third position
 * Create Fork: Create a place with two abilities for self to win 
 * (2 sets of 2-in-a-row)
 * Block a fork
 * Create 2 in a row to force opponent to defend
 * Simply occupy the only fork opportunity of the opponent
 * Occupy center (unless self is first player; then occupy corner)
 * Check opponent occupies corner
 * Occupy opposite corner
 * Else
 * Play in any empty corner
 * Play in any empty side

 * @author mwwie
 *
 */
public class AI
{
	/**
	 * The board that this AI plays on
	 */
	private Board board;
	/**
	 * What number this goes: 1 for X's, 2 for O's
	 */
	private int playerIndex;
	/**
	 * The name of this AI
	 */
	private String name;
	
	public Board getBoard() { return board; }
	public int getPlayerIndex() { return playerIndex; }
	public String getName() { return name; }
	
	/**
	 * @return the index of the other player in the game
	 */
	private int otherPlayer() { return 3 - playerIndex; }
	
	public AI()
	{
		this(new Board(), 1, "AI");
	}
	
	public AI(Board b, int p, String n)
	{
		board = b;
		playerIndex = p;
		name = n;
	}
	
	/**
	 * TODO
	 * @return the best move based on a simple tic-tac-toe strategy
	 */
	public Position bestMove()
	{
		if(board.gameOver()) { return null; }

		// good moves to go in
		List<Position> goodMoves = null;
		
		// go in position to win for this
		goodMoves = winPositionsFor(playerIndex);
		if(!goodMoves.isEmpty())
		{ return goodMoves.get(0); }
		
		// block player about to win
		goodMoves = winPositionsFor(otherPlayer());
		if(!goodMoves.isEmpty()) // other player is about to win
		{ return goodMoves.get(0); } // block the first win opportunity
		
		// go in random empty position
		return board.getEmptyPositions().get(
				(int)(Math.random() * board.getEmptyPositions().size()));
	}
	
	/**
	 * Returns the list of positions that, if player were to go in them, would
	 * result in player winning
	 * @param player the player to test win positions for
	 * @return list of positions that would result in player winning if player
	 * were to go in them
	 */
	private List<Position> winPositionsFor(int player)
	{
		List<Line> winLines = winLines(board);
		List<Position> blockPositions = new ArrayList<Position>();
		
		for(Line line : winLines)
		{
			Position winPos = winPos(player, line);
			if(winPos != null && !blockPositions.contains(winPos))
			{
				blockPositions.add(winPos);
			}
		}
		
		return blockPositions;
	}
	
	/**
	 * Calculates the position player would go in to win on this line
	 * @param player the player that might be about to win
	 * @param line the line the player might be about to win on
	 * @return the position the player must play to win; null if the player
	 * cannot win on this turn
	 */
	private Position winPos(int player, Line line)
	{ 
		// invalid player index or line structure
		if(player < 1 || !board.isWinLine(line)) { return null; }
		
		// The number of positions on this line controlled by player
		int playerControl = 0;
		int empty = 0; // empty positions on this line
		Position blockPos = null; // the position to win
		
		for(Position p : line.getPositions())
		{
			int controller = board.get(p);
			if(controller == player) { playerControl++; }
			if(controller == 0) // the space is empty
			{
				// two empty spots
				if(++empty > 1) { return null; } // the player can't win next
				
				blockPos = p; // else going in the empty space would win
			}
		}
		
		// player is about to win
		if(playerControl == board.getWinLength() - 1 && empty == 1)
		{return blockPos; } // return position to win
		
		// player is not about to win
		return null; // no position would win
	}
	
	/**
	 * Returns all sequences that would result in a player winning
	 * @param board the board to test
	 * @return a list of all unique lines of length board.winLength that are
	 * valid on this board
	 */
	public static List<Line> winLines(Board board)
	{
		List<Line> winLines = new ArrayList<Line>();
		
		int k = board.getWinLength();
		int rows = board.getRows();
		int cols = board.getCols();
		
		// iff above this, add down lines
		int split = rows - k + 1;
		
		// iff left of this, add right and down-right lines
		int leftSection = cols - k + 1;
		
		// iff right of this and above split, add down-left lines
		int rightSection = k - 1;
		
		for(int r = 0; r < rows; r++)
		{
			for(int c = 0; c < cols; c++)
			{
				Position pos = new Position(r, c);
				
				if(r < split) // should add any downward lines
				{
					winLines.add(new Line(pos, Position.DOWN, k));
					
					if(c < leftSection) // should add down-right lines
					{ winLines.add(new Line(pos, Position.DOWN_RIGHT, k)); }
					
					if(c >= rightSection) // should add left-down lines
					{ winLines.add(new Line(pos, Position.DOWN_LEFT, k)); }
				} // end downward lines
				
				if(c < leftSection) // add right lines
				{ winLines.add(new Line(pos, Position.RIGHT, k)); }
			}
		}
		
		return winLines;
	}
}
