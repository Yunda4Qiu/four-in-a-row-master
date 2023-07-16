package NRow.Players;

import NRow.Board;
import NRow.Heuristics.Heuristic;
import NRow.Game;


public class AlphaBetaPlayer extends MinMaxPlayer {
    private int depth;

    public AlphaBetaPlayer(int playerId, int gameN, int depth, Heuristic heuristic) {
        super(playerId, gameN, depth, heuristic);
    }

    @Override
    public int makeMove(Board board) {
        int bestMove = -1;
        int bestScore = Integer.MIN_VALUE;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        for (int action = 0; action < board.width; action++) {
            if (board.isValid(action)) {
                Board newBoard = board.getNewBoard(action, playerId);
                int score = alphaBeta(newBoard, depth - 1, alpha, beta, false);

                if (score > bestScore) {
                    bestScore = score;
                    bestMove = action;
                }
                alpha = Math.max(alpha, bestScore);
            }
        }

        return bestMove;
    }

    private int alphaBeta(Board board, int depth, int alpha, int beta, boolean isMaximizingPlayer) {
        if (depth == 0 || isTerminalNode(board)) {
            return heuristic.evaluateBoard(playerId, board);
        }

        if (isMaximizingPlayer) {
            int bestScore = Integer.MIN_VALUE;
            for (int action = 0; action < board.width; action++) {
                if (board.isValid(action)) {
                    Board newBoard = board.getNewBoard(action, playerId);
                    int score = alphaBeta(newBoard, depth - 1, alpha, beta, false);
                    bestScore = Math.max(bestScore, score);
                    alpha = Math.max(alpha, bestScore);
                    if (beta <= alpha) {
                        break; // Beta cutoff
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int action = 0; action < board.width; action++) {
                if (board.isValid(action)) {
                    Board newBoard = board.getNewBoard(action, 3 - playerId); // The opponent's player ID is 3 - playerId
                    int score = alphaBeta(newBoard, depth - 1, alpha, beta, true);
                    bestScore = Math.min(bestScore, score);
                    beta = Math.min(beta, bestScore);
                    if (beta <= alpha) {
                        break; // Alpha cutoff
                    }
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
