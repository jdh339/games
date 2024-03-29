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
        Square h4 = new Square("h4");
        Pawn captured = new Pawn(true, h4);
        Move move = new Move(queen, h4, captured);
        assertEquals("Qxh4", move.getCanonicalName());
    }
    
    @Test
    void getCanonicalNameWithPawnCaptures() {
        Pawn pawn = new Pawn(true, new Square("e4"));
        Square d5 = new Square("d5");
        Pawn captured = new Pawn(false, d5);
        Move move = new Move(pawn, d5, captured);
        assertEquals("exd5", move.getCanonicalName());
    }
    
    @Test
    void getCanonicalNameWithFileAmbiguity() {
        Rook rookA = new Rook(true, new Square("a1"));
        Move Rad1 = new Move(rookA, new Square("d1"));
        assertEquals("Rd1", Rad1.getCanonicalName());
        Rad1.setIsAmbiguous(true, false);
        assertEquals("Rad1", Rad1.getCanonicalName());
    }
    
    @Test
    void getCanonicalNameWithRankAmbiguity() {
        Rook rook8 = new Rook(false, new Square("d8"));
        Move R8d4 = new Move(rook8, new Square("d4"));
        assertEquals("Rd4", R8d4.getCanonicalName());
        R8d4.setIsAmbiguous(false, true);
        assertEquals("R8d4", R8d4.getCanonicalName());
    }
    
    @Test
    void getCanonicalNameWithBothAmbiguity() {
        Queen queenA3 = new Queen(true, new Square("a3"));
        Move Qa3b4 = new Move(queenA3, new Square("b4"));
        assertEquals("Qb4", Qa3b4.getCanonicalName());
        Qa3b4.setIsAmbiguous(true, true);
        assertEquals("Qa3b4", Qa3b4.getCanonicalName());
    }
    
    @Test
    void testIsPawnDoubleJump() {
        Pawn a2Pawn = new Pawn(true, new Square("a2"));
        Pawn b2Pawn = new Pawn(true, new Square("b2"));
        Pawn a7Pawn = new Pawn(false, new Square("a7"));
        Pawn b7Pawn = new Pawn(false, new Square("b7"));
        Rook h1Rook = new Rook(true, new Square("h1"));
        Move a3 = new Move(a2Pawn, new Square("a3"));
        Move b4 = new Move(b2Pawn, new Square("b4"));
        Move a5 = new Move(a7Pawn, new Square("a5"));
        Move b6 = new Move(b7Pawn, new Square("b6"));
        Move Rh3 = new Move(h1Rook, new Square("h3"));
        assertTrue(b4.isPawnDoubleJump());
        assertTrue(a5.isPawnDoubleJump());
        assertFalse(a3.isPawnDoubleJump());
        assertFalse(b6.isPawnDoubleJump());
        assertFalse(Rh3.isPawnDoubleJump());
    }
    
    @Test
    void testIsCastleReturnsTrue() {
        King whiteKing = new King(true, new Square("e1"));
        King blackKing = new King(false, new Square("e8"));
        Move whiteO_O = Move.castleKingside(whiteKing);
        Move blackO_O = Move.castleKingside(blackKing);
        Move whiteO_O_O = Move.castleQueenside(whiteKing);
        Move blackO_O_O = Move.castleQueenside(blackKing);
        assertTrue(whiteO_O.isCastle());
        assertTrue(whiteO_O_O.isCastle());
        assertTrue(blackO_O.isCastle());
        assertTrue(blackO_O_O.isCastle());
    }
    
    @Test
    void testIsCastleReturnsFalse() {
        King whiteKing = new King(true, new Square("e1"));
        King blackKing = new King(false, new Square("e8"));
        Rook h1Rook = new Rook(true, new Square("h1"));
        Move Ke2 = new Move(whiteKing, new Square("e2"));
        Move Kd7 = new Move(blackKing, new Square("d7"));
        Move Rf1 = new Move(h1Rook, new Square("f1"));
        assertFalse(Ke2.isCastle());
        assertFalse(Kd7.isCastle());
        assertFalse(Rf1.isCastle());
    }
    
    @Test
    void testCanonicalNameReturnsCorrectForCastles() {
        King whiteKing = new King(true, new Square("e1"));
        King blackKing = new King(false, new Square("e8"));
        Move whiteO_O = Move.castleKingside(whiteKing);
        Move blackO_O = Move.castleKingside(blackKing);
        Move whiteO_O_O = Move.castleQueenside(whiteKing);
        Move blackO_O_O = Move.castleQueenside(blackKing);
        assertEquals("O-O", whiteO_O.getCanonicalName());
        assertEquals("O-O", blackO_O.getCanonicalName());
        assertEquals("O-O-O", whiteO_O_O.getCanonicalName());
        assertEquals("O-O-O", blackO_O_O.getCanonicalName());
    }
    
    @Test
    void canParse() {
    }
}