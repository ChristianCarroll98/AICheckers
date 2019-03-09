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

        return minmax(currentGame.boardData, null, 7, Integer.MIN_VALUE, Integer.MAX_VALUE, true).move;
    }
	
	MovePair minmax(CheckersData b, CheckersMove initialMove, int depth, int alpha, int beta, boolean maximizing) {

		if (!((depth == 0) || (b.getLegalMoves(CheckersData.BLACK) == null) || (b.getLegalMoves(CheckersData.RED) == null))){

			if (maximizing) {

				MovePair maxEval = new MovePair(null, Integer.MIN_VALUE);

                CheckersMove[] blackMoves = b.getLegalMoves(CheckersData.BLACK);

				for (CheckersMove blackMove : blackMoves) {

					CheckersData testBoard = new CheckersData(b);
					testBoard.makeMove(blackMove);

					MovePair eval;

                    if (initialMove == null) {
                        eval = minmax(testBoard, blackMove, depth - 1, alpha, beta, false);
                    }else {
                        eval = minmax(testBoard, initialMove, depth - 1, alpha, beta, false);
                    }

                    if(maxEval.value < eval.value) maxEval = eval;

					alpha = Math.max(alpha, eval.value);

					if (beta <= alpha)
						break;
				}
				return maxEval;
			} else {
				MovePair minEval = new MovePair(null, Integer.MAX_VALUE);

                CheckersMove[] redMoves = b.getLegalMoves(CheckersData.BLACK);

                for (CheckersMove redMove : redMoves) {

                    CheckersData testBoard = new CheckersData(b);
                    testBoard.makeMove(redMove);

                    MovePair eval = minmax(testBoard, initialMove, depth - 1, alpha, beta, false);

                    if(minEval.value > eval.value) minEval = eval;

                    beta = Math.min(beta, eval.value);

                    if (beta <= alpha)
                        break;
                }
                System.out.println(minEval.value);
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

        int eval = board.getNumPieces(CheckersData.BLACK) + 2*board.getNumPieces(CheckersData.BLACK_KING) - board.getNumPieces(CheckersData.RED) - 2*board.getNumPieces(CheckersData.RED_KING);

        for(int y = 0; y < 8; y++) {
            for (int x = Math.floorMod(y, 2); x < 8; x += 2) { //x starts at 0 when y is even and starts at 1 when y is odd

                int piece = board.pieceAt(x, y);

                if (piece == 0) break;

                if (board.getLegalJumpsFrom(piece, x, y) != null) {
                    int numJumps = board.getLegalJumpsFrom(piece, x, y).length;
                    if (piece == CheckersData.BLACK || piece == CheckersData.BLACK_KING)
                        eval += 20 * numJumps;
                    else
                        eval -= 20 * numJumps;
                }
            }
        }
        return eval;
    }
}