import javax.swing.*;
import java.awt.*;
import java.awt.Image;
import java.awt.Font;
import java.awt.FontMetrics;
/**
 * Write a description of class FramePics here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Pics extends JPanel
{

final int scale=100;
int sideLength=0;
int rows=0;
String [][] ownerOfTile=null;
    public Pics()//default to 3 rows with a width of 1260
    {

        setBackground(Color.white);
        setFocusable(true);

        setPreferredSize(new Dimension(1260, 1260));
        sideLength=1260;
        ownerOfTile=new String [3][3]
        
    }
    
    public Pics(int width,int numRows)//parameter
    {

        setBackground(Color.white);
        setFocusable(true);

        setPreferredSize(new Dimension(width,width));
        sideLength=width;
        rows=numRows;
        ownerOfTile=new String [numRows][numRows];
       fillRows("aaaabaaaa");
    }
    
    public void fillRows(String example)
    {
    	for(int count=0;count<example.length();count++)
    	{
    		ownerOfTile[count/3][count%3]=example.substring(count,count+1);
    	}
    	
    }
    
    

    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) 
    {
        Graphics2D g2 =(Graphics2D)g;
        
        int twnty=sideLength/21;
        g2.fillRect(0,6*twnty,sideLength,twnty);
        g2.fillRect(0,13*twnty,sideLength,twnty);
        
        g2.fillRect(6*twnty,0,twnty,sideLength);
        g2.fillRect(13*twnty,0,twnty,sideLength);

       
       
        Font proportionalFont = new Font("Helvetica", Font.BOLD, sideLength/3);
        FontMetrics metrFont = getFontMetrics(proportionalFont);
        g2.setFont(proportionalFont);
        
    
        
        String [] letters={"L","M"};
        for(int countOuter=0;countOuter<3;countOuter++)
        {
        	for(int countInner=0;countInner<3;countInner++)
        	{
        		String xLetter="";
        		
        		if(ownerOfTile[countOuter][countInner].equals("a"))
        		{
        			xLetter=letters[0];
        		}
        		else if(ownerOfTile[countOuter][countInner].equals("b"))
        		{
        			xLetter=letters[1];
        		}
        	
        		g2.setColor(Color.cyan);
	        	 g2.drawString(xLetter,(3+7*countInner)*twnty-metrFont.stringWidth(xLetter)/2,(10+7*countOuter)*twnty-metrFont.getHeight()/2);

        	}
        }
        

    }
}
 


