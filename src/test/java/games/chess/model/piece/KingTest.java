package games.chess.model.piece;

import games.chess.model.Square;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KingTest {

    @Test
    void getCapableSquares() {
        King king = new King(false, new Square("d4"));
        Square[][] expected = new Square[][] {
            {new Square("d5")},
            {new Square("e5")},
            {new Square("e4")},
            {new Square("e3")},
            {new Square("d3")},
            {new Square("c3")},
            {new Square("c4")},
            {new Square("c5")},
        };
        Square[][] actual = king.getCapableSquares();
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < actual.length; i++) {
            assertArrayEquals(expected[i], actual[i]);
        }
    }

    @Test
    void getName() {
        assertEquals("King", new King(true, new Square("e1")).getName());
    }

    @Test
    void getAbbrevName() {
        assertEquals("K", new King(true, new Square("e1")).getAbbrevName());
    }

    @Test
    void getFENAbbrevName() {
        King whiteKing = new King(true, new Square("e1"));
        King blackKing = new King(false, new Square("e8"));
        assertEquals('K', whiteKing.getFENAbbrevName());
        assertEquals('k', blackKing.getFENAbbrevName());
    }
}