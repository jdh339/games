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
    Game initial;
    Game scandinavianGame;
    Game enPassantGame;
    
    @BeforeEach
    void setUp() {
        parser = new Parser();
        initial = new Game();
        try {
            scandinavianGame = parser.parseFromFENFile(resourcesDir + "scandinavian.fen");
            enPassantGame = parser.parseFromFENFile(resourcesDir + "en_passant.fen");
        } catch (Exception e) {
            fail("Failed during test setup: cannot parse FEN!");
        }
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
        Player white = initial.getNextPlayer();
        int expectedNumMoves = 20;
        Move[] actual = white.getCapableMoves(initial);
        assertEquals(expectedNumMoves, actual.length);
    }
    
    @Test
    void getCapableMovesReturnsPawnCaptureInScandinavian() {
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
        ArrayList<Move> moves = enPassantGame.getWhitePlayer().getEnPassantMoves(enPassantGame);
        assertEquals(2, moves.size());
    }
    
    @Test
    void getCapableMovesEnPassantHas4Captures() {
        Move[] moves = enPassantGame.getWhitePlayer().getCapableMoves(enPassantGame);
        int numCaptures = 0;
        for (Move move : moves) {
            if (move.getMover() instanceof Queen) {
                System.out.println(move.getCanonicalName());
            }
            if (move.isCapture()) {
                numCaptures++;
            }
        }
        assertEquals(4, numCaptures);
    }
}