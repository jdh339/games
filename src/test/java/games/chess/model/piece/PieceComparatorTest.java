package games.chess.model.piece;

import games.chess.model.Square;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PieceComparatorTest {

    PieceComparator comparator;
    
    @BeforeEach
    void setUp() {
        comparator = new PieceComparator();
    }
    
    @Test
    void compareReturnsCorrectlyForDifferentPieceTypes() {
        Piece[] pieces = new Piece[]{
                new Pawn(true, new Square("a1")),
                new Rook(true, new Square("a1")),
                new Knight(true, new Square("a1")),
                new Bishop(true, new Square("a1")),
                new Queen(true, new Square("a1")),
                new King(true, new Square("a1")),
        };
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces.length; j++) {
                int expected = Integer.compare(i, j);
                assertEquals(expected, comparator.compare(pieces[i], pieces[j]));
            }
        }
    }
    
    @Test
    void compareReturnsCorrectlyForDifferentFileIndexes() {
        Pawn pawnA = new Pawn(false, new Square("a7"));
        Pawn pawnB7Black = new Pawn(false, new Square("b7"));
        Pawn pawnB7White = new Pawn(true, new Square("b7"));
        assertEquals(-1, comparator.compare(pawnA, pawnB7Black));
        assertEquals(1, comparator.compare(pawnB7Black, pawnA));
        assertEquals(0, comparator.compare(pawnB7Black, pawnB7White));
    }

    @Test
    void compareReturnsCorrectlyForDifferentRankIndexes() {
        Pawn pawnA6 = new Pawn(false, new Square("a6"));
        Pawn pawnA7Black = new Pawn(false, new Square("a7"));
        Pawn pawnA7White = new Pawn(true, new Square("a7"));
        assertEquals(-1, comparator.compare(pawnA6, pawnA7Black));
        assertEquals(1, comparator.compare(pawnA7Black, pawnA6));
        assertEquals(0, comparator.compare(pawnA7Black, pawnA7White));
    }
}