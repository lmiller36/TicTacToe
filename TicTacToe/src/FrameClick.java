import javax.swing.*;
import java.awt.*;
import java.awt.Image;
import java.awt.EventQueue;
/**
 * Write a description of class Frame here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class FrameClick extends JFrame
{
    public FrameClick()
    {
    	int width=300;//needs to be divisible by 25
    	int numRows=3;
        setResizable(false);
        pack();
        add(new PicsClick(width*numRows,numRows));//longer in height than width
    //    setSize((7*numRows)*(width/(7*numRows+4))*numRows,numRows*width);
        setSize(numRows*width*(7*numRows)/(7*numRows+4),numRows*width);
        setTitle("Snake");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }

    public static void main(String [] args)
    {
    	  EventQueue.invokeLater(new Runnable() {
              @Override
              public void run() {                
                  JFrame ex = new FrameClick();
                  ex.setVisible(true);                
              }
          });
    }
}
