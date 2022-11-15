package games.chess.model.piece;

import games.chess.model.Square;

public class Pawn extends Piece {
    public Pawn(boolean isWhite, Square initialPosition) {
        super(isWhite, initialPosition);
    }

    @Override
    public Square[][] getCapableSquares() {
        int rankModifier = this.isWhite() ? 1 : -1;

        Square curr = this.getSquare();
        Square[] squares;
        if (hasMoved()) {
            squares = new Square[1];
        } else {
            squares = new Square[2];
            squares[1] = new Square(curr.getFileIndex(), curr.getRankIndex() + (2 * rankModifier));
        }
        squares[0] = new Square(curr.getFileIndex(), curr.getRankIndex() + rankModifier);

        return new Square[][]{squares};
    }

    @Override
    public String getName() {
        return "Pawn";
    }

    @Override
    public String getAbbrevName() {
        return "";
    }
}
