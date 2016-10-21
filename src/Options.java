import javax.swing.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class Options extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L; // just to avoid a yellow flag
	private static final double EPSILON=1e-14;
	final int scale = 100;

	int xClick=-1;
	int yClick=-1;

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

	/**
	 * PVP if player vs player, PVC if player vs computer, null if not assigned yet
	 */
	String gameMode=null;

	String nameP1=null;
	String nameP2=null;

	/**
	 * [circle 1,2.....n][x,y,radius]
	 */

	int [][] locationsOfCirclesGameMode=null;

	/**
	 * [Box 1,2....n][x,y,width,height]
	 */


	ArrayList<int []> locationsOfNameBoxes=new ArrayList<int []>();

	String firstPlayer=null;
	
	/**
	 * Easy Medium or Hard
	 */
	String computerDifficulty=null;


	private Timer timer;
	private final int DELAY = 1200;

	public Options(int myHeight, int numRows) // parameter constructor
	{
		setBackground(Color.white);
		setFocusable(true);

		setPreferredSize(new Dimension(myHeight * ( 7 * numRows) / (7 * numRows + 4), myHeight));

		width = myHeight * ( 7 * numRows) / (7 * numRows + 4);
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
	 * This adds the options title
	 */

	public void AddTitle(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		int fontSize=(int)Math.round(width*.15);
		Font TitleFont = new Font("Helvetica", Font.BOLD,fontSize);
		FontMetrics TitleFontMetrics = getFontMetrics(TitleFont);
		g2.setFont(TitleFont);

		//draws Options in the middle of the width
		String msg="Options";
		double portionDownOnScreen=.15*height;
		g2.setColor(Color.black);
		int beginX=width/2-TitleFontMetrics.stringWidth(msg)/2;
		int beginY=(int)Math.round(portionDownOnScreen);
		g2.drawString(msg,beginX,beginY);
	}

	public void drawBoxSurroundingWord(int startX,int startY,int width, int height,Graphics g,Color color){
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(color);

		g2.drawRect(startX,startY-4*height/5,width,height);
	}

	/**
	 * Draws an arc of a circle (outside black circle with a wide inner circle)
	 * returns the int [] containing starting x,y, width,height for a box to contain the msg
	 */

	public void DrawCircle(Graphics g, int startX, int startY, int circleDiameter,String msg,Color color,boolean drawRect)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(color);
		g2.fillArc(startX,startY,circleDiameter,circleDiameter,0,360);	

		g2.setColor(Color.white);
		int portionOfCircleDiameter=(int)Math.round(.75*circleDiameter);
		g2.fillArc(startX+portionOfCircleDiameter/6,startY+portionOfCircleDiameter/6,
				portionOfCircleDiameter,portionOfCircleDiameter,0,360);//sorry about the 6


		/**
		 * Add Msg if needed
		 */
		if(msg!=null)
		{
			g2.setColor(Color.BLACK);
			int fontSize=(int)Math.round(width*.05);
			Font msgFont = new Font("Helvetica", Font.BOLD,fontSize);
			FontMetrics msgFontMetrics = getFontMetrics(msgFont);
			g2.setFont(msgFont);

			g2.drawString(msg,startX+circleDiameter/2-
					msgFontMetrics.stringWidth(msg)/2,startY+3*circleDiameter/2);

			if(drawRect){

				drawBoxSurroundingWord(startX+circleDiameter/2-
						msgFontMetrics.stringWidth(msg)/2,startY+
						3*circleDiameter/2,msgFontMetrics.stringWidth(msg),msgFontMetrics.getHeight(),g,Color.black);
				locationsOfNameBoxes.add(new int []{startX+circleDiameter/2-
						msgFontMetrics.stringWidth(msg)/2,startY+
						3*circleDiameter/2,msgFontMetrics.stringWidth(msg),msgFontMetrics.getHeight()});


			}
		}


	}
	/**
	 * Adds the mini concentric circles(between 1&3)
	 *circles are a tenth of the height
	 *
	 */

	public void AddOptionCircles(Graphics g, int startY, int numOfCircles, int circleDiameter,String [] msgs,Color color,boolean drawRect)
	{

		//	Graphics2D g2 = (Graphics2D) g;

		//black circles

		if(numOfCircles==1)// 1 circle
		{
			DrawCircle(g,width/2-circleDiameter/2,startY,circleDiameter,msgs[0],color,drawRect);

		}
		else if(numOfCircles==2)//2 circles
		{
			DrawCircle(g,width/2-3*circleDiameter/2,startY,circleDiameter,msgs[0],color,drawRect);

			DrawCircle(g,width/2+circleDiameter/2,startY,circleDiameter,msgs[1],color,drawRect);

		}
		else// 3 circles
		{
			DrawCircle(g,width/2-circleDiameter/2,startY,circleDiameter,msgs[0],color,drawRect);
			DrawCircle(g,width/2-5*circleDiameter/2,startY,circleDiameter,msgs[1],color,drawRect);
			DrawCircle(g,width/2+3*circleDiameter/2,startY,circleDiameter,msgs[2],color,drawRect);
		}


		//portion inside circle
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
		AddTitle(g);
		AddGameModeOptions(g);

		if(gameMode!=null){
			AddNamesStartOptions(g);

			AddFillInCircleGameMode(g);
		}

		if(firstPlayer!=null){
			if(gameMode.equals("PVC"))
			{
				AddDifficultyCircles(g);
				if(computerDifficulty!=null){
					AddPlayButton(g);

				}
			}
			else
				AddPlayButton(g);

		}

		//g2.setColor(Color.cyan);
		//g2.fillRect(xClick, yClick, 15, 15);



	}

	public void AddDifficultyCircles(Graphics g){
		//	Graphics2D g2 = (Graphics2D) g;
		int startOfFirstOptionsY=(int)(height*.65);
		int numOfCircles=3;
		int circleDiameter=(int)(.15*height);

		String [] msgs={"Easy","Medium", "Hard"};

		AddOptionCircles(g,startOfFirstOptionsY,numOfCircles,circleDiameter,msgs,Color.red,false);

		locationsOfNameBoxes=new ArrayList<int []>();


	}

	public void AddFillInCircleStartingPlayer(Graphics g){
		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(Color.black);
		if(firstPlayer.equals("p1")){
			int rad=locationsOfCirclesGameMode[0][2];
			g2.fillArc(locationsOfCirclesGameMode[0][0]-rad,locationsOfCirclesGameMode[0][1]-rad+(int)(.25*height),2*rad,2*rad,0,360);
		}
		else//p2 starts
		{
			int rad=locationsOfCirclesGameMode[1][2];
			g2.fillArc(locationsOfCirclesGameMode[1][0]-rad,locationsOfCirclesGameMode[1][1]-rad+(int)(.25*height),2*rad,2*rad,0,360);
		}

	}

	public void AddFillInCircleGameMode(Graphics g){
		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(Color.black);
		if(gameMode.equals("PVP")){
			int rad=locationsOfCirclesGameMode[0][2];
			g2.fillArc(locationsOfCirclesGameMode[0][0]-rad,locationsOfCirclesGameMode[0][1]-rad,2*rad,2*rad,0,360);
		}
		else//PVC
		{
			int rad=locationsOfCirclesGameMode[1][2];
			g2.fillArc(locationsOfCirclesGameMode[1][0]-rad,locationsOfCirclesGameMode[1][1]-rad,2*rad,2*rad,0,360);
		}

	}

	public void AddPlayButton(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;

		int widthOfPlayButton=width/10;
		int startOfFirstOptionsY=(int)(height*.9);


		g2.setColor(Color.black);
		g2.fillRect(width/2 - widthOfPlayButton/2,startOfFirstOptionsY,widthOfPlayButton,widthOfPlayButton);
		AddTriangleToPlayButton(width/2 - widthOfPlayButton/2,startOfFirstOptionsY,widthOfPlayButton,g);

	}
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

	public void AddNamesStartOptions(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		int startOfFirstOptionsY=(int)(height*.6);
		//int numOfCircles=2;
		//int circleDiameter=(int)(.15*height);

		String name1=nameP1!=null?nameP1:"name1";
		String name2=nameP2!=null?nameP2:"name2";
		String [] msgs={name1,name2};

		/*
		AddOptionCircles(g,startOfFirstOptionsY,numOfCircles,circleDiameter,msgs,Color.green,true);
		 */

		locationsOfNameBoxes=new ArrayList<int []>();

		g2.setColor(Color.BLACK);
		int fontSize=(int)Math.round(width*.075);
		Font msgFont = new Font("Helvetica", Font.BOLD,fontSize);
		FontMetrics msgFontMetrics = getFontMetrics(msgFont);
		g2.setFont(msgFont);

		int portionFromMiddle=(int)(width*.1);
		g2.drawString(msgs[0],width/2-portionFromMiddle-msgFontMetrics.stringWidth(msgs[0]),startOfFirstOptionsY);
		//surrounding rect

		drawBoxSurroundingWord(width/2-portionFromMiddle-msgFontMetrics.stringWidth(msgs[0]),startOfFirstOptionsY,
				msgFontMetrics.stringWidth(msgs[0]),msgFontMetrics.getHeight(),g,Color.black);


		g2.drawString(msgs[1],width/2+portionFromMiddle,startOfFirstOptionsY);
		//surrounding rect

		drawBoxSurroundingWord(width/2+portionFromMiddle,startOfFirstOptionsY,
				msgFontMetrics.stringWidth(msgs[1]),msgFontMetrics.getHeight(),g,Color.black);



	}

	public void AddGameModeOptions(Graphics g){
		int startOfFirstOptionsY=(int)(height*.25);
		int numOfCircles=2;
		int circleDiameter=(int)(.15*height);
		String [] msgs={"PVP","PVC"};
		AddOptionCircles(g,startOfFirstOptionsY,numOfCircles,circleDiameter,msgs,Color.blue,false);

		locationsOfCirclesGameMode=new int [numOfCircles][3];//2 circles, [x,y,radius]

		locationsOfCirclesGameMode[0][0]=width/2-circleDiameter;//PVP circle 1 X vertex
		locationsOfCirclesGameMode[0][1]=startOfFirstOptionsY+circleDiameter/2;//PVP circle 1 Y vertex
		locationsOfCirclesGameMode[0][2]=circleDiameter/2;//PVP circle 1 radius


		locationsOfCirclesGameMode[1][0]=width/2+circleDiameter;//PVC circle 2 X vertex
		locationsOfCirclesGameMode[1][1]=startOfFirstOptionsY+circleDiameter/2;//PVC circle 2 Y vertex
		locationsOfCirclesGameMode[1][2]=circleDiameter/2;//PVP circle 2 radius



	}



	@Override
	public void actionPerformed(ActionEvent e) 
	{
		repaint();
	}

	public boolean inCircle(int startX,int startY,int vertexX,int vertexY,int radius){
		double xDistance=vertexX-startX;
		double yDistance=vertexY-startY;


		double distance=Math.sqrt(Math.pow(xDistance, 2)+Math.pow(yDistance, 2));
		return distance<radius||Math.abs(distance-radius)<=EPSILON;

	}


	/**
	 * Kept for when clicking is used
	 */

	public class MousePressListener implements MouseListener
	{
		public void mousePressed(MouseEvent event)
		{
			xClick = event.getX();
			yClick = event.getY();

			System.out.println("CLICK: "+xClick+" "+ yClick);

			if(gameMode==null){

				if(inCircle(xClick,yClick,locationsOfCirclesGameMode[0][0],locationsOfCirclesGameMode[0][1],
						locationsOfCirclesGameMode[0][2]))//In Circle 1
				{
					gameMode="PVP";
				}

				else if(inCircle(xClick,yClick,locationsOfCirclesGameMode[1][0],locationsOfCirclesGameMode[1][1],
						locationsOfCirclesGameMode[1][2]))//In Circle 1
				{
					gameMode="PVC";
				}
			}
			else if(firstPlayer==null){

				if(inCircle(xClick,yClick,locationsOfCirclesGameMode[0][0],locationsOfCirclesGameMode[0][1]+(int)(.25*height),
						locationsOfCirclesGameMode[0][2]))//In Circle 1
				{
					//	firstPlayer="p1";
				}

				else if(inCircle(xClick,yClick,locationsOfCirclesGameMode[1][0],locationsOfCirclesGameMode[1][1]+(int)(.25*height),
						locationsOfCirclesGameMode[1][2]))//In Circle 1
				{
					//firstPlayer="p2";
				}

				firstPlayer="p1";

				
			}
			else if(gameMode.equals("PVC")&&computerDifficulty==null){
			
				computerDifficulty="EASY";
			}



			repaint();
		}

		public void mouseReleased(MouseEvent event) {}
		public void mouseClicked(MouseEvent event) {}
		public void mouseEntered(MouseEvent event) {}
		public void mouseExited(MouseEvent event) {}

	}
}
