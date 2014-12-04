import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;



public class ShipPlacement
{
	private ArrayList<Gene> population_;
	private double[] weight_;
	
	private String saveFile_ = "population.txt";
	
	private int ancestors_ = 50;
	
	private int curIndex_ = 0;
	
	private int numShips_ = 5;
	private int[] shipSizes_ = {3,2,3,4,5};
	
	private Boolean[][] board_;
	
	private int rows_;
	private int cols_;
	
	//moves coefficient
	private double alpha_ = .4;
	
	//ships alive coefficient
	private double beta_ = .6;
	
	//mutation rate
	private double evoRate_ = .05;
	
	//favor best gets to mate
	private double favor_ = .5;
	
	public ShipPlacement(int rows, int cols)
	{
		population_ = new ArrayList<Gene>();
		
		for(int x = 0; x < ancestors_; x ++)
		{
			population_.add(new Gene(numShips_, shipSizes_));
		}
		
		weight_ = new double[ancestors_];
			
		rows_ = rows;
		cols_ = cols;
		
		board_ = new Boolean[rows_][cols_];
		
		//add new board
		//TODO: Remove once we have a stable population. Reinstate to reinitilize population
		for(int x = 0; x < ancestors_; x ++)
		{
			population_.set(x, randomizeBoard());
		}		
		
		//see if there is a population to load
		System.out.println("Loading population from file");
		loadPopulation();
	}
	
