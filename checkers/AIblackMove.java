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

        return minmax(System.currentTimeMillis(), currentGame.boardData, null, Integer.MIN_VALUE, Integer.MAX_VALUE, true).move;
    }
	
	MovePair minmax(long curTime, CheckersData b, CheckersMove initialMove, int alpha, int beta, boolean maximizing) {
        System.out.println(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
		if (!((System.currentTimeMillis() - curTime > 3000) || (b.getLegalMoves(CheckersData.BLACK) == null) || (b.getLegalMoves(CheckersData.RED) == null))){

			if (maximizing) {
				MovePair maxEval = new MovePair(null, Integer.MIN_VALUE);

				for (CheckersMove blackMove : b.getLegalMoves(CheckersData.BLACK)) {

					CheckersData testBoard = new CheckersData(b);
					testBoard.makeMove(blackMove);

					MovePair eval;

                    if (initialMove == null) {
                        eval = minmax(curTime, testBoard, blackMove, alpha, beta, false);
                    }else {
                        eval = minmax(curTime, testBoard, initialMove, alpha, beta, false);
                    }

                    if(maxEval.value < eval.value) maxEval = eval;

					alpha = Math.max(alpha, eval.value);

					if (beta <= alpha) break;
				}
				return maxEval;
			} else {
				MovePair minEval = new MovePair(null, Integer.MAX_VALUE);

                for (CheckersMove redMove : b.getLegalMoves(CheckersData.RED)) {

                    CheckersData testBoard = new CheckersData(b);
                    testBoard.makeMove(redMove);

                    MovePair eval = minmax(curTime, testBoard, initialMove, alpha, beta, false);

                    if(minEval.value > eval.value) minEval = eval;

                    beta = Math.min(beta, eval.value);

                    if (beta <= alpha) break;
                }
                return minEval;
			}
		}

		return new MovePair(initialMove, evaluate(b));
	}

    // One thing you will probably want to do is evaluate the current
    // goodness of the board.  This is a toy example, and probably isn't
    // very good, but you can tweak it in any way you want.  Not only is
    // number of pieces important, but board position could also be important.
    // Also, are kings more valuable than regular pieces?  How much?
    int evaluate(CheckersData board) {

        int eval = board.numBlack() + board.numBlackKing() -
                board.numRed() - board.numRedKing();
        //System.out.println(eval);
        for(int y = 0; y < 8; y++) {
            for (int x = Math.floorMod(y, 2); x < 8; x += 2) { //x starts at 0 when y is even and starts at 1 when y is odd
                int piece = board.pieceAt(x, y);

                if (piece == 0) break;

                if (board.getLegalJumpsFrom(piece, x, y) != null) {
                    int numJumps = board.getLegalJumpsFrom(piece, x, y).length;
                    if (piece == CheckersData.BLACK || piece == CheckersData.BLACK_KING)
                        eval += numJumps;
                    else
                        eval -= numJumps;
                }
            }
        }
        return eval;
    }
}