/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author phil
 */
public class AIblackMove{

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
        return minmax(System.currentTimeMillis(), 300, currentGame.boardData, null, Integer.MIN_VALUE, Integer.MAX_VALUE, true).move;
    }
	
	MovePair minmax(long startTime, int depth, CheckersData initialBoard, CheckersMove initialMove, int alpha, int beta, boolean maximizing) {

		if (!(((System.currentTimeMillis() - startTime) > 6000) || (depth == 0) ||
                (initialBoard.getLegalMoves(CheckersData.BLACK) == null) || (initialBoard.getLegalMoves(CheckersData.RED) == null))){

			if (maximizing) {
				MovePair maxEval = new MovePair(initialMove, Integer.MIN_VALUE);

				for (CheckersMove blackMove : initialBoard.getLegalMoves(CheckersData.BLACK)) {

					CheckersData testBoard = new CheckersData(initialBoard);
					testBoard.makeMove(blackMove);

					MovePair eval;

					if(testBoard.getLegalJumpsFrom(testBoard.pieceAt(blackMove.toRow, blackMove.toCol),
                            blackMove.toRow, blackMove.toCol) != null)
                        maximizing = !maximizing;

                    if (initialMove == null) {
                        eval = minmax(startTime, depth - 1, testBoard, blackMove, alpha, beta, !maximizing);
                    }else {
                        eval = minmax(startTime, depth - 1, testBoard, initialMove, alpha, beta, !maximizing);
                    }

                    if(maxEval.value <= eval.value) maxEval = eval;

					alpha = Math.max(alpha, eval.value);

					if (beta <= alpha) break;
				}

				return maxEval;
			} else {
				MovePair minEval = new MovePair(initialMove, Integer.MAX_VALUE);

                for (CheckersMove redMove : initialBoard.getLegalMoves(CheckersData.RED)) {

                    CheckersData testBoard = new CheckersData(initialBoard);
                    testBoard.makeMove(redMove);

                    if(testBoard.getLegalJumpsFrom(testBoard.pieceAt(redMove.toRow, redMove.toCol),
                            redMove.toRow, redMove.toCol) != null)
                        maximizing = !maximizing;

                    MovePair eval = minmax(startTime, depth - 1, testBoard, initialMove, alpha, beta, !maximizing);

                    if(minEval.value >= eval.value) minEval = eval;

                    beta = Math.min(beta, eval.value);

                    if (beta <= alpha) break;
                }
                return minEval;
			}
		}
		return new MovePair(initialMove, evaluate(initialBoard));
	}

    int evaluate(CheckersData board) {

        if(board.getLegalMoves(CheckersData.RED) == null)
            return Integer.MAX_VALUE;

        if(board.getLegalMoves(CheckersData.BLACK) == null)
            return Integer.MIN_VALUE;
/*
        int eval = board.numBlack() + 2*board.numBlackKing() -
                board.numRed() - 2*board.numRedKing();

        for(int y = 0; y < 8; y++) {
            for (int x = Math.floorMod(y, 2); x < 8; x += 2) { //x starts at 0 when y is even and starts at 1 when y is odd

                int piece = board.pieceAt(x, y);

                if (piece == CheckersData.EMPTY) break;

//                if(board.numBlack() + board.numBlackKing() + board.numRed() + board.numRedKing() == 24) { // set up initial moves
//                    switch (piece) {
//                        case CheckersData.BLACK:
//                            eval += CheckersData.blackBoardValues[x][y];
//                            break;
//                        case CheckersData.BLACK_KING:
//                            eval += 2 * CheckersData.blackBoardValues[x][y];
//                            break;
//                        case CheckersData.RED:
//                            eval -= CheckersData.redBoardValues[x][y];
//                            break;
//                        case CheckersData.RED_KING:
//                            eval -= 2 * CheckersData.redBoardValues[x][y];
//                            break;
//                    }
//                }else
                if (board.getLegalJumpsFrom(piece, x, y) != null) {
                    if ((board.numBlack() + board.numBlackKing() - board.numRed() - board.numRedKing()) > 0) {
                        int numJumps = board.getLegalJumpsFrom(piece, x, y).length;
                        if (piece == CheckersData.BLACK || piece == CheckersData.BLACK_KING)
                            eval += 2*numJumps;
                        else
                            eval -= numJumps;
                    } else {
                        int numJumps = board.getLegalJumpsFrom(piece, x, y).length;
                        if (piece == CheckersData.BLACK || piece == CheckersData.BLACK_KING)
                            eval += numJumps;
                        else
                            eval -= 2*numJumps;
                    }
                }
            }
        }
        return eval;
        */return board.numBlack() + 2*board.numBlackKing() -
                board.numRed() - 2*board.numRedKing();
    }
}

class MovePair{

    public CheckersMove move;
    public int value;

    public MovePair(CheckersMove mv, int val){
        move = mv;
        value = val;
    }
}