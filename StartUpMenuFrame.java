import javax.swing.*;
import java.awt.EventQueue;

/**
 * Write a description of class Frame here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class StartUpMenuFrame extends JFrame
{
	private static final long serialVersionUID = 1L;

	public StartUpMenuFrame()
    {
    	int height = 300; // needs to be divisible by 25
    	int numRows = 3;
        setResizable(false);
        add(new StartMenu(height * numRows, numRows)); // longer in height than width
        setSize(numRows * height * (7 * numRows) / (7 * numRows + 4), numRows * height);
        pack();
        setTitle("Tic-Tac-Toe");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String [] args)
    {
    	  EventQueue.invokeLater(new Runnable() {
              @Override
              public void run() {                
                  JFrame ex = new StartUpMenuFrame();
                  ex.setVisible(true);                
              }
          });
    }
}
