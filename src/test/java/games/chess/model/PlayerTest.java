package games.chess.model;

import games.chess.model.piece.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void canCreatePlayerFromPieceCollection() {
        TreeSet<Piece> pieces = new TreeSet<>(new PieceComparator());
        Pawn pawn = new Pawn(true, new Square("a2"));
        King king = new King(true, new Square("e1"));
        Knight knight = new Knight(true, new Square("g1"));
        pieces.add(pawn);
        pieces.add(king);
        pieces.add(knight);
        Player white = new Player(true, pieces);
        assertEquals(king, white.getKing());
        assertEquals(3, white.getPieces().length);
    }
    
    @Test
    void getCapableMovesReturnsExpectedNumberAtOpening() {
        Game initial = new Game();
        Player white = initial.getActivePlayer();
        int expectedNumMoves = 20;
        Move[] actual = white.getCapableMoves(initial);
        assertEquals(expectedNumMoves, actual.length);
    }
    
    @Test
    void getCapableMovesReturnsPawnCaptureInScandinavian() {
        Game scandinavianGame = TestUtils.parseGameFromFileOrFail("scandinavian.fen");
        Move[] whiteCapable = scandinavianGame.getWhitePlayer().getCapableMoves(scandinavianGame);
        Move pawnCapture = null;
        for (Move move : whiteCapable) {
            if (move.isCapture()) {
                pawnCapture = move;
                assertEquals(new Square("d5"), pawnCapture.getDestSquare());
            }
        }
        assertNotNull(pawnCapture);
    }
    
    @Test
    void getEnPassantMoves() {
        Game enPassantGame = TestUtils.parseGameFromFileOrFail("en_passant.fen");
        ArrayList<Move> moves = enPassantGame.getWhitePlayer().getEnPassantMoves(enPassantGame);
        assertEquals(2, moves.size());
    }
    
    @Test
    void getCapableMovesEnPassantHas4Captures() {
        Game enPassantGame = TestUtils.parseGameFromFileOrFail("en_passant.fen");
        Move[] moves = enPassantGame.getWhitePlayer().getCapableMoves(enPassantGame);
        int numCaptures = 0;
        for (Move move : moves) {
            if (move.isCapture()) {
                numCaptures++;
            }
        }
        assertEquals(4, numCaptures);
    }
    
    @Test
    void playerMakingAKingMoveRemovesCastlingAbility() {
        Game scandinavianGame = TestUtils.parseGameFromFileOrFail("scandinavian.fen");
        Player whitePlayer = scandinavianGame.getWhitePlayer();
        assertTrue(whitePlayer.canCastleKingside);
        assertTrue(whitePlayer.canCastleQueenside);
        Move Ke2 = new Move(whitePlayer.getKing(), new Square("e2"));
        whitePlayer.makeMove(Ke2);
        assertEquals(new Square("e2"), whitePlayer.getKing().getSquare());
        assertFalse(whitePlayer.canCastleKingside);
        assertFalse(whitePlayer.canCastleQueenside);
    }
    
    @Test
    void playerMakingARookMoveRemovesQueensideCastlingAbility() {
        Game flankAttackGame = TestUtils.parseGameFromFileOrFail("flank_attack.fen");
        Player white = flankAttackGame.getWhitePlayer();
        Player black = flankAttackGame.getBlackPlayer();
        Square a2 = new Square("a2");
        Square a7 = new Square("a7");
        Move Ra2 = new Move(flankAttackGame.getPieceAt(new Square("a1")), a2);
        Move Ra7 = new Move(flankAttackGame.getPieceAt(new Square("a8")), a7);
        assertTrue(white.canCastleQueenside);
        white.makeMove(Ra2);
        assertFalse(white.canCastleQueenside);
        assertTrue(white.canCastleKingside);
        
        assertTrue(black.canCastleQueenside);
        black.makeMove(Ra7);
        assertFalse(black.canCastleQueenside);
        assertTrue(black.canCastleKingside);
    }

    @Test
    void playerMakingARookMoveRemovesKingsideCastlingAbility() {
        Game flankAttackGame = TestUtils.parseGameFromFileOrFail("flank_attack.fen");
        Player white = flankAttackGame.getWhitePlayer();
        Player black = flankAttackGame.getBlackPlayer();
        Square h2 = new Square("h2");
        Square h7 = new Square("h7");
        Move Rh2 = new Move(flankAttackGame.getPieceAt(new Square("h1")), h2);
        Move Rh7 = new Move(flankAttackGame.getPieceAt(new Square("h8")), h7);
        assertTrue(white.canCastleKingside);
        white.makeMove(Rh2);
        assertFalse(white.canCastleKingside);
        assertTrue(white.canCastleQueenside);

        assertTrue(black.canCastleKingside);
        black.makeMove(Rh7);
        assertFalse(black.canCastleKingside);
        assertTrue(black.canCastleQueenside);
    }
    
    @Test
    void playerDoesNotMakeMoveIfMoverIsOppositeColor() {
        Game game = new Game();
        Player whitePlayer = game.getWhitePlayer();
        Piece whiteBishop = game.getPieceAt("f1");
        game.makeMove(new Move(game.getPieceAt("e2"), "e3"));
        game.makeMove(new Move(game.getPieceAt("b7"), "b6"));
        assertEquals(new Square("f1"), whiteBishop.getSquare());
        whitePlayer.makeMove(new Move(game.getPieceAt("c8"), "a6"));
        assertEquals(new Square("f1"), whiteBishop.getSquare());
        whitePlayer.makeMove(new Move(game.getPieceAt("f1"), "a6"));
        assertEquals(new Square("a6"), whiteBishop.getSquare());
    }
    
    @Test
    void canCopyPlayer() {
        Player original = new Player(true);
        Player copy = new Player(original);
        Piece originalPawn = original.getPieces()[0];
        Piece copyPawn = copy.getPieces()[0];
        assertNotEquals(originalPawn, copyPawn);
        assertEquals(originalPawn.getSquare(), copyPawn.getSquare());
        original.makeMove(new Move(originalPawn, new Square("a3")));
        assertNotEquals(originalPawn.getSquare(), copyPawn.getSquare());
    }
    
    @Test
    void removingCastlingAbilityWithKingMoveIsSavedInMove() {
        Game scandinavianGame = TestUtils.parseGameFromFileOrFail("scandinavian.fen");
        Player whitePlayer = scandinavianGame.getWhitePlayer();
        Move Ke2 = new Move(whitePlayer.getKing(), "e2");
        scandinavianGame.makeMove(Ke2);
        assertTrue(Ke2.didRevokeKingsideCastle);
        assertTrue(Ke2.didRevokeQueensideCastle);
        scandinavianGame.makeMove(new Move(scandinavianGame.getPieceAt("a7"), "a6"));
        Move Ke3 = new Move(whitePlayer.getKing(), "e3");
        scandinavianGame.makeMove(Ke3);
        assertFalse(Ke3.didRevokeKingsideCastle);
        assertFalse(Ke3.didRevokeQueensideCastle);
    }
    
    @Test
    void removingKingsideCastlingAbilityWithRookMoveIsSavedInMove() {
        Game flankAttackGame = TestUtils.parseGameFromFileOrFail("flank_attack.fen");
        Player white = flankAttackGame.getWhitePlayer();
        Player black = flankAttackGame.getBlackPlayer();
        Move Rh2 = new Move(flankAttackGame.getPieceAt("h1"), "h2");
        Move Rh7 = new Move(flankAttackGame.getPieceAt("h8"), "h7");
        assertFalse(Rh2.didRevokeKingsideCastle);
        white.makeMove(Rh2);
        assertTrue(Rh2.didRevokeKingsideCastle);

        assertFalse(Rh7.didRevokeKingsideCastle);
        black.makeMove(Rh7);
        assertTrue(Rh7.didRevokeKingsideCastle);
    }

    @Test
    void removingQueensideCastlingAbilityWithRookMoveIsSavedInMove() {
        Game flankAttackGame = TestUtils.parseGameFromFileOrFail("flank_attack.fen");
        Player white = flankAttackGame.getWhitePlayer();
        Player black = flankAttackGame.getBlackPlayer();
        Move Ra2 = new Move(flankAttackGame.getPieceAt("a1"), "a2");
        Move Ra7 = new Move(flankAttackGame.getPieceAt("a8"), "a7");
        assertFalse(Ra2.didRevokeQueensideCastle);
        white.makeMove(Ra2);
        assertTrue(Ra2.didRevokeQueensideCastle);

        assertFalse(Ra7.didRevokeQueensideCastle);
        black.makeMove(Ra7);
        assertTrue(Ra7.didRevokeQueensideCastle);
    }
}