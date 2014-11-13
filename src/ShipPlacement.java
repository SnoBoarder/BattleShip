import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;



public class ShipPlacement
{
	private Queue<ArrayList<Chromosome>> history_;
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
	
	public ArrayList<Chromosome> getBoard()
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
	
	private ArrayList<Chromosome> nextGene()
	{
		ArrayList<Chromosome> gene = new ArrayList<Chromosome>(numShips_);
		
		int first = 0;
		int second = 0;
		
		
		
		return gene;
	}
	
	private ArrayList<Chromosome> randomizeBoard()
	{		
		ArrayList<Chromosome> genes = new ArrayList<Chromosome>(numShips_);
		
		do
		{
			for(int x = 0; x < 5; x ++)
			{
				genes.get(x).shipType_ = x;	//ship type
				genes.get(x).shipSize_ = shipSizes_[x];
				genes.get(x).row_ = (int)Math.random() * rows_; //ship row
				genes.get(x).col_ = (int)Math.random() * cols_; //ship col
				genes.get(x).direction_ = (int)Math.random() * 4; //ship direction
			}
		}while(!validBoard(genes));
		
		return genes;
	}
	
	private Boolean validBoard(ArrayList<Chromosome> genes)
	{
		for(int x = 0; x < rows_; x ++)
		{
			for(int y = 0; y < cols_; y ++)
			{
				board_[x][y] = true;
			}
		}
		
		//place ships
		for(int x = 0; x < genes.size(); x ++)
		{
			//get direction, 1 = up, 2 = right, 3 = down, 4 = left
			//place each ship down
			//check if any ships overlap
			  //return false if they do
			int xMark = genes.get(x).row_;
			int yMark = genes.get(x).col_;
			
			if(genes.get(x).direction_ == 1)
			{
				//up
				for(int i = 0; i < genes.get(x).shipSize_; i ++)
				{
					if(!board_[xMark][yMark] || xMark < 0)
					{
						return false;
					}
					
					board_[xMark][yMark] = false;
					xMark --;
				}
			}

			if(genes.get(x).direction_ == 2)
			{
				//right
				for(int i = 0; i < genes.get(x).shipSize_; i ++)
				{
					if(!board_[xMark][yMark] || yMark >= cols_)
					{
						return false;
					}
					
					board_[xMark][yMark] = false;
					yMark ++;
				}	
			}
			
			if(genes.get(x).direction_ == 3)
			{
				//down
				for(int i = 0; i < genes.get(x).shipSize_; i ++)
				{
					if(!board_[xMark][yMark] || xMark >= rows_)
					{
						return false;
					}
					
					board_[xMark][yMark] = false;
					xMark ++;
				}
			}
			
			if(genes.get(x).direction_ == 4)
			{
				for(int i = 0; i < genes.get(x).shipSize_; i ++)
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
