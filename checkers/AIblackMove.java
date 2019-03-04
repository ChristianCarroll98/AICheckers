/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author phil
 */
public class AIblackMove {

    //This is the current state of the game
    CheckersGame currentGame;
    //This array contains the legal moves at this point in the game for black.
    CheckersMove legalMoves[];
	
	int iter;

    // The constructor.
    public AIblackMove(CheckersGame game, CheckersMove moves[]) {
        currentGame = game;
        legalMoves = moves;
		iter = 5;
    }

    // This is where your logic goes to make a move.
    public CheckersMove nextMove() {
		
		MovePair testPair = new MovePair(currentGame.boardData.getLegalMoves(CheckersData.BLACK)[0], 10);
		System.out.println(testPair.move.toString() + ", " + testPair.value);
		//returns best move based on minmax function
        return minmax(currentGame.boardData, iter);
    }
	
	CheckersMove minmax(CheckersData b, int iter){
		
		// create new board so we dont mess up the original
		CheckersData board = new CheckersData(b);
		
		//set best black move to default mve from 0,0 to 0,0 in case no moves exist
		CheckersMove bestBlackMove = new CheckersMove(0,0,0,0);
		
		// get all black moves
		CheckersMove blackMoves[] = board.getLegalMoves(CheckersData.BLACK);
		
		if(blackMoves != null && blackMoves.length > 0){
			
			//initialize best black move to first one in list
			bestBlackMove = blackMoves[0];
			
			//create variable to keep track of highest minimum
			int bestMinmax = Integer.MIN_VALUE;
			
			//iterate through all black moves
			for(int i = 0; i<blackMoves.length; i++){
				//reset board to initial state
				board = new CheckersData(b);
				
				//make the black move
				board.makeMove(blackMoves[i]);
				
				//if red has a move, make the best one. method is below.
				//otherwise, this move makes red lose so pick it.
				if(!makeBestRedMove(board))
					return blackMoves[i];

				//find new evaluated value of the board after red makes its move
				int minValue = evaluate(board);
				
				//recursion code
				if(iter > 0 && minValue > 0){
					//call minmax again with board after the current move, and iteration -1
					board.makeMove(minmax(board, iter - 1));
					
					//veriable that holds new value of board
					//(perhaps this is the state of the board at the end of the iteration?)
					int testVal = evaluate(board);
					
					//if the test value is larger than the best Minmax then set the stuff.
					if( testVal > bestMinmax ){
						bestMinmax = testVal;
						bestBlackMove = blackMoves[i];
					}
				}
				//base case, if minvalue of board is larger than minmax then set the stuff.
				else{
					if(minValue > bestMinmax){
						bestMinmax = minValue;
						bestBlackMove = blackMoves[i];
					}
				}
				
			}
		}//end if statement and black moves for loop
		return bestBlackMove;
	}
	
	/**
	* Picks best red move and makes it, then returns true.
	* If no moves are available, return false.
	*/
	boolean makeBestRedMove(CheckersData board){
		
		//find all red moves
		CheckersMove redMoves[] = board.getLegalMoves(CheckersData.RED);
		
		//if no red moves exist, return false
		if(redMoves != null && redMoves.length > 0) {
		
			//otherwise set best move to first in list initially
			CheckersMove bestRedMove = redMoves[0];
			
			//create variable to hold minimum evaluated value
			int minValue = Integer.MAX_VALUE;
			
			//iterate through red moves
			for(int i = 0; i<redMoves.length; i++){
				//create copy of board
				CheckersData redTest = new CheckersData(board);
				
				//make the red move
				redTest.makeMove(redMoves[i]);
				
				//evaluate board and if the value is lower, set new best move and min value.
				int testVal = evaluate(redTest);
				if( testVal < minValue ){
					minValue = testVal;
					bestRedMove = redMoves[i];
				}
			}
			//make the best red move and return true
			board.makeMove(bestRedMove);
			return true;
		}
		//if no red moves exist, return false
		return false;
	}
	
    // One thing you will probably want to do is evaluate the current
    // goodness of the board.  This is a toy example, and probably isn't
    // very good, but you can tweak it in any way you want.  Not only is
    // number of pieces important, but board position could also be important.
    // Also, are kings more valuable than regular pieces?  How much?
    int evaluate(CheckersData board) {
		//did not change this much. yet.
        return board.numBlack()+ 2*board.numBlackKing()
		
                - board.numRed() - 2*board.numRedKing();
    }
}
/*
numRed and such

evaluate

jump

piece at

* make minmax method

get legal jumps from

be sure to call getLegalMoves each time a move is made or simulated

*/