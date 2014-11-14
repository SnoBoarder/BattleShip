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
		//_tracker.dump();
		
		DensityPeg peg = _tracker.getMove();
		
		// make move based on density algorithm
		game.makeMove(hisShips, myMoves, peg.row + 1, peg.col + 1); // add 1 to be base 1...
		
		// make move based on completely random
		//while(!game.makeMove(hisShips, myMoves, randomRow(), randomCol()));

		numMoves++;
		System.out.println("Player " + myPlayerNum + " num Moves = " + numMoves);
		
		//_tracker.playerMoveDump();
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
