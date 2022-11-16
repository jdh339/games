package games.chess.model;

import games.chess.model.piece.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoveTest {

    @Test
    void getCanonicalNamePawnMover() {
        Pawn whitePawn = new Pawn(true, new Square("b2"));
        Pawn blackPawn = new Pawn(false, new Square("f7"));
        assertEquals("b4", new Move(whitePawn, new Square("b4")).getCanonicalName());
        assertEquals("f6", new Move(blackPawn, new Square("f6")).getCanonicalName());
    }
    
    @Test
    void getCanonicalNameKingMover() {
        King king = new King(true, new Square("e1"));
        assertEquals("Ke2", new Move(king, new Square("e2")).getCanonicalName());
    }
    
    @Test
    void getCanonicalNameQueenMover() {
        Queen queen = new Queen(true, new Square("d1"));
        assertEquals("Qd8", new Move(queen, new Square("d8")).getCanonicalName());
    }
    
    @Test
    void getCanonicalNameRookMover() {
        Rook rook = new Rook(false, new Square("a8"));
        assertEquals("Rc8", new Move(rook, new Square("c8")).getCanonicalName());
    }
    
    @Test
    void getCanonicalNameKnightMover() {
        Knight knight = new Knight(false, new Square("g8"));
        assertEquals("Nf7", new Move(knight, new Square("f7")).getCanonicalName());
    }

    @Test
    void getCanonicalNameBishopMover() {
        Bishop bishop = new Bishop(false, new Square("d5"));
        assertEquals("Be4", new Move(bishop, new Square("e4")).getCanonicalName());
    }
    
    @Test
    void getCanonicalNameWithQueenCaptures() {
        Queen queen = new Queen(false, new Square("d8"));
        Move move = new Move(queen, new Square("h4"), true);
        assertEquals("Qxh4", move.getCanonicalName());
    }
    
    @Test
    void getCanonicalNameWithPawnCaptures() {
        Pawn pawn = new Pawn(true, new Square("e4"));
        Move move = new Move(pawn, new Square("d5"), true);
        assertEquals("exd5", move.getCanonicalName());
    }
    
    @Test
    void getCanonicalNameWithPieceDescriptors() {
        
    }
    
    @Test
    void canParse() {
    }
}