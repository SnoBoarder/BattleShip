import java.util.Comparator;

/**
 * The peg.
 */
public class DensityPeg implements Comparator<DensityPeg>
{
	public int row;
	public int col;
	
	// used to find a move
	public int densityValue;
	
	public DensityPeg()
	{
		
	}
	
	// sort from largest to smallest density value
	public int compare(DensityPeg peg1, DensityPeg peg2)
	{
		return peg2.densityValue - peg1.densityValue;
	}
}
