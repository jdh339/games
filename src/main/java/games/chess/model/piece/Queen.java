package games.chess.model.piece;

import games.chess.model.Square;

import java.util.ArrayList;
import java.util.Collections;

public class Queen extends Piece {
    public Queen(boolean isWhite, Square initialPosition) {
        super(isWhite, initialPosition);
    }

    @Override
    public Square[][] getCapableSquares() {
        Square[][] linear = getLinearSquares();
        Square[][] diagonal = getDiagonalSquares();
        ArrayList<Square[]> result = new ArrayList<>(linear.length + diagonal.length);
        Collections.addAll(result, linear);
        Collections.addAll(result, diagonal);
        return result.toArray(new Square[0][]);
    }

    @Override
    public String getName() {
        return "Queen";
    }

    @Override
    public String getAbbrevName() {
        return "Q";
    }
}
