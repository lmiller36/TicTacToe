
public class BasicBoard {
	
	public static void main(String [] args)
	{
		int size=5;
		final int board_size=3*size+4;
		for(int i=0;i<board_size;i++)
		{
			for(int j=0;j<board_size;j++)
			{
			if(i==0||j==0||(i%size)==0||(j%size==0)||)
			{
				System.out.print("*");
				
			}
			else
			{
			System.out.print(" ");
			}
			}
			System.out.println("");
		}
	}

}
