package games.chess.model.piece;

import games.chess.model.Square;

public class Rook extends Piece {
    public Rook(boolean isWhite, Square initialPosition) {
        super(isWhite, initialPosition);
    }

    @Override
    public Square[][] getCapableSquares() {
        return this.getLinearSquares();
    }

    @Override
    public String getName() {
        return "Rook";
    }

    @Override
    public String getAbbrevName() {
        return "R";
    }
}
