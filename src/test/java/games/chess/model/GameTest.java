package games.chess.model;

import games.chess.model.piece.Piece;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    
    @Test
    void readStartPositionFromFile() {
        
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