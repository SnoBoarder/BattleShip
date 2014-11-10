import java.io.Console;

// This player is as rudimentary as it gets.  It simply puts the ships in a static 
// location, and makes random moves until one sticks.  Your player can use this 
// as a base to expand upon. It is a good idea to play against this player until yours
// gets good enough to beat it regularly.

public class Player1 extends Player {

	private DensityBoard _tracker;
	
	private boolean _firstMove = true;
	
	// You must call the super to establish the necessary game variables
	// and register the game.
	public Player1(int playerNum) {
		super(playerNum);
	}
	
	private void setupDensityBoard()
	{
		boolean opponentsShipSunk[];
		int playersMovesId;
		
		if (myPlayerNum == BSGame.GAME_PLAYER1)
		{
			opponentsShipSunk = game.p2ShipsSunk;
			playersMovesId = BSGame.BOARD_P1MOVES;
		}
		else
		{
			opponentsShipSunk = game.p1ShipsSunk;
			playersMovesId = BSGame.BOARD_P2MOVES;
		}
		
		_tracker = new DensityBoard(game, this, opponentsShipSunk, playersMovesId);
	}

	@Override
	public void makeMove()
	{
		if (_firstMove)
		{
			_firstMove = false;
			setupDensityBoard();
		}
		
		_tracker.update();
		_tracker.dump();
		
		DensityPeg peg = _tracker.getMove();
		
		// Try making a move until successful
		game.makeMove(hisShips, myMoves, peg.row + 1, peg.col + 1); // add 1 to be base 1...
		
		//while(!game.makeMove(hisShips, myMoves, randomRow(), randomCol()));

		numMoves++;
		System.out.println("Player " + myPlayerNum + " num Moves = " + numMoves);
		
		_tracker.playerMoveDump();
	}

	public boolean addShips() {
		
		game.putShip(myShips, Ships.SHIP_CARRIER, 2, 2, Ships.SHIP_SOUTH);
		game.putShip(myShips, Ships.SHIP_BATTLESHIP, 5, 5, Ships.SHIP_EAST);
		game.putShip(myShips, Ships.SHIP_CRUISER, 6, 7, Ships.SHIP_EAST);
		game.putShip(myShips, Ships.SHIP_DESTROYER, 8, 3, Ships.SHIP_EAST);
		game.putShip(myShips, Ships.SHIP_SUBMARINE, 9, 9, Ships.SHIP_NORTH);

		return true;
	}
}

// implements Probability Density Functions Algorithm

/**
 * Implements the Probability Density Algorithm.
 * Takes every ship and tries to place it in every possible situation.
 * If it's a valid location, increment the value of the location.
 * The density board should be clearer the more moves occur.
 * 
 */
class DensityBoard
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

/**
 * Represents the "ship" that we're placing in every possible situation.
 *
 */
class DensityShip
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
			
			switch (playersMoveBoard[peg.row][peg.col])
			{
				case BSGame.PEG_HIT:
				case BSGame.PEG_MISS:
					return false;
			}
		}
		
		return true;
	}
}

/**
 * The peg.
 */
class DensityPeg
{
	public int row;
	public int col;
	
	public DensityPeg()
	{
		
	}
}
