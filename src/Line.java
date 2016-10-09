/**
 * A line is a set of Positions that correspond to each other
 * This class is used to find win conditions of the board
 * @author mwwie
 *
 */
public class Line
{
	private Position start;
	private int direction;
	private int length;
	private Position[] positions;

	public Position getStart() { return start; }
	public int getLength() { return length; }
	public Position[] getPositions() { return positions; }
	
	/**
	 * Adjusts the start position to pos and resets the positions of this line
	 * @param pos the new start position
	 */
	public void setStart(Position pos)
	{
		start = pos;
		resetPositions();
	}
	
	/**
	 * Creates a new line starting at Position pos, going in direction d, and
	 * of length l
	 * @param pos the starting position of this line
	 * @param d the direction of this line
	 * @param l the length of this line
	 */
	public Line(Position pos, int d, int l)
	{
		start = pos;
		direction = Position.adjustDirection(d);
		length = l;
		resetPositions();
	}

	/**
	 * Sets the positions of this line based on its starting position, length,
	 * and direction
	 */
	private void resetPositions()
	{
		positions = new Position[length];
		Position pos = start;
		for(int i = 0; i < length; i++)
		{
			positions[i] = pos;
			pos = pos.getAdjacentPosition(direction);
		}
	}
	
	/**
	 * Returns a string representation of this line
	 * <p>[start] [direction] [length]
	 * <p>(0, 2) UP 3
	 */
	public String toString()
	{
		String s = start.toString() + " " + Position.toString(direction) + 
				" " + length;
		return s;
	}
}
