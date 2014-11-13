import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;



public class ShipPlacement
{
	private Queue<int[][]> history_;
	private double[] weight_;
	
	int ancestors_ = 10;
	
	private int numShips_ = 5;
	private int[] shipSizes_ = {3,2,3,4,5};
	
	private Boolean[][] board_;
	
	private int rows_;
	private int cols_;
	
	private double alpha_ = 1;
	private double beta_ = 1;
	
	public ShipPlacement(int rows, int cols)
	{
		weight_ = new double[ancestors_];
			
		rows_ = rows;
		cols_ = cols;
		
		board_ = new Boolean[rows_][cols_];
		
		//add new board
		for(int x = 0; x < ancestors_; x ++)
		{
			history_.add(randomizeBoard());
		}
	}
	
	public void setWeight(int numMoves, int shipsDead)
	{
		weight_[0] = alpha_ * numMoves - beta_ * shipsDead;
	}
	
	public int[][] getBoard()
	{
		history_.remove();

		for(int x = 0 ; x < ancestors_ - 1; x ++)
		{
			weight_[x] = weight_[x+1];
		}

		//add new gene to gene pool
		history_.add(nextGene());
		weight_[ancestors_ - 1] = 0;

		return history_.remove();
	}
	
	private int[][] nextGene()
	{
		int[][] gene = new int[numShips_][5];
		
		int first = 0;
		int second = 0;
		
		
		
		return gene;
	}
	
	private int[][] randomizeBoard()
	{		
		int[][] gene = new int[numShips_][5];
		
		do
		{
			for(int x = 0; x < 5; x ++)
			{
				gene[x][0] = x;	//ship type
				gene[x][1] = (int)Math.random() * rows_; //ship row
				gene[x][2] = (int)Math.random() * cols_; //ship col
				gene[x][3] = (int)Math.random() * 4; //ship direction
			}
			
			gene[0][4] = shipSizes_[0];
			gene[1][4] = shipSizes_[1];
			gene[2][4] = shipSizes_[2];
			gene[3][4] = shipSizes_[3];
			gene[4][4] = shipSizes_[4];
		}while(!validBoard(gene));
		
		return gene;
	}
	
	private Boolean validBoard(int[][] gene)
	{
		for(int x = 0; x < rows_; x ++)
		{
			for(int y = 0; y < cols_; y ++)
			{
				board_[x][y] = true;
			}
		}
		
		//place ships
		for(int x = 0; x < gene.length; x ++)
		{
			//get direction, 1 = up, 2 = right, 3 = down, 4 = left
			//place each ship down
			//check if any ships overlap
			  //return false if they do
			int xMark = gene[x][1];
			int yMark = gene[x][2];
			
			if(gene[x][3] == 1)
			{
				//up
				for(int i = 0; i < gene[x][4]; i ++)
				{
					if(!board_[xMark][yMark] || xMark < 0)
					{
						return false;
					}
					
					board_[xMark][yMark] = false;
					xMark --;
				}
			}

			if(gene[x][3] == 2)
			{
				//right
				for(int i = 0; i < gene[x][4]; i ++)
				{
					if(!board_[xMark][yMark] || yMark >= cols_)
					{
						return false;
					}
					
					board_[xMark][yMark] = false;
					yMark ++;
				}	
			}
			
			if(gene[x][3] == 3)
			{
				//down
				for(int i = 0; i < gene[x][4]; i ++)
				{
					if(!board_[xMark][yMark] || xMark >= rows_)
					{
						return false;
					}
					
					board_[xMark][yMark] = false;
					xMark ++;
				}
			}
			
			if(gene[x][3] == 4)
			{
				for(int i = 0; i < gene[x][4]; i ++)
				{
					if(!board_[xMark][yMark] || yMark < 0)
					{
						return false;
					}
					
					board_[xMark][yMark] = false;
					yMark --;
				}
			}
		}
		return true;
	}
}
