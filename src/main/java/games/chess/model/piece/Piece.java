package games.chess.model.piece;

import games.chess.model.Move;
import games.chess.model.Square;

import java.util.ArrayList;

public abstract class Piece {

    private final boolean isWhite;
    private Square square;

    public Piece(boolean isWhite, Square initialPosition) {
        this.isWhite = isWhite;
        this.square = initialPosition;
    }

    public static boolean sameColor(Piece p1, Piece p2) {
        return p1.isWhite == p2.isWhite;
    }

    public static boolean differentColors(Piece p1, Piece p2) {
        return p1.isWhite != p2.isWhite;
    }

    /**
     * Returns the squares this piece can move to based on its current square.
     * The results are given in a series of lists, each list sorted on distance
     * from the origin square.
     * <p>
     * This means that iterating through a list of Squares, the first occupied Square
     * should stop the piece's movement.
     *
     * @return 2-dimensional array.
     */
    public abstract Square[][] getCapableSquares();

    public abstract String getName();

    public abstract String getAbbrevName();

    public boolean isWhite() {
        return isWhite;
    }

    public Square getSquare() {
        return this.square;
    }

    public boolean isInPlay() {
        return square != null && square.isOnBoard();
    }

    public void makeMove(Move move) {
        square = move.getDestSquare();
    }

    public void removeFromPlay() {
        this.square = null;
    }

    /**
     * Helper function for Rooks and Queens.
     * @return a 2d array of Squares as needed by Rook.getCapableSquares().
     */
    Square[][] getLinearSquares() {
        int[] fileModifiers = new int[] {0, 1, 0, -1};
        int[] rankModifiers = new int[] {1, 0, -1, 0};
        return this.getSquaresUsingModifiers(fileModifiers, rankModifiers);
    }

    /**
     * Helper function for Bishops and Queens.
     * @return a 2d array of Squares as needed by Bishop.getCapableSquares().
     */
    Square[][] getDiagonalSquares() {
        int[] fileModifiers = new int[] {1, 1, -1, -1};
        int[] rankModifiers = new int[] {1, -1, -1, 1};
        return getSquaresUsingModifiers(fileModifiers, rankModifiers);
    }

    // Iterates over the board, adding the modifiers to a Square rank and file.
    // This saves duplicate code from `getLinearSquares()` and `getDiagonalSquares()`.
    // This function is tested via dependent calls in RookTest, BishopTest, and QueenTest.
    private Square[][] getSquaresUsingModifiers(int[] fileModifiers, int[] rankModifiers) {
        int numModifiers = fileModifiers.length;
        Square[][] result = new Square[numModifiers][];
        for (int i = 0; i < numModifiers; i++) {
            Square candidate = this.getSquare();
            ArrayList<Square> list = new ArrayList<>(8);
            while (candidate.isOnBoard()) {
                list.add(candidate);
                candidate = new Square(
                        candidate.getFileIndex() + fileModifiers[i],
                        candidate.getRankIndex() + rankModifiers[i]
                );
            }
            list.remove(0);  // first element in each list is `this`.
            result[i] = list.toArray(new Square[0]);
        }
        return result;
    }
}