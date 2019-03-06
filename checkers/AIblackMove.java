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
    }

    // This is where your logic goes to make a move.
    public CheckersMove nextMove() {
		
		//MovePair testPair = new MovePair(currentGame.boardData.getLegalMoves(CheckersData.BLACK)[0], 10);
		//System.out.println(testPair.move.toString() + ", " + testPair.value);
		//returns best move based on minmax function

        return minmax(currentGame.boardData, null, 1000, Integer.MIN_VALUE, Integer.MAX_VALUE, true).move;
    }
	
	MovePair minmax(CheckersData b, CheckersMove initialMove, int depth, int alpha, int beta, boolean maximizing) {
		if (!((depth == 0) || (b.getLegalMoves(CheckersData.BLACK) == null) || (b.getLegalMoves(CheckersData.RED) == null))) {

			if (maximizing) {

				int maxEval = Integer.MIN_VALUE;

				for (CheckersMove move : b.getLegalMoves(CheckersData.BLACK)) {

					if (initialMove == null)
						initialMove = move;

					CheckersData testBoard = new CheckersData(b);
					testBoard.makeMove(move);

					int eval = minmax(testBoard, initialMove, depth - 1, alpha, beta, false).value;

					maxEval = Math.max(maxEval, eval);
					alpha = Math.max(alpha, eval);

					if (beta <= alpha)
						break;

					return new MovePair(initialMove, maxEval);
				}
			} else {
				int minEval = Integer.MAX_VALUE;

				for (CheckersMove move : b.getLegalMoves(CheckersData.RED)) {

					CheckersData testBoard = new CheckersData(b);
					testBoard.makeMove(move);

					int eval = minmax(testBoard, initialMove, depth - 1, alpha, beta, true).value;

					minEval = Math.min(minEval, eval);
					beta = Math.min(beta, eval);

					if (beta <= alpha)
						break;
					return new MovePair(initialMove, minEval);
				}
			}
		}
		System.out.println("depth: " + depth);
		return new MovePair(initialMove, evaluate(b));
	}
	
    // One thing you will probably want to do is evaluate the current
    // goodness of the board.  This is a toy example, and probably isn't
    // very good, but you can tweak it in any way you want.  Not only is
    // number of pieces important, but board position could also be important.
    // Also, are kings more valuable than regular pieces?  How much?
    int evaluate(CheckersData board) {
		
		//added boardValue static array to CheckersData constructor
		//eval += (value of piece at (0,0) * board.boardValues[0][0];)

		int eval = 0;

		for(int y = 0; y < 8; y++){
			for(int x = Math.floorMod(y,2); x < 8; x+=2){ //x starts at 0 when y is even and starts at 1 when y is odd

				int piece = board.pieceAt(x,y);
				int pieceVal = 0;
				switch(piece){
					case CheckersData.BLACK:
						pieceVal = 1;
						break;
					case CheckersData.BLACK_KING:
						pieceVal = 2;
						break;
					case CheckersData.RED:
						pieceVal = -1;
						break;
					case CheckersData.RED_KING:
						pieceVal = -2;
						break;
				}

				eval += (CheckersData.boardValues[0][0] * pieceVal); //multiply board value by piece value.
			}
		}

        return eval;
		//return board.getNumPieces(CheckersData.BLACK) + 2*board.getNumPieces(CheckersData.BLACK_KING) -
		//		board.getNumPieces(CheckersData.RED) - 2*board.getNumPieces(CheckersData.RED_KING);
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