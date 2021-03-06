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
			Chromosome c = new Chromosome();
			c.shipSize_ = shipSizes[x];
			c.shipType_ = x;
			gene_.add(c);
		}
	}
	
	public Gene(Gene g)
	{
		gene_ = new ArrayList<Chromosome>();
		for(int x = 0; x < g.gene_.size(); x ++)
		{
			gene_.add(new Chromosome(gene_.get(x)));
		}
		
	}
	
	public Gene(String g)
	{
		gene_ = new ArrayList<Chromosome>();
		
		String chro;
		
		int index = g.indexOf(";");
		
		weight_ = Double.valueOf(g.substring(0, index));
		
		g = g.substring(index+1);
		index = g.indexOf(";");
		
		while(index < g.length() && index != -1)
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
		int change = (int)(Math.random()*(gene_.size()));
		
		int index = 0;
		
		Boolean[] changed = new Boolean[gene_.size()];
		
		for(int x = 0; x < gene_.size(); x ++)
		{
			changed[x] = false;
		}
		
		int x = 0;
		while(x < change)
		{
			index = (int)(Math.random() * (gene_.size() - 1));
			
			if(!changed[index])
			{
				x ++;
				gene_.get(x).mutate();
				
				changed[index] = true;
			}
		}
	}
	
	
	public void randomizeGene(int rows, int cols)
	{
		for(int x = 0; x < gene_.size(); x ++)
		{
			gene_.get(x).row_ = ((int)(Math.random()*(rows-1)));
			gene_.get(x).col_ = ((int)(Math.random()*(cols-1)));
			
			gene_.get(x).direction_ = ((int)(Math.random()*3));		
		}
	}
	
	public String writeGene()
	{
		String gene = "";
		
		gene += weight_;
		
		gene += " ; " ;
		
		for(int x = 0; x < gene_.size(); x++)
		{
			gene += gene_.get(x).getChromosome();
			
			gene += " ; ";
		}
		
		//System.out.println("Writing " + gene);
		
		return gene;
	}
}
