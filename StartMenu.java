import javax.swing.*;
import java.awt.*;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class StartMenu extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L; // just to avoid a yellow flag
	final int scale = 100;


/**
 * number of x's (and the line) that must be drawn
 */

	int numberOfXForDisplay=0;

	boolean inGame = true;

	/**
	 * width of the board
	 */
	int width;
	
	/**
	 * height of the board
	 */
	int height;
	
	
	private Timer timer;
	private final int DELAY = 1200;

	public StartMenu(int myHeight, int numRows) // parameter constructor
	{
		setBackground(Color.white);
		setFocusable(true);

		setPreferredSize(new Dimension(300, myHeight));

		width = 300;
		height = myHeight;

		addMouseListener(new MousePressListener());
		initGame();
	}

	/**
	 * This displays the contents of the panel to the frame.
	 * 
	 * @param g is the graphics component to draw the frame
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		doDrawing(g);
	}

	/**
	 * Implements the timer in order to draw the x's at timed intervals
	 */
	private void initGame() 
	{
		timer = new Timer(DELAY, this);
		timer.start();
	}
	
	/**
	 * Draws all the elements on the startup menu
	 */

	private void doDrawing(Graphics g)
	{
		//	Graphics2D g2 = (Graphics2D) g;

		AddPlayButton(g);
		AddTicTacToeTitle(g);
		AddMiniTicTacToeBoard(g);
	}


	/**
	 * 
	 * Draws the lines of the mini tic tac toe board and plugs in its starting 
	 * position (x,y) into the x drawing method
	 */
	public void AddMiniTicTacToeBoard(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		double portionDownOnScreen=.3;
		int boxSize=(int)(Math.round(height*.3));
		int eighthOfBoxSize=boxSize/8;
		boxSize=eighthOfBoxSize*8;
		int startHeight=(int)Math.round(portionDownOnScreen*height);
		int startWidth=(width/2-boxSize/2);

		for(int a=2;a*eighthOfBoxSize<boxSize;a+=3)
		{			

			g2.fillRect(startWidth+a*eighthOfBoxSize,startHeight,eighthOfBoxSize,boxSize);
			g2.fillRect(startWidth,startHeight+a*eighthOfBoxSize,boxSize,eighthOfBoxSize);

		}

		AddXToMiniBoard(g,eighthOfBoxSize,startWidth,startHeight);

	}
	
	/**
	 * Draws the mini x's and the winning line(when needed)
	 */

	public void AddXToMiniBoard(Graphics g, int eighthOfBoxSize, int startWidth,int startHeight)
	{
		Graphics2D g2 = (Graphics2D) g;

		int fontSize=(int)Math.round(1.5*eighthOfBoxSize);
		Font MiniX = new Font("Helvetica", Font.BOLD,fontSize);
		FontMetrics MiniXMetrics = getFontMetrics(MiniX);
		g2.setFont(MiniX);


		int i=0;
		String msg="X";
		//this loop creates the x's
		while(i<numberOfXForDisplay&&i<3)
		{
			int startOfX=eighthOfBoxSize-MiniXMetrics.stringWidth(msg)/2
					+(i*3)*eighthOfBoxSize;
			int startOfY=eighthOfBoxSize+(i*3)*eighthOfBoxSize+MiniXMetrics.getHeight()/3;
			g2.drawString(msg,startOfX+startWidth,startOfY+startHeight);
			i++;
		}
		//this creates the line
		if(numberOfXForDisplay==4)
		{
			int [] XPoints={startWidth,startWidth+eighthOfBoxSize,
					startWidth+8*eighthOfBoxSize,startWidth+7*eighthOfBoxSize};
			int [] YPoints={startHeight+eighthOfBoxSize,startHeight,
					startHeight+7*eighthOfBoxSize,startHeight+8*eighthOfBoxSize};

			g2.fillPolygon(XPoints, YPoints, 4);
		}
	}
	
	/**
	 * Draws the title 
	 */

	public void AddTicTacToeTitle(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		int fontSize=(int)Math.round(width*.15);
		Font TitleFont = new Font("Helvetica", Font.BOLD,fontSize);
		FontMetrics TitleFontMetrics = getFontMetrics(TitleFont);
		g2.setFont(TitleFont);

		//draws Tic Tac Toe in the middle of the width
		String msg="Tic-Tac-Toe";
		double portionDownOnScreen=.15*height;
		g2.setColor(Color.black);
		int beginX=width/2-TitleFontMetrics.stringWidth(msg)/2;
		int beginY=(int)Math.round(portionDownOnScreen);
		g2.drawString(msg,beginX,beginY);

	}
	
	/**
	 * Creates the triangle of the  play button
	 */

	public void AddTriangleToPlayButton(int startX,int startY,int widthOfBox,Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(Color.green);
		//triangle starts a portion into the square
		double percentage=.2;
		int startTriangleX=startX+(int)Math.round(widthOfBox*percentage);
		int [] xs={startTriangleX,startTriangleX,startX+widthOfBox};
		int [] ys={startY,startY+widthOfBox,startY+widthOfBox/2};

		g2.fillPolygon(xs,ys,3);
	}

	
	/**
	 * 
	 * Draws the box and triangle that comprise the play button
	 */
	public void AddPlayButton(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;

		int widthOfPlayButton=width/5;


		g2.setColor(Color.black);
		g2.fillRect(width/2 - widthOfPlayButton/2,3*height/4,widthOfPlayButton,widthOfPlayButton);
		AddTriangleToPlayButton(width/2 - widthOfPlayButton/2,3*height/4,widthOfPlayButton,g);

	}

	/**
	 * Adds an element to numberOfXForDisplay until it is 5, where the var then resets to 0
	 */
	public void AdvanceDisplayX()
	{
		numberOfXForDisplay++;

		//first x,second x,third x, line
		if(numberOfXForDisplay>4)
		{
			numberOfXForDisplay=0;
		}
	}

	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		AdvanceDisplayX();
		repaint();
	}


/**
 * Kept for when clicking is used
 */

	public class MousePressListener implements MouseListener
	{
		public void mousePressed(MouseEvent event)
		{
			//xClick = event.getX();
			//yClick = event.getY();

			repaint();
		}

		public void mouseReleased(MouseEvent event) {}
		public void mouseClicked(MouseEvent event) {}
		public void mouseEntered(MouseEvent event) {}
		public void mouseExited(MouseEvent event) {}

	}
}
