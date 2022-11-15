package games.chess.model.piece;

import games.chess.model.Move;
import games.chess.model.Square;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PieceTest {

    @Test
    void makeMove() {
        Piece rook = new Rook(true, new Square("a1"));
        assertFalse(rook.hasMoved());
        rook.makeMove(new Move());
        assertTrue(rook.hasMoved());
        // TODO finish this when method is implemented.        
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