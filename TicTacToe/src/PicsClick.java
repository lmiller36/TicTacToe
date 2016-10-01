import javax.swing.*;
import java.awt.*;
import java.awt.Image;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.Toolkit;
import javax.swing.Timer;

/**
 * Write a description of class FramePics here.
 * 
 * @author (your name)
 * @version (a version number or a date)
 */
public class PicsClick extends JPanel implements ActionListener {

	final int scale = 100;
	int sideLength = 0;
	int rows = 0;
	private int xClick = 10000000;
	private int yClick = 10000000;
	private final int DELAY = 150;
	String[][] ownerOfTile = null;
	String winner="No One";
	boolean playerXTurn=true;
	boolean inGame=true;
	boolean onlyOnce=true;
	boolean lastWinner=false;//true is X, false is O
	int numMoves=0;
	int [] box=null;


	public PicsClick()// default to 3 rows with a width of 1260
	{

		setBackground(Color.white);
		setFocusable(true);

		setPreferredSize(new Dimension(1260, 1500));
		sideLength = 1500;
		ownerOfTile = new String[3][3];
		addMouseListener(new MousePressListener());
		initGame();

	}

	public PicsClick(int width, int numRows)// parameter
	{

		setBackground(Color.white);
		setFocusable(true);
		//setSize((7*numRows)*(width/(7*numRows+4))*numRows,numRows*width);

		setPreferredSize(new Dimension(width*(7*numRows)/(7*numRows+4),width));
		sideLength = 7*numRows * (width / (7*numRows+4));
		rows = numRows;
		ownerOfTile = new String[numRows][numRows];

		addMouseListener(new MousePressListener());
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		doDrawing(g);
	}

	private void initGame() {
		timer = new Timer(DELAY, this);
		timer.start();
	}

