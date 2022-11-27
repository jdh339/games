package games.chess.model;

import games.chess.model.piece.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    
    Parser parser;
    String resourcesDir = "src/main/resources/chess/positions/";
    
    @BeforeEach
    void setUp() {
        parser = new Parser();
    }
    
    // Helper to avoid lots of redundant try/catches
    Game parseGameFromFileOrFail(String fileName) {
        try {
            return parser.parseFromFENFile(resourcesDir + fileName);
        } catch (Exception e) {
            fail("Failed during test setup: cannot parse file " + fileName);
        }
        return null;
    }

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
        Game scandinavianGame = parseGameFromFileOrFail("scandinavian.fen");
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
        Game enPassantGame = parseGameFromFileOrFail("en_passant.fen");
        ArrayList<Move> moves = enPassantGame.getWhitePlayer().getEnPassantMoves(enPassantGame);
        assertEquals(2, moves.size());
    }
    
    @Test
    void getCapableMovesEnPassantHas4Captures() {
        Game enPassantGame = parseGameFromFileOrFail("en_passant.fen");
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
        Game scandinavianGame = parseGameFromFileOrFail("scandinavian.fen");
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
        Game flankAttackGame = parseGameFromFileOrFail("flank_attack.fen");
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
        Game flankAttackGame = parseGameFromFileOrFail("flank_attack.fen");
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
}