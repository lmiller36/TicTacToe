/**
 * This class defines a position on the board. It simply has an x- and y-coordinate
 * The position (0, 0) represents the top left of the board
 * The position (2, 3) would represent the bottom middle of a 3 x 3 board
 * @author mwwie
 *
 */
public class Position
{
	public static final int UP = 0;
	public static final int UP_RIGHT = 1;
	public static final int RIGHT = 2;
	public static final int DOWN_RIGHT = 3;
	public static final int DOWN = 4;
	public static final int DOWN_LEFT = 5;
	public static final int LEFT = 6;
	public static final int UP_LEFT = 7;
	
	public static final int TURN_RIGHT = 2;
	public static final int TURN_LEFT = -2;
	
	public static final int TURN_HALF_RIGHT = 1;
	public static final int TURN_HALF_LEFT = -1;
	
	public static final int HALF_CIRCLE = 4;
	public static final int FULL_CIRCLE = 8;
	
	public static int adjustDirection(int d)
	{
		while (d < 0) { d += FULL_CIRCLE; }
		while (d >= FULL_CIRCLE) { d -= FULL_CIRCLE; }
		return d;
	}
	
	/**
	 * The column of this position
	 */
	private int col;
	/**
	 * The row of this position
	 */
	private int row;
	
	/**
	 * Creates a new position at row "r" and col "c"
	 * @param r the row of this position
	 * @param c the column of this position
	 */
	public Position(int r, int c)
	{
		row = r;
		col = c;
	}
	
	/**
	 * @return the row of this position
	 */
	public int row() { return row; }
	public int col() { return col; }
	
	public Position getAdjacentPosition(int direction)
	{
		adjustDirection(direction);
		
		int r = row;
		int c = col;
		
		if(direction == UP)
		{
			r--;
		}
		else if(direction == UP_RIGHT)
		{
			r--;
			c++;
		}
		else if(direction == RIGHT)
		{
			c++;
		}
		else if(direction == DOWN_RIGHT)
		{
			r++;
			c++;
		}
		else if(direction == DOWN)
		{
			r++;
		}
		else if(direction == DOWN_LEFT)
		{
			r++;
			c--;
		}
		else if(direction == LEFT)
		{
			c--;
		}
		else if(direction == UP_LEFT)
		{
			r--;
			c--;
		}
		
		return new Position(r, c);
	}
	
	public boolean equals(Object o)
	{
		if(!(o instanceof Position)) {return false;}
		Position p = (Position)o;
		
		return p.row() == row && p.col() == col;
	}

	/**
	 * Returns a string representation of this position
	 * <p>([row], [col])
	 * <p>Example: (4, 2)
	 */
	public String toString()
	{
		String s = "(" + row + ", " + col + ")";
		return s;
	}

	/**
	 * Return the string representation of this direction
	 * @param direction the direction
	 * @return the word form of this direction, example "UP" or "UP_RIGHT"
	 */
	public static String toString(int direction)
	{
		direction = adjustDirection(direction);
		if(direction == Position.UP) { return "UP"; }
		if(direction == Position.UP_RIGHT) { return "UP_RIGHT"; }
		if(direction == Position.RIGHT) { return "RIGHT"; }
		if(direction == Position.DOWN_RIGHT) { return "DOWN_RIGHT"; }
		if(direction == Position.DOWN) { return "DOWN"; }
		if(direction == Position.DOWN_LEFT) { return "DOWN_LEFT"; }
		if(direction == Position.LEFT) { return "LEFT"; }
		if(direction == Position.UP_LEFT) { return "UP_LEFT"; }
		return ""; // avoid compiler error
	}
}