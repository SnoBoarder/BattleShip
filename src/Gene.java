import java.util.ArrayList;


public class Gene 
{
	ArrayList<Chromosome> gene_;
	
	public double weight_;
	
	public Gene(int numChromosomes)
	{
		gene_ = new ArrayList<Chromosome>(numChromosomes);
	}
	
	public void randomizeGene()
	{
		
	}
}
