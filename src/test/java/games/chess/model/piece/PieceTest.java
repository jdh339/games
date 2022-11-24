package games.chess.model.piece;

import games.chess.model.move.Move;
import games.chess.model.Square;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PieceTest {

    @Test
    void makeMove() {
        Piece rook = new Rook(true, new Square("a1"));
        Square dest = new Square("a6");
        rook.makeMove(new Move(rook, dest));
        assertEquals(dest, rook.getSquare());
    }

    @Test
    void removeFromPlay() {
        Piece rook = new Rook(true, new Square("a1"));
        assertTrue(rook.isInPlay());
        rook.removeFromPlay();
        assertFalse(rook.isInPlay());
    }

    @Test
    void isWhite() {
        Piece whiteKing = new King(true, new Square("e1"));
        Piece blackKing = new King(false, new Square("e8"));
        assertTrue(whiteKing.isWhite());
        assertFalse(blackKing.isWhite());
    }
}