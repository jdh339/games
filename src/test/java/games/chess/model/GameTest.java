package games.chess.model;

import games.chess.model.piece.Piece;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    
    @Test
    void readStartPositionFromFile() {
        
    }
    
    @Test
    void makeMovePushesToMoveHistory() {
        Game game = new Game();
        Piece e2Pawn = game.getPieceAt("e2");
        Move e4 = new Move(e2Pawn, "e4");
        assertFalse(game.canUndoLastMove());
        game.makeMove(e4);
        assertTrue(game.canUndoLastMove());
    }
    
    @Test
    void undoingLastMoveRemovesFromMoveHistory() {
        Game game = new Game();
        Piece e2Pawn = game.getPieceAt("e2");
        Move e4 = new Move(e2Pawn, "e4");
        game.makeMove(e4);
        assertTrue(game.canUndoLastMove());
        game.undoLastMove();
        assertFalse(game.canUndoLastMove());
    }
    
    @Test
    void undoingLastMoveReplacesPieceAtItsPosition() {
        Game game = new Game();
        Piece pawn = game.getPieceAt("e2");
        Move e4 = new Move(pawn, "e4");
        game.makeMove(e4);
        assertNull(game.getPieceAt("e2"));
        assertEquals(game.getPieceAt("e4"), pawn);
        game.undoLastMove();
        assertNull(game.getPieceAt("e4"));
        assertEquals(pawn, game.getPieceAt("e2"));
    }

    @Test
    void makePawnDoubleJumpCorrectlySetsEnPassantSquare() {
        Game game = new Game();
        Piece e2Pawn = game.getPieceAt(new Square("e2"));
        Piece d7Pawn = game.getPieceAt(new Square("d7"));
        game.makeMove(new Move(e2Pawn, new Square("e4")));
        assertEquals(new Square("e3"), game.getEnPassantSquare());
        assertEquals(e2Pawn, game.getEnPassantCapturablePiece());
        game.makeMove(new Move(d7Pawn, new Square("d5")));
        assertEquals(new Square("d6"), game.getEnPassantSquare());
        assertEquals(d7Pawn, game.getEnPassantCapturablePiece());
    }
    
}