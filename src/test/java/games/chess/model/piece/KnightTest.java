package games.chess.model.piece;

import games.chess.model.Square;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class KnightTest {

    @Test
    void getCapableSquares() {
        Knight knight = new Knight(false, new Square("d4"));
        Square[][] expected = new Square[][]{
                {new Square("e6")},
                {new Square("f5")},
                {new Square("f3")},
                {new Square("e2")},
                {new Square("c2")},
                {new Square("b3")},
                {new Square("b5")},
                {new Square("c6")},
        };
        Square[][] actual = knight.getCapableSquares();
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < actual.length; i++) {
            assertArrayEquals(expected[i], actual[i]);
        }
    }
    
    @Test
    void getCapableSquaresNearRim() {
        Knight knight = new Knight(false, new Square("b2"));
        Square[][] expected = new Square[][]{
                {new Square("c4")},
                {new Square("d3")},
                {new Square("d1")},
                {},
                {},
                {},
                {},
                {new Square("a4")},
        };
        Square[][] actual = knight.getCapableSquares();
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < actual.length; i++) {
            assertArrayEquals(expected[i], actual[i]);
        }
    }

    @Test
    void getName() {
        assertEquals("Knight", new Knight(true, new Square("b1")).getName());
    }

    @Test
    void getAbbrevName() {
        assertEquals("N", new Knight(true, new Square("b1")).getAbbrevName());
    }
}