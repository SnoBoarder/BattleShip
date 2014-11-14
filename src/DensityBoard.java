/**
 * Implements the Probability Density Algorithm.
 * Takes every ship and tries to place it in every possible situation.
 * If it's a valid location, increment the value of the location.
 * The density board should be clearer the more moves occur.
 * 
 */
public class DensityBoard
{
	public static final int NUM_ROWS = 10;
	public static final int NUM_COLS = NUM_ROWS;
	
	// game content
	private BSGame _game;
	private Player _self;
	private boolean _opponentsShipsSunk[];
	private int _playersMovesId;
	
	private char _playersMoveBoard[][];
	private int _densityBoard[][];
	
	private DensityPeg _movePeg;
	
	public DensityBoard(BSGame game, Player self, boolean opponentsShipsSunk[], int playersMovesId)
	{
		_game = game;
		_self = self;
		_opponentsShipsSunk = opponentsShipsSunk;
		_playersMovesId = playersMovesId;
		
		_playersMoveBoard = new char[NUM_ROWS][NUM_COLS];
		_densityBoard = new int[NUM_ROWS][NUM_COLS];
		
		_movePeg = new DensityPeg();
	}
	
	public DensityPeg getMove()
	{
		int bestDensityIndex = 0;
		int currentDensityIndex;
		
		int bestRow = -1;
		int bestCol = -1;
		
		// TODO: Implement prioritization list and take the highest priority that doesn't have a hint
		for (int row = 0; row < NUM_ROWS; ++row)
		{
			for (int col = 0; col < NUM_COLS; ++col)
			{
				currentDensityIndex = _densityBoard[row][col];
				if (bestDensityIndex < currentDensityIndex && _playersMoveBoard[row][col] == BSGame.PEG_EMPTY)
				{
					bestDensityIndex = currentDensityIndex;
					bestRow = row;
					bestCol = col;
				}
			}
		}
		
		_movePeg.row = bestRow;
		_movePeg.col = bestCol;
		
		return _movePeg;
	}
	
	/**
	 * Dump the density board.
	 */
	public void dump()
	{
		String retval = "\n";
		for (int row = 0; row < NUM_ROWS; ++row)
		{
			for (int col = 0; col < NUM_COLS; ++col)
			{
				retval += " ";
				retval += String.format("%02d", _densityBoard[row][col]);
				retval += " ";
				
				if (col + 1 == NUM_COLS)
				{ // last column, break line
					retval += "\n";
				}
			}
		}
		
		System.out.println(retval);
	}
	
	public void playerMoveDump()
	{
		String retval = "\n";
		for (int row = 0; row < NUM_ROWS; ++row)
		{
			for (int col = 0; col < NUM_COLS; ++col)
			{
				retval += "[";
				retval += _playersMoveBoard[row][col];
				retval += "]";
				
				if (col + 1 == NUM_COLS)
				{ // last column, break line
					retval += "\n";
				}
			}
		}
		
		System.out.println(retval);
	}
	
	/**
	 * Update the density boards values based on all previous moves done.
	 */
	public void update()
	{
		int row;
		int col;
		
		// traverse every index
		for (row = 0; row < NUM_ROWS; row++)
		{
			for (col = 0; col < NUM_COLS; col++)
			{
				// update our player board from the game's player board
				// NOTE: I wish we had direct access to our board, but we don't...
				_playersMoveBoard[row][col] = _game.getMoveBoardValue(_playersMovesId, row + 1, col + 1);
				
				// reset the density boards values to 0
				_densityBoard[row][col] = 0;
			}
		}
		
		DensityShip ship;
		// update the probability of ALL SHIPS based on the current player moves board
		int len = _opponentsShipsSunk.length;
		for (int i = 0; i < len; ++i)
		{
			if (_opponentsShipsSunk[i])
				continue; // ship already sunk, ignore.
			
			// create ship based off of ship length
			ship = new DensityShip(_self.getShipLength(i + 1));
			
			// traverse the board with a horizontal ship
			traverseBoard(ship, true);
			
			// traverse the board with a vertical ship
			traverseBoard(ship, false);
		}
	}
	
	// traverse every single scenario possible, check if the ship fits
	// if it fits, update the nodes that the ship is over.
	private void traverseBoard(DensityShip ship, boolean horizontalShip)
	{
		// start at 0 horizontally
		ship.setToRow(0, horizontalShip);
		for (int row = 0; row < NUM_ROWS; row++)
		{
			for (int col = 0; col < NUM_COLS; col++)
			{
				ship.updateDensityBoard(_playersMoveBoard, _densityBoard);
				ship.moveRight();
			}
			
			if (row + 1 < NUM_ROWS)
				ship.setToRow(row + 1, horizontalShip);
		}
	}	
}