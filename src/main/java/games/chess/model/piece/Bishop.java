package games.chess.model.piece;

import games.chess.model.Square;

public class Bishop extends Piece {
    public Bishop(boolean isWhite, Square initialPosition) {
        super(isWhite, initialPosition);
    }

    @Override
    public Square[][] getCapableSquares() {
        return getDiagonalSquares();
    }

    @Override
    public String getName() {
        return "Bishop";
    }

    @Override
    public String getAbbrevName() {
        return "B";
    }
}