	private void doDrawing(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		int twnty = sideLength / (rows*7);
		for(int count=0;count<rows-1;count++)
		{
			g2.fillRect(0, (6+7*count) * twnty, sideLength, twnty);
			//	g2.fillRect(0, 13 * twnty, sideLength, twnty);

			//g2.fillRect(6 * twnty, 0, twnty, sideLength);
			g2.fillRect((6+7*count) * twnty, 0, twnty, sideLength);
		}

		Font proportionalFont = new Font("Helvetica", Font.BOLD, sideLength / rows);
		FontMetrics metrFont = getFontMetrics(proportionalFont);
		g2.setFont(proportionalFont);

		String[] letters = { "X", "O" };
		for (int countOuter = 0; countOuter < rows; countOuter++) {
			for (int countInner = 0; countInner < rows; countInner++) {
				String xLetter = "";

				if (ownerOfTile[countOuter][countInner] != null) {
					if (ownerOfTile[countOuter][countInner].equals("a")) {
						xLetter = letters[0];
					} else if (ownerOfTile[countOuter][countInner].equals("b")) {
						xLetter = letters[1];
					}
				}

				g2.setColor(Color.cyan);
				g2.drawString(xLetter, (3 + 7 * countInner) * twnty - metrFont.stringWidth(xLetter) / 2,
						(10 + 7 * countOuter) * twnty - metrFont.getHeight() / 2);

			}
		}

		g2.setColor(Color.black);
		Font proportionalFontSmall = new Font("Helvetica", Font.BOLD, sideLength / (3*rows));
		FontMetrics metrFontSmall = getFontMetrics(proportionalFontSmall);
		g2.setFont(proportionalFontSmall);
		if(!inGame)
		{
			if(onlyOnce)
			{
				winner="Winner: "+winner;
				onlyOnce=false;
			}

			g2.drawString(winner,sideLength/2-metrFontSmall.stringWidth(winner)/2,(7*rows+2)*sideLength/(7*rows));

			//replay box
			int plus=15;
			g2.setColor(Color.red);
			g2.fillRect(sideLength/2+metrFontSmall.stringWidth(winner)/2+plus,(7*rows+1)*sideLength/(7*rows)-metrFontSmall.getHeight()/2,
					metrFontSmall.getHeight(),metrFontSmall.getHeight());
			g2.setColor(Color.black);
			g2.drawString("R",sideLength/2+metrFontSmall.stringWidth(winner)/2+2*plus,(7*rows+1)*sideLength/(7*rows)+2*plus);
			int [] temp={sideLength/2+metrFontSmall.stringWidth(winner)/2+plus,(7*rows+1)*sideLength/(7*rows)-metrFontSmall.getHeight()/2,metrFontSmall.getHeight()};
			box=temp;
		}
		else
		{

			String currentP="It's ";
			if(playerXTurn)
			{
				currentP+="X";
			}
			else
			{
				currentP+="O";
			}
			currentP+="'s turn";
			g2.drawString(currentP,sideLength/2-metrFontSmall.stringWidth(currentP)/2,(7*rows+2)*sideLength/(7*rows));
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
	}

	// row,column or null
	public int [] whichQuadrant(int clickX,int clickY)//a return of null means it is not in a quadrant
	{
		int twnty=sideLength/(7*rows);
		int a=0;
		int row=-1;
		int col=-1;
		int b=0;

		while(a*twnty<sideLength&&(row==-1||col==-1))
		{
			//System.out.println(a*twnty+" "+(a+6)*twnty);
			if(clickY>=a*twnty&&clickY<=(a+6)*twnty)
			{
				row=b;
			}

			if(clickX>=a*twnty&&clickX<=(a+6)*twnty)
			{
				col=b;
			}

			a+=7;
			b++;
		}
		if(row!=-1&&col!=-1&&row<rows&&col<rows)
		{
			return new int []{row,col};
		}
		else
		{
			return null;
		}

	}

	public boolean checkWinner()
	{

		int rowCount=0;
		int colCount=0;
		boolean hasWon=false;

		while(rowCount<rows&&!hasWon)//check horizontal rows
		{
			colCount=0;
			while(colCount<rows-1)
			{
				if(ownerOfTile[rowCount][colCount]!=null&&ownerOfTile[rowCount][colCount].equals(ownerOfTile[rowCount][colCount+1]))
				{
					colCount++;
					hasWon= true;
				}
				else
				{
					colCount+=rows;
					hasWon=false;
				}

			}
			if(hasWon)
			{
				return true;
			}
			rowCount++;

		}

		colCount=0;

		while(colCount<rows&&!hasWon)//check vertical rows
		{
			rowCount=0;
			while(rowCount<rows-1)
			{
				if(ownerOfTile[rowCount][colCount]!=null&&ownerOfTile[rowCount][colCount].equals(ownerOfTile[rowCount+1][colCount]))
				{
					rowCount++;
					hasWon= true;
				}
				else
				{
					rowCount+=rows;
					hasWon=false;
				}

			}
			if(hasWon)
			{
				return true;
			}
			colCount++;

		}



		rowCount=0;


		hasWon=true;
		while(rowCount<rows-1&&hasWon)//top left to bottom right diagonal line
		{
			if(ownerOfTile[rowCount][rowCount]!=null&&ownerOfTile[rowCount][rowCount].equals(ownerOfTile[rowCount+1][rowCount+1]))
			{
				rowCount++;
				hasWon= true;
			}
			else
			{
				rowCount+=rows;
				hasWon=false;
			}

		}

		if(hasWon)
		{
			return true;
		}

		rowCount=0;


		hasWon=true;
		while(rowCount<rows-1&&hasWon)//top right to bottom left diagonal line
		{
			if(ownerOfTile[rows-1-rowCount][rowCount]!=null&&ownerOfTile[rows-1-rowCount][rowCount].equals(ownerOfTile[rows-rowCount-2][rowCount+1]))
			{
				rowCount++;
				hasWon= true;
			}
			else
			{
				rowCount+=rows;
				hasWon=false;
			}

		}

		if(hasWon)
		{
			return true;
		}

		return false;
	}


	public void playMove()
	{

		int [] temp=whichQuadrant(xClick,yClick);


		if(temp!=null&&ownerOfTile[temp[0]][temp[1]]==null)
		{
			if(playerXTurn)
			{
				ownerOfTile[temp[0]][temp[1]]="a";
				playerXTurn=false;
			}
			else
			{
				ownerOfTile[temp[0]][temp[1]]="b";
				playerXTurn=true;
			}
			numMoves++;
			boolean someoneWon=checkWinner();
			inGame=!someoneWon;

			if(!inGame)
			{
				
				//winner=numMoves%2!=0&&numMoves!=0?"X":"Y";
				if(numMoves%2!=0&&!playerXTurn)
				{
					winner="X";
				}
				else
				{
					winner="O";
				}
				
				if(winner.equals("X"))
				{
					lastWinner=true;
				}
				else
				{
					lastWinner=false;
				}
			}
			else if(inGame&&numMoves==(rows*rows))
			{
				inGame=false;
				lastWinner=!playerXTurn;

			}


		}



	}

	public void restartGame(int clickA,int clickB,int [] coord)
	{
		//inside restart box
		if(clickA>=coord[0]&&clickA<=coord[0]+coord[2]&&clickB>=coord[1]&&clickB<=coord[1]+coord[2])
		{
			inGame=true;
			box=null;
			ownerOfTile = new String[rows][rows];
			 onlyOnce=true;
			winner="No One";
			if(lastWinner)
			{
				playerXTurn=false;

			}
			else
			{
				playerXTurn=true;
			}
			 numMoves=0;
		}
	}


	public class MousePressListener implements MouseListener {

		public void mousePressed(MouseEvent event) {

			xClick = event.getX();
			yClick = event.getY();
			if(inGame)
			{
				playMove();
			}
			else
			{
				restartGame(xClick,yClick,box);
			}
			repaint();
		}

		public void mouseReleased(MouseEvent event) {
		}

		public void mouseClicked(MouseEvent event) {

		}

		public void mouseEntered(MouseEvent event) {
		}

		public void mouseExited(MouseEvent event) {
		}

	}
}
