package games.connect4;

import games.common.RegularGame;
import games.common.RegularMove;

import java.util.Arrays;

public class Game implements RegularGame {

    public final static int NUM_COLS = 7;
    public final static int COL_HEIGHT = 6;

    private final char[][] grid;
    private final int[] heights;
    private boolean redsTurn;
    private Move lastMove = null;

    public Game() {
        grid = new char[NUM_COLS][COL_HEIGHT];
        for (char[] col : grid) {
            Arrays.fill(col, '_');
        }
        heights = new int[NUM_COLS];
        Arrays.fill(heights, 0);
        redsTurn = true;
    }

    private Game(Game toCopy) {
        grid = new char[NUM_COLS][];
        for (int i = 0; i < grid.length; i++) {
            grid[i] = Arrays.copyOf(toCopy.grid[i], toCopy.grid[i].length);
        }
        heights = Arrays.copyOf(toCopy.heights, NUM_COLS);
        redsTurn = toCopy.redsTurn;
    }

    public char getCharAt(int colId, int rowId) {
        return grid[colId][rowId];
    }

    public String getPlayerToMove() {
        return (redsTurn ? "Red" : "Blue");
    }

    public void makeMove(Move move) {
        char c = move.red() ? 'R' : 'B';
        int height = heights[move.column()]++;
        grid[move.column()][height] = c;
        redsTurn = !redsTurn;
        lastMove = move;
    }

    public String getCurrentPlayer() {
        return redsTurn ? "Red" : "Blue";
    }

    public String getLastPlayer() {
        return redsTurn ? "Blue" : "Red";
    }

    @Override
    public boolean isFinished() {
        return getWinner() != null;
    }

    @Override
    public String getWinner() {
        // Check for connect 4s:
        // Check columns, rows, and diagonals.
        char result;
        for (char[] col: getCols()) {
            result = get4Connected(col);
            if (result == 'R' || result == 'B') {
                return result == 'R' ? "Red" : "Blue";
            }
        }
        for (char[] row: getRows()) {
            result = get4Connected(row);
            if (result == 'R' || result == 'B') {
                return result == 'R' ? "Red" : "Blue";
            }
        }

        for (char[] diagonal: getDiagonals()) {
            result = get4Connected(diagonal);
            if (result == 'R' || result == 'B') {
                return result == 'R' ? "Red" : "Blue";
            }
        }

        return null;
    }

//    public boolean didLastMoveWin() {
//        String lastPlayer = lastMove.red() ? "R" : "B";
//        return getResult().equalsIgnoreCase(lastPlayer);
//    }


    @Override
    public Move[] getLegalMoves() {
        int possible = 0;
        for (int h : heights) {
            if (h < COL_HEIGHT) {
                possible++;
            }
        }
        Move[] legalMoves = new Move[possible];

        int moveId = 0;
        for (int colId = 0; colId < NUM_COLS; colId++) {
            if (heights[colId] < COL_HEIGHT) {
                legalMoves[moveId++] = new Move(redsTurn, colId);
            }
        }
        return legalMoves;
    }

    @Override
    public Game cloneWithUpdate(RegularMove move) {
        if (!(move instanceof Move)) {
            return this;
        }

        Game clone = new Game(this);
        clone.makeMove((Move)move);
        return clone;
    }

    private char[][] getCols() {
        return this.grid;
    }

    private char[][] getRows() {
        char[][] rows = new char[COL_HEIGHT][];
        for (int rowID = 0; rowID < COL_HEIGHT; rowID++) {
            rows[rowID] = new char[NUM_COLS];
            for (int colId = 0; colId < NUM_COLS; colId++) {
                rows[rowID][colId] = grid[colId][rowID];
            }
        }
        return rows;
    }

    /**
     * Returns the 12 diagonals of length at least 4 in a Connect4 game.
     * 6 of them travel up and right, and 6 travel up and left.
     * The returned char arrays will all be of length 6, even though some
     * diagonals are only 4 or 5 spaces long. The extra chars are '_' at the
     * end of the arrays.
     * @return 12 arrays of length 6 each.
     */
    private char[][] getDiagonals() {
        // First, traverse up and right.
        char[][] result = new char[12][COL_HEIGHT];
        int diagonalID = 0;

        // Traverse right and up.
        for (int startCol = -2; startCol < 4; startCol++) {
            Arrays.fill(result[diagonalID], '_');
            for (int offset = 0; offset < COL_HEIGHT; offset++) {
                int col = startCol + offset;
                if (col >= 0 && col < NUM_COLS) {
                    result[diagonalID][offset] = grid[col][offset];
                }
            }
            diagonalID++;
        }

        // Traverse left and up.
        for (int startCol = 8; startCol >= 3; startCol--) {
            Arrays.fill(result[diagonalID], '_');
            for (int offset = 0; offset < COL_HEIGHT; offset++) {
                int col = startCol - offset;
                if (col >= 0 && col < NUM_COLS) {
                    result[diagonalID][offset] = grid[col][offset];
                }
            }
            diagonalID++;
        }
        return result;
    }

    /**
     * Traverses the given set of chars in a line to
     * see if there is a set of four consecutive 'R's or 'B's.
     *
     * @param line a list of chars (of length at least 4) in a line.
     * @return 'R' if red has 4 in row, 'B' if blue does, or '_' if neither.
     */
    private char get4Connected(char[] line) {
        char lastSeen = '_';
        int count = 0;
        for (char c : line) {
            if (c == '_') {
                lastSeen = '_';
                count = 0;
            } else if (c == lastSeen) {
                count++;
                if (count == 4) {
                    return lastSeen;
                }
            } else {
                lastSeen = c;
                count = 1;
            }
        }
        return '_';
    }
}
