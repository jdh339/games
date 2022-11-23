package games.chess.model.piece;

import java.util.Comparator;

public class PieceComparator implements Comparator<Piece> {

    /**
     * Sort the pieces by type, then by file (a to h), then by rank (1 to 8)
     * 
     * The types are order based on starting position:
     * Pawns < Rooks < Knights < Bishops < Queens < King
     * 
     * @param p1 a Piece parsed from a FEN file.
     * @param p2 a Piece parsed from a FEN file.
     * @return -1 if p1 < p2, 0 if p1 == p2, 1 if p1 > p2
     */    
    @Override
    public int compare(Piece p1, Piece p2) {
        int diff = getPieceTypeAsInt(p1) - getPieceTypeAsInt(p2);
        if (diff == 0) {
            diff = p1.getSquare().getFileIndex() - p2.getSquare().getFileIndex();
            if (diff == 0) {
                diff = p1.getSquare().getRankIndex() - p2.getSquare().getRankIndex();
                if (diff == 0) {
                    return 0;
                }
            }
        }
        return diff / Math.abs(diff);
    }
    
    private int getPieceTypeAsInt(Piece piece) {
        int n;
        if (piece instanceof Pawn) {
            n = 0;
        } else if (piece instanceof Rook) {
            n = 1;
        } else if (piece instanceof Knight) {
            n = 2;
        } else  if (piece instanceof Bishop) {
            n = 3;
        } else if (piece instanceof Queen) {
            n = 4;
        } else {
            n = 5;
        }
        return n;
    }
}