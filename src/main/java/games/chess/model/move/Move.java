package games.chess.model.move;

import games.chess.model.Square;
import games.chess.model.piece.Pawn;
import games.chess.model.piece.Piece;

public class Move {

    private final Piece mover;
    private final boolean isCapture;
    private final Square originSquare;
    private final Square destSquare;

    boolean isAmbiguousByFile = false;
    boolean isAmbiguousByRank = false;
    
    public Move(Piece mover, Square destSquare) {
        this.mover = mover;
        this.originSquare = mover.getSquare();
        this.destSquare = destSquare;
        this.isCapture = false;
    }

    public Move(Piece mover, Square destSquare, boolean isCapture) {
        this.mover = mover;
        this.originSquare = mover.getSquare();
        this.destSquare = destSquare;
        this.isCapture = isCapture;
    }

    public static boolean canParse(String moveName) {
        return false;  // TODO implement
    }

    public Piece getMover() {
        return mover;
    }

    public boolean isCapture() {
        return isCapture;
    }

    public Square getOriginSquare() {
        return originSquare;
    }

    public Square getDestSquare() {
        return destSquare;
    }


    /**
     * Writes this move in standard Chess notation.
     * Examples: e4, Be2, exd5, 0-0, Rd8#
     * 
     * // TODO - castling
     * // TODO - checks and checkmates
     * 
     * @return a String representation of the move, in normal Chess notation.
     */
    public String getCanonicalName() {
        StringBuilder builder = new StringBuilder();
        builder.append(mover.getAbbrevName());
        if (isAmbiguousByFile || (mover instanceof Pawn && isCapture)) {
            builder.append(mover.getSquare().getFileName());
        }
        if (isAmbiguousByRank) {
            builder.append(mover.getSquare().getRankName());
        }
        if (isCapture) {
            builder.append("x");
        }
        builder.append(destSquare.getName());
        return builder.toString();
    }
    
    public void setIsAmbiguous(boolean byFile, boolean byRank) {
        isAmbiguousByFile = byFile;
        isAmbiguousByRank = byRank;
    }
}