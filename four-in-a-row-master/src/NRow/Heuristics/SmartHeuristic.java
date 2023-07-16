package NRow.Heuristics;

import NRow.Board;
import NRow.Game;

public class SmartHeuristic extends Heuristic {

    private boolean isFirstMove = true;

    public SmartHeuristic(int gameN) {
        super(gameN);
    }

    @Override
    protected String name() {
        return "Smart";
    }

    /**
     * Determine utility of a board state using a smart heuristic evaluation
     */
    @Override
    protected int evaluate(int player, Board board) {
        int[][] boardState = board.getBoardState();
        int winning = Game.winning(boardState, this.gameN);
        if (winning == player) {
            return Integer.MAX_VALUE;
        } else if (winning != 0) {
            return Integer.MIN_VALUE;
        }

        if (isFirstMove) {
            isFirstMove = false;
            int middleCol = boardState[0].length / 2;
            if (board.isValid(middleCol)) {
                return Integer.MAX_VALUE; // Play the first move at the middle of the width
            }
        }

        int opponent = 3 - player;
        int playerScore = 0;
        int opponentScore = 0;

        // Evaluate the board state for the current player
        playerScore = evaluatePlayer(player, boardState);

        // Evaluate the board state for the opponent player
        opponentScore = evaluatePlayer(opponent, boardState);

        // Return the difference between player and opponent scores
        return playerScore - opponentScore;
    }

    private int evaluatePlayer(int player, int[][] boardState) {
        int playerScore = 0;

        // Check rows for potential wins or blocks
        for (int i = 0; i < boardState.length; i++) {
            playerScore += evaluateLine(player, boardState[i]);
        }

        // Check columns for potential wins or blocks
        for (int j = 0; j < boardState[0].length; j++) {
            int[] column = new int[boardState.length];
            for (int i = 0; i < boardState.length; i++) {
                column[i] = boardState[i][j];
            }
            playerScore += evaluateLine(player, column);
        }

        // Check diagonals for potential wins or blocks
        int diagonalLength = Math.min(boardState.length, boardState[0].length);
        for (int d = 0; d < diagonalLength; d++) {
            int[] diagonal1 = new int[boardState.length - d];
            int[] diagonal2 = new int[boardState.length - d];
            for (int i = 0; i < boardState.length - d; i++) {
                int col_pos = i + d;
                int col_nag = boardState[0].length - 1 - i - d;
                if (col_pos < boardState[0].length && col_nag > -1) {
                    diagonal1[i] = boardState[i][i + d];
                    diagonal2[i] = boardState[i][boardState[0].length - 1 - i - d];
                }
            }
            playerScore += evaluateLine(player, diagonal1);
            playerScore += evaluateLine(player, diagonal2);
        }

        return playerScore;
    }

    private int evaluateLine(int player, int[] line) {
        int length = line.length;
        int emptySpaces = 0;
        int playerPieces = 0;
        int opponentPieces = 0;
        int playerScore = 0;

        for (int i = 0; i < length; i++) {
            if (line[i] == 0) {
                emptySpaces++;
            } else if (line[i] == player) {
                playerPieces++;
                playerScore += playerPieces * playerPieces;
                opponentPieces = 0; // Reset opponent count if blocked by player
            } else {
                opponentPieces++;
                playerScore -= opponentPieces * opponentPieces;
                playerPieces = 0; // Reset player count if blocked by opponent
            }
        }

        // Encourage placing near the center of the board
        int center = length / 2;
        int centerWeight = 3; // Adjust the center weight as needed
        playerScore += centerWeight * emptySpaces * (length - Math.abs(center - (length - 1) / 2));

        return playerScore;
    }
}
