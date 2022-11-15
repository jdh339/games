package games.chess.model.piece;

import games.chess.model.Square;

public class Knight extends Piece {
    public Knight(boolean isWhite, Square initialPosition) {
        super(isWhite, initialPosition);
    }

    @Override
    public Square[][] getCapableSquares() {
        Square[][] result = new Square[8][];
        int[] fileModifiers = new int[]{1, 2, 2, 1, -1, -2, -2, -1};
        int[] rankModifiers = new int[]{2, 1, -1, -2, -2, -1, 1, 2};
        for (int i = 0; i < fileModifiers.length; i++) {
            Square candidate = new Square(
                    this.getSquare().getFileIndex() + fileModifiers[i],
                    this.getSquare().getRankIndex() + rankModifiers[i]
            );
            if (candidate.isOnBoard()) {
                result[i] = new Square[]{candidate};
            } else {
                result[i] = new Square[0];
            }
        }
        return result;
    }

    @Override
    public String getName() {
        return "Knight";
    }

    @Override
    public String getAbbrevName() {
        return "N";
    }
}
