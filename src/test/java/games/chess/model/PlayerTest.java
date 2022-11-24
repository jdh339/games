package games.chess.model;

import games.chess.model.piece.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void canCreatePlayerFromPieceCollection() {
        TreeSet<Piece> pieces = new TreeSet<Piece>(new PieceComparator());
        pieces.add(new Pawn(true, new Square("a2")));
        King king = new King(true, new Square("e1"));
        pieces.add(king);
        pieces.add(new Knight(true, new Square("g1")));
        Player white = new Player(true, pieces);
        assertEquals(king, white.getKing());
        assertEquals(3, white.getPieces().length);
    }
    
    @Test
    void getCapableMovesReturnsExpectedNumberAtOpening() {
        Game game = new Game();
        Player white = game.getNextPlayer();
        int expectedNumMoves = 20;
        Move[] actual = white.getCapableMoves(game);
        System.out.println("Moves at start:");
        Arrays.stream(actual).forEach((m) -> System.out.println(m.getCanonicalName()));
        assertEquals(expectedNumMoves, white.getCapableMoves(game).length);
    }
}