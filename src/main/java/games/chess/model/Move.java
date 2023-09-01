package games.chess.model;

import games.chess.model.piece.King;
import games.chess.model.piece.Pawn;
import games.chess.model.piece.Piece;

public class Move {

    private final Piece mover;
    private final Piece capturedPiece;
    private final Square originSquare;
    private final Square destSquare;

    boolean isAmbiguousByFile = false;
    boolean isAmbiguousByRank = false;
    
    public Move(Piece mover, Square destSquare) {
        this(mover, destSquare, null);
    }
    
    public Move(Piece mover, String squareName) {
        this(mover, new Square(squareName));
    }

    public Move(Piece mover, Square destSquare, Piece capturedPiece) {
        this.mover = mover;
        this.originSquare = mover.getSquare();
        this.destSquare = destSquare;
        this.capturedPiece = capturedPiece;
    }
    
    public Move(Move toCopy, Game game) {
        this.mover = game.getPieceAt(toCopy.getOriginSquare());
        this.originSquare = toCopy.getOriginSquare();
        this.destSquare = toCopy.getDestSquare();
        this.capturedPiece = toCopy.getCapturedPiece();
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
    
    public Piece getCapturedPiece() {
        return capturedPiece;
    }

    public boolean isCapture() {
        return this.capturedPiece != null;
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
        if (isAmbiguousByFile || (mover instanceof Pawn && isCapture())) {
            builder.append(mover.getSquare().getFileName());
        }
        if (isAmbiguousByRank) {
            builder.append(mover.getSquare().getRankName());
        }
        if (isCapture()) {
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