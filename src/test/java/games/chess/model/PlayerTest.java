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
        Player white = initial.getNextPlayer();
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
}