import javax.swing.*;
import java.awt.*;
import java.awt.Image;
/**
 * Write a description of class Frame here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Frame extends JFrame
{
    public Frame()
    {
    	int width=420;//needs to be divisible by 21
    	int numRows=3;
        setResizable(false);
        pack();
        add(new Pics(width*numRows,numRows));
        setSize(width*numRows,numRows*width);
        setTitle("Snake");
        setLocationRelativeTo(null);

    }

    public static void main(String [] args)
    {
        Frame window=new Frame();
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
