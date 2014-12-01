import java.util.ArrayList;


public class Gene 
{
	ArrayList<Chromosome> gene_;
		
	public double weight_;
	
	private int numMutations_ = 1;
	
	public Gene(int numChromosomes, int[] shipSizes)
	{
		gene_ = new ArrayList<Chromosome>();
		
		for(int x = 0; x < shipSizes.length; x ++)
		{
			System.out.println("Loading chromosome " + x);
			Chromosome c = new Chromosome();
			c.shipSize_ = shipSizes[x];
			
			gene_.add(c);
		}
	}
	
	public Gene(String g)
	{
		String chro;
		
		int index = g.indexOf(";");
		
		while(index < g.length())
		{
			chro = g.substring(0, index);
			
			Chromosome c = new Chromosome(chro);
			
			gene_.add(c);
			
			if(index+1 >= g.length())
			{
				break;
			}
			
			g = g.substring(index+1);
			index = g.indexOf(";");
		}
		
	}

	public void mutateGene()
	{
		int change = numMutations_;//(int)Math.random()%(gene_.size());
		
		int index = 0;
		
		Boolean[] changed = new Boolean[gene_.size()];
		
		for(int x = 0; x < gene_.size(); x ++)
		{
			changed[x] = false;
		}
		
		int x = 0;
		while(x < change)
		{
			index = (int)(Math.random() * gene_.size());
			
			if(!changed[index])
			{
				x ++;
				gene_.get(x).mutate();
			}
		}
	}
	
	
	public void randomizeGene(int rows, int cols)
	{
		for(int x = 0; x < gene_.size(); x ++)
		{
			gene_.get(x).row_ = ((int)(Math.random()*(rows-1)));
			gene_.get(x).col_ = ((int)(Math.random()*(cols-1)));
			
			gene_.get(x).direction_ = ((int)(Math.random()*4));
		}
	}
	
	public String writeGene()
	{
		String gene = "";
		
		for(int x = 0; x < gene_.size(); x++)
		{
			gene += gene_.get(x).getChromosome();
			
			gene += " ; ";
		}
		
		System.out.println("Writing " + gene);
		
		return gene;
	}
}
