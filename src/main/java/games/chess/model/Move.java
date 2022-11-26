package games.chess.model;

import games.chess.model.piece.King;
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

    public static Move castleKingside(King king) {
        Square dest = new Square(6, king.getSquare().getRankIndex()); // King to g file.
        return new Move(king, dest);
    }

    public static Move castleQueenside(King king) {
        Square dest = new Square(2, king.getSquare().getRankIndex()); // King to c file.
        return new Move(king, dest);
    }

    public Piece getMover() {
        return mover;
    }

    public boolean isCapture() {
        return isCapture;
    }

    public boolean isPawnDoubleJump() {
        return mover instanceof Pawn && 
                Math.abs(destSquare.getRankIndex() - originSquare.getRankIndex()) == 2;
    }

    public boolean isCastle() {
        return mover instanceof King &&
                Math.abs(destSquare.getFileIndex() - originSquare.getFileIndex()) == 2;
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
        if (isCastle()) {
            return destSquare.getFileIndex() == 6 ? "O-O" : "O-O-O";
        }
        
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