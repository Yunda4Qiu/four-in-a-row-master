package NRow.Players;

import NRow.Board;
import NRow.Heuristics.Heuristic;
import NRow.Game;

public class MinMaxPlayer extends PlayerController {
    private int depth;

    public MinMaxPlayer(int playerId, int gameN, int depth, Heuristic heuristic) {
        super(playerId, gameN, heuristic);
        this.depth = depth;
    }

    @Override
    public int makeMove(Board board) {
        int bestMove = -1;
        int bestScore = Integer.MIN_VALUE;

        for (int action = 0; action < board.width; action++) {
            if (board.isValid(action)) {
                Board newBoard = board.getNewBoard(action, playerId);
                int score = minMax(newBoard, depth - 1, false);

                if (score > bestScore) {
                    bestScore = score;
                    bestMove = action;
                }
            }
        }

        return bestMove;
    }

    private int minMax(Board board, int depth, boolean isMaximizingPlayer) {
        if (depth == 0 || isTerminalNode(board)) {
            return heuristic.evaluateBoard(playerId, board);
        }

        if (isMaximizingPlayer) {
            int bestScore = Integer.MIN_VALUE;
            for (int action = 0; action < board.width; action++) {
                if (board.isValid(action)) {
                    Board newBoard = board.getNewBoard(action, playerId);
                    int score = minMax(newBoard, depth - 1, false);
                    bestScore = Math.max(bestScore, score);
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int action = 0; action < board.width; action++) {
                if (board.isValid(action)) {
                    Board newBoard = board.getNewBoard(action, 3 - playerId); // The opponent's player ID is 3 - playerId
                    int score = minMax(newBoard, depth - 1, true);
                    bestScore = Math.min(bestScore, score);
                }
            }
            return bestScore;
        }
    }

    private boolean isTerminalNode(Board board) {
        int winner = Game.winning(board.getBoardState(), gameN);
        return winner != 0;
    }
}




//public class MinMaxPlayer extends PlayerController {
//    private int depth;
//
//    public MinMaxPlayer(int playerId, int gameN, int depth, Heuristic heuristic) {
//        super(playerId, gameN, heuristic);
//        this.depth = depth;
//        //You can add functionality which runs when the player is first created (before the game starts)
//    }
//
//    /**
//   * Implement this method yourself!
//   * @param board the current board
//   * @return column integer the player chose
//   */
//    @Override
//    public int makeMove(Board board) {
//        TODO: implement minmax player!
//        // HINT: use the 'depth' variable to determine how deep you should search
//
//        // Example:
//        int maxValue = Integer.MIN_VALUE;
//        int maxMove = 0;
//        for(int i = 0; i < board.width; i++) { //for each of the possible moves
//            if(board.isValid(i)) { //if the move is valid
//                Board newBoard = board.getNewBoard(i, playerId); // Get a new board resulting from that move
//                int value = heuristic.evaluateBoard(playerId, newBoard); //evaluate that new board to get a heuristic value from it
//                if(value > maxValue) {
//                    maxMove = i;
//                }
//            }
//        }
//        // This returns the same as:
//        heuristic.getBestAction(playerId, board); // Very useful helper function!
//
//
//        /*
//        This is obviously not enough (this is depth 1)
//        Your assignment is to create a data structure (tree) to store the gameboards such that you can evaluate a higher depths.
//        Then, use the minmax algorithm to search through this tree to find the best move/action to take!
//        */
//
//        return maxMove;
//    }
//
//}
