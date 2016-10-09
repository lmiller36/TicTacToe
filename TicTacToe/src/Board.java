import java.util.ArrayList;
import java.util.List;

/**
 * This contains information about the board of tic-tac-toe
 * This includes the dimensions of the board, where pieces are, how many pieces
 * in a row to win, and who has won that game based on the board state.
 * A board that has a winner does not accept new pieces.
 * @author mwwie
 *
 */
public class Board
{
	/**
	 * Number of columns in this board
	 */
	private int cols;
	/**
	 * Number of rows in this board
	 */
	private int rows;
	/**
	 * The number of pieces in a row required to win on this board
	 */
	private int winLength;
	/**
	 * The current state of the pieces on this board
	 * 0 would be an empty spot
	 * 1 is a spot taken by player one (X)
	 * 2 is a spot taken by player two (O)
	 * pieces[0] is the top row of pieces
	 * pieces[0][0] is the piece at the top-left of the board
	 */
	private int[][] pieces;
	/**
	 * The player index of the player that won the game.
	 * -1 if the game is not over
	 * 0 if the game was a tie
	 */
	private int winner;
	/**
	 * The empty spaces left on this board
	 */
	private List<Position> emptyPositions;
	
	public int getCols() { return cols; }
	public int getRows() { return rows; }
	/**
	 * Returns winLength
	 * @return winLength
	 */
	public int getWinLength() { return winLength; }
	public int[][] getPieces() { return pieces; }
	public int getWinner() { return winner; }
	public List<Position> getEmptyPositions()
	{ return new ArrayList<Position>(emptyPositions); }
	
	/**
	 * Creates a new tic-tac-toe board
	 */
	public Board()
	{
		this(3, 3, 3);
	}
	
	/**
	 * Creates a new board with c columns, r rows, and k to win
	 * @param c the number of columns in this board
	 * @param r the number of rows in this board
	 * @param k the number of pieces in a row to win
	 */
	public Board(int c, int r, int k)
	{
		cols = c;
		rows = r;
		winLength = k;
		pieces = new int[rows][cols]; // all positions empty
		winner = -1; // game still going
		setEmptyPositions();
	}
	
	/**
	 * Resets the board to be empty with no winner
	 */
	public void reset()
	{
		pieces = new int[rows][cols];
		winner = -1;
		setEmptyPositions();
	}
	
	/**
	 * Assumes the board is empty and creates the list of all empty positions
	 */
	private void setEmptyPositions()
	{
		emptyPositions = new ArrayList<Position>();
		for(int r = 0; r < rows; r++)
		{
			for(int c = 0; c < cols; c++)
			{
				emptyPositions.add(new Position(r, c));
			}
		}
	}
	
	/**
	 * Returns the player that controls this position
	 * @param p
	 * @return the index of the player that occupies this position
	 *  0 if empty
	 *  -1 if invalid
	 */
	public int get(Position p)
	{ 
		if(isValid(p))
		{
			return pieces[p.row()][p.col()];
		}
		return -1;
	}
	
	/**
	 * Returns whether the game is complete
	 * @return true iff winner != -1
	 */
	public boolean gameOver()
	{
		return winner != -1;
	}
	
	/**
	 * Adds a new piece at position pos if a new piece can be added at that 
	 * position. Updates the winner and number of empty positions
	 * @param player the player who owns the piece
	 * @param pos the position to place the piece in
	 * @return whether the new piece has been added: pos is valid, empty,
	 * and the game was not over
	 */
	public boolean addNewPiece(int player, Position pos)
	{
		if(!isValid(pos)) { return false; }
		if(get(pos) == 0 && winner == -1)
		{
			setPosition(pos, player);
			emptyPositions.remove(pos);
			updateWinner(linesContaining(pos));
			return true;
		}
		return false;
	}

	/**
	 * Returns whether line is valid
	 * @param line the line to test
	 * @return true iff all positions in line are valid
	 */
	public boolean isValid(Line line)
	{
		for(Position pos : line.getPositions())
		{
			if(!isValid(pos)) { return false; }
		}
		return true;
	}
	
	/**
	 * @param p the position to test
	 * @return whether p is a valid position on this board
	 */
	public boolean isValid(Position p)
	{
		return p != null
				&& p.row() >= 0 // not too high
				&& p.row() < rows // not too low
				&& p.col() >= 0 // not too far left
				&& p.col() < cols; // not too far right
	}
	
	/**
	 * Adds the player piece to the given pos
	 * <p>Assumes player and pos are valid
	 * @param pos the position to set
	 * @param player the player to add
	 */
	private void setPosition(Position pos, int player)
	{
		pieces[pos.row()][pos.col()] = player;
	}
	
	/**
	 * Updates the winner index if someone has won on the given lines
	 * @param lines the lines that may have a winner
	 * @return the first winner found on this line
	 */
	private void updateWinner(List<Line> lines)
	{
		for(Line line : lines)
		{
			int player = winnerOn(line);
			if(player > 0)
			{
				winner = player;
				return;
			}
		}
		if(emptyPositions.isEmpty()) { winner = 0; } // no more spaces, no winner
	}
	
	/**
	 * Returns a list of all valid lines that contain position pos
	 * @param pos the position to test for
	 * @return a list of all valid lines that contain position pos
	 * <p>null if pos is invalid
	 */
	private List<Line> linesContaining(Position pos)
	{
		if(!isValid(pos)) { return null; }
		
		List<Line> lines = new ArrayList<Line>();
		
		for(int dir = Position.UP; dir < Position.HALF_CIRCLE; 
				dir += Position.TURN_HALF_RIGHT)
		{
			Line line = new Line(pos, dir, winLength);
			for(int i = 0; i < winLength; i++)
			{
				if(isValid(line)) { lines.add(line); }
				
				// move line to a new position
				line = new Line(line.getStart().getAdjacentPosition
						(dir + Position.HALF_CIRCLE), dir, winLength);
			}
		}
		
		return lines;
	}
	
	/**
	 * Returns the player that won on this line
	 * @param line the line to check for a winner
	 * @return the player that won on this line
	 * <p>0 if no player won on this line
	 * <p>-1 if this line is not a win line
	 */
	private int winnerOn(Line line)
	{
		if(!isWinLine(line)) { return -1; }
		int player = get(line.getPositions()[0]);
		for(Position pos : line.getPositions())
		{
			int curr = get(pos);
			if(curr == 0 || curr != player) { return 0; }
		}
		return player;
	}
	
	/**
	 * Returns whether this line can be a win line
	 * @param line the line to test
	 * @return true iff line is of winLength and line is valid
	 */
	public boolean isWinLine(Line line)
	{
		return line.getLength() == winLength && isValid(line);
	}
}
