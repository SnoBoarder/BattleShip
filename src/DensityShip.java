/**
 * Represents the "ship" that we're placing in every possible situation.
 *
 */
public class DensityShip
{
	private DensityPeg _shipPegs[];
	private int _shipLength;
	
	public DensityShip(int shipLength)
	{
		_shipLength = shipLength;
		_shipPegs = new DensityPeg[_shipLength];
		for (int i = 0; i < _shipLength; ++i)
		{
			_shipPegs[i] = new DensityPeg();
		}
	}
	
	/**
	 * Set ship to next row. Also set whether the ship should be horizontal or vertical starting at the current row and starting at the 0 column.
	 * 
	 * @param row the row that we want to start at
	 * @param horizontally defines the position of the board
	 * @return
	 */
	public boolean setToRow(int row, boolean horizontally)
	{
		DensityPeg peg;
		for (int i = 0; i < _shipLength; ++i)
		{
			peg = _shipPegs[i];
			if (horizontally)
			{
				peg.row = row;
				peg.col = i;
			}
			else
			{
				peg.row = row + i;
				peg.col = 0;
			}
		}
		
		// TODO: Return if the ship can correctly be set here. if not, program should skip the entire row
		return true;
	}
	
	/**
	 * Move the entire ship right.
	 */
	public void moveRight()
	{
		for (int i = 0; i < _shipLength; ++i)
		{
			_shipPegs[i].col++;
		}
	}
	
	/**
	 * Update the density board with the current ship state only if it's valid.
	 * 
	 * @param playersMoveBoard reference to the player's moves
	 * @param densityBoard reference to the density board
	 */
	public void updateDensityBoard(char playersMoveBoard[][], int densityBoard[][])
	{
		if (!validPlacement(playersMoveBoard))
			return;
		
		// update density board with this ship layout
		DensityPeg peg;
		for (int i = 0; i < _shipLength; ++i)
		{
			peg = _shipPegs[i];
			
			// only update the density board of this peg if it's empty in the player moves list
			if (playersMoveBoard[peg.row][peg.col] == BSGame.PEG_EMPTY)
				densityBoard[peg.row][peg.col]++;
		}
	}
	
	/**
	 * Check if the ship's current placement is valid.
	 * 
	 * Validity is based off the following criteria:
	 * - Stays within the bounds
	 * - Is PEG_EMPTY
	 * 
	 * @param playersMoveBoard reference to the player's moves
	 * @return true if it's a valid placement. otherwise false.
	 */
	private boolean validPlacement(char playersMoveBoard[][])
	{
		DensityPeg peg;
		for (int i = 0; i < _shipLength; ++i)
		{
			peg = _shipPegs[i];
			
			if (peg.row < 0 || peg.row >= DensityBoard.NUM_ROWS)
				return false;
			
			if (peg.col < 0 || peg.col >= DensityBoard.NUM_COLS)
				return false;
			
			// if the peg was a miss, then invalid placement
			if (playersMoveBoard[peg.row][peg.col] == BSGame.PEG_MISS)
				return false;
		}
		
		return true;
	}
}
