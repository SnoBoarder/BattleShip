// This player is as rudimentary as it gets.  It simply puts the ships in a static 
// location, and makes random moves until one sticks.  Your player can use this 
// as a base to expand upon. It is a good idea to play against this player until yours
// gets good enough to beat it regularly.

/**
 * @author Brian, Manu, and Nik
 */
public class Player1 extends Player {

	private DensityBoard _tracker;
	
	private ShipPlacement _placement;
	
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
		
		DensityPeg peg = _tracker.getMove();
		
		// make move based on density algorithm
		game.makeMove(hisShips, myMoves, peg.row + 1, peg.col + 1); // add 1 to be base 1...
		
		// make move based on completely random
		//while(!game.makeMove(hisShips, myMoves, randomRow(), randomCol()));

		numMoves++;
		System.out.println("Player " + myPlayerNum + " num Moves = " + numMoves);
		
		//_tracker.dump();
		//_tracker.playerMoveDump();
	}

	public boolean addShips() {
		
		_placement = new ShipPlacement(10,10);
		
		System.out.println("Getting new board");
		Gene gene = _placement.getBoard();
		
		System.out.println("Placing Ships");
		
			game.putShip(myShips, Ships.SHIP_CARRIER,gene.gene_.get(4).row_ + 1, gene.gene_.get(4).col_ + 1, gene.gene_.get(4).direction_);
			game.putShip(myShips, Ships.SHIP_BATTLESHIP, gene.gene_.get(3).row_ + 1, gene.gene_.get(3).col_ + 1, gene.gene_.get(3).direction_);
			game.putShip(myShips, Ships.SHIP_CRUISER,gene.gene_.get(2).row_ + 1, gene.gene_.get(2).col_ + 1, gene.gene_.get(2).direction_);
			game.putShip(myShips, Ships.SHIP_DESTROYER,gene.gene_.get(1).row_ + 1, gene.gene_.get(1).col_ + 1, gene.gene_.get(1).direction_);
			game.putShip(myShips, Ships.SHIP_SUBMARINE,gene.gene_.get(0).row_ + 1, gene.gene_.get(0).col_ + 1, gene.gene_.get(0).direction_);
		
			/*
			gene = _placement.randomizeBoard();
			game.putShip(myShips, Ships.SHIP_CARRIER,gene.gene_.get(4).row_ + 1, gene.gene_.get(4).col_ + 1, gene.gene_.get(4).direction_);
			game.putShip(myShips, Ships.SHIP_BATTLESHIP, gene.gene_.get(3).row_ + 1, gene.gene_.get(3).col_ + 1, gene.gene_.get(3).direction_);
			game.putShip(myShips, Ships.SHIP_CRUISER,gene.gene_.get(2).row_ + 1, gene.gene_.get(2).col_ + 1, gene.gene_.get(2).direction_);
			game.putShip(myShips, Ships.SHIP_DESTROYER,gene.gene_.get(1).row_ + 1, gene.gene_.get(1).col_ + 1, gene.gene_.get(1).direction_);
			game.putShip(myShips, Ships.SHIP_SUBMARINE,gene.gene_.get(0).row_ + 1, gene.gene_.get(0).col_ + 1, gene.gene_.get(0).direction_);*/
			
//			game.putShip(myShips, Ships.SHIP_CARRIER,2, 2, Ships.SHIP_SOUTH);
//			game.putShip(myShips, Ships.SHIP_BATTLESHIP, 5, 5, Ships.SHIP_EAST);
//			game.putShip(myShips, Ships.SHIP_CRUISER, 6, 7, Ships.SHIP_EAST);
//			game.putShip(myShips, Ships.SHIP_DESTROYER, 8, 3, Ships.SHIP_EAST);
//			game.putShip(myShips, Ships.SHIP_SUBMARINE, 9, 9, Ships.SHIP_NORTH);
		
		return true;
	}
	
	public void gameOver() {
		System.out.println("Game Over");
		
		//check if we won
		Boolean lost = true;
		
		int enemyDead = 0;
		int usDead = 0;
		
		if(game.p1 == this)
		{			
			for(int x = 0; x < game.p1ShipsSunk.length; x ++)
			{
				if(game.p2ShipsSunk[x])
				{
					enemyDead ++;
				}
				
				if(game.p1ShipsSunk[x])
				{
					usDead ++;
				}
				
				lost = lost && game.p1ShipsSunk[x];
			}
			_placement.setWeight(numMoves, game.p1ShipsSunk.length - usDead);
		}
		
		if(game.p2 == this)
		{
			for(int x = 0; x < game.p2ShipsSunk.length; x ++)
			{
				if(game.p1ShipsSunk[x])
				{
					enemyDead ++;
				}
				
				if(game.p2ShipsSunk[x])
				{
					usDead ++;
				}
				
				lost = lost && game.p2ShipsSunk[x];
			}
			_placement.setWeight(numMoves, game.p2ShipsSunk.length - usDead);
		}
	}
}
