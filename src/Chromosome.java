
public class Chromosome 
{
	public int shipType_;
	public int shipSize_;
	public int direction_;
	public int row_;
	public int col_;
	
	public Chromosome()
	{
	}
	
	public Chromosome(String c)
	{
		int index = 0;
		
		c = c.replaceAll("\\s+","");
		
		index = c.indexOf(',');
		
		row_ = Integer.valueOf(c.substring(0,index));
		
		c = c.substring(index +1);
		
		col_ = Integer.valueOf(c.substring(0, index));
		
		c = c.substring(index +1);
		
		direction_ = Integer.valueOf(c.substring(0, index));

		c = c.substring(index +1);

		shipSize_ = Integer.valueOf(c.substring(0, index));

		c = c.substring(index +1);

		shipType_ = Integer.valueOf(c.substring(0, index));
	}
	
	public void mutate()
	{
		int which = (int)(Math.random()*3);
		
		if(which == 0)
		{
			row_ = row_ + ((int)(Math.random()*3)) - 1;
		}
		
		if(which == 1)
		{
			col_ = col_ + ((int)(Math.random()*3)) - 1;
		}
		
		if(which == 2)
		{
			direction_ = (int)(Math.random()*4);
		}
	}
	
	public String getChromosome()
	{
		String out = String.valueOf(row_);
		
		out += " , ";
		
		out += String.valueOf(col_);
		
		out += " , ";
		
		out += String.valueOf(direction_);

		out += " , ";

		out += String.valueOf(shipSize_);
		
		out += " , ";

		out += String.valueOf(shipType_);
		return out;
	}
}