	public void loadPopulation()
	{
		try
		{
			BufferedReader in = new BufferedReader(new FileReader(saveFile_));
			
			String line = in.readLine();
			
			int skip = Integer.valueOf(line);
			
			if(skip == 1)
			{
				//0=true, 1=false
				//if skip == 0, then lets do this; otherwise, keep random population
				line = in.readLine();
				curIndex_ = Integer.valueOf(line);
				
				int index = 0;
				while((line = in.readLine())!=null)
				{
					population_.set(index, new Gene(line));
					index ++;
				}
			}
			in.close();
		}
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void savePopulation()
	{
		try {
			PrintWriter out = new PrintWriter("Population.txt");
			out.println(String.valueOf(1));
			out.println(String.valueOf(curIndex_));
			for(int x = 0; x < ancestors_; x ++)
			{
				//System.out.println("Saving " + x);
				out.println(population_.get(x).writeGene());
			}
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setWeight(int numMoves, int shipsAlive)
	{		
		if(curIndex_ <= ancestors_)
		{
			population_.get(curIndex_ - 1).weight_ = alpha_ * numMoves + beta_ * shipsAlive;
		}
		savePopulation();
	}
	
	public Gene getBoard()
	{
		if(curIndex_ >= ancestors_)
		{
			//all the previous ancestors have been used up
			
			curIndex_ = 0;
			
			//clean house and get a new population
			Gene[] wList = new Gene[ancestors_];
			
			//quick selection sort
			for(int x = 0; x < ancestors_; x ++)
			{
				double high = 0;
				for(int y = x; y < ancestors_; y ++)
				{
					if(population_.get(y).weight_ >= high)
					{
						wList[x] = population_.get(y);
						high = population_.get(y).weight_;
					}
				}
			}
			
			population_.clear();
			
			System.out.println("Repopulating");
			
			//repopulate
			//make this a weighted repopulation
			Gene best = wList[0];
			Gene secondBest = wList[1];
			
			//last 2 get culled for the best
			//wList[ancestors_ - 1] = new Gene(best);
			//wList[ancestors_ - 2] = new Gene(secondBest);
			
			for(int x = 0; x < ancestors_; x ++)
			{			
				Gene child;
				
				double prob = Math.random() * 1;
				
				if(prob > favor_)
				{		
					//2/ancestor times we will not have random mating, it will be the best
					int mate1 = (int)(Math.random()*(ancestors_ - 1));
					int mate2 = (int)(Math.random()*(ancestors_ - 1));
					
					while(mate2 == 1)
					{
						//no mating with self
						mate2 = (int)(Math.random()*(ancestors_ - 1));
					}
					//first gets first dibs
					child = nextGene(wList[mate2], wList[1]);
				}
				
				else
				{
					int mate = (int)(Math.random()*(ancestors_ - 1));
					while(mate == 0)
					{
						//no mating with self
						//you just get the best
						mate = (int)(Math.random()*(ancestors_ - 1));
					}
					child = nextGene(best, wList[mate]);
				}
				
				population_.add(child);
			}
			System.out.println("Finished Repopulation");
			
		}
		
		//get the gene at our current index
		Gene g = population_.get(curIndex_);

		curIndex_ ++;
		
		System.out.println("Returning new gene");

		return g;
	}
	
	private Gene nextGene(Gene one, Gene two)
	{
		//create a new gene
		Gene nG = new Gene(numShips_, shipSizes_);
		
		
		//couplate the two given genes
		//make sure that the result is valid. 

		System.out.println("Couplating");
		for(int x = 0; x < numShips_; x ++)
		{
			double which = (Math.random() * 1);
			
			if(which > .50)
				nG.gene_.set(x, new Chromosome(one.gene_.get(x)));
			
			else
				nG.gene_.set(x, new Chromosome(two.gene_.get(x)));
		}
		
		if((Math.random())*1 < evoRate_)
		{
			//straight up combination didn't work, try mutation
			nG.mutateGene();
		}
		
		int tries = 0;
		while(!validBoard(nG.gene_))
		{
			tries ++;
			nG = randomizeBoard();//new Gene(numShips_, shipSizes_);

			if(tries < 5000)
			{
				//try again
				for(int x = 0; x < numShips_; x ++)
				{
					double which = (Math.random() * 1);
										
					if(which > .50)
						nG.gene_.set(x, new Chromosome(one.gene_.get(x)));
					
					else
						nG.gene_.set(x, new Chromosome(two.gene_.get(x)));
				}
				
				//child is no good, mutate till workable
				//mutate till we get a viable pair
				
				
				if((Math.random())*1 < evoRate_)
				{
					//straight up combination didn't work, try mutation
					nG.mutateGene();
				}
			}
		}

		System.out.println("Child took : " + tries + " tries");
		return nG;
	}
	
	public Gene randomizeBoard()
	{		
		Gene gene = new Gene(numShips_, shipSizes_);
		
		do
		{
			gene.randomizeGene(rows_, cols_);

		}while(!validBoard(gene.gene_));
		
		return gene;
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
			
			//does the ship start off the board?
			if(xMark < 0 || xMark >= rows_ || yMark < 0 || yMark >= cols_)
			{
				return false;
			}
			
			//check if the ship goes off the board or overlaps another
			if(genes.get(x).direction_ == 0)
			{
				//up
				for(int i = 0; i < genes.get(x).shipSize_; i ++)
				{
					if(xMark < 0 || !board_[xMark][yMark])
					{
						return false;
					}
					
					board_[xMark][yMark] = false;
					xMark --;
				}
			}

			if(genes.get(x).direction_ == 1)
			{
				//right
				for(int i = 0; i < genes.get(x).shipSize_; i ++)
				{
					if( yMark >= cols_ || !board_[xMark][yMark])
					{
						return false;
					}
					
					board_[xMark][yMark] = false;
					yMark ++;
				}	
			}
			
			if(genes.get(x).direction_ == 2)
			{
				//down
				for(int i = 0; i < genes.get(x).shipSize_; i ++)
				{
					if(xMark >= rows_ || !board_[xMark][yMark] )
					{
						return false;
					}
					
					board_[xMark][yMark] = false;
					xMark ++;
				}
			}
			
			if(genes.get(x).direction_ == 3)
			{
				//left
				for(int i = 0; i < genes.get(x).shipSize_; i ++)
				{
					if(yMark < 0 || !board_[xMark][yMark])
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
