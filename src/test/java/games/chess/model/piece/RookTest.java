package games.chess.model.piece;

import games.chess.model.Square;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RookTest {

    @Test
    void getCapableSquares() {
        Rook rook = new Rook(true, new Square("c3"));
        Square[][] expected = new Square[][]{
                {
                        new Square("c4"),
                        new Square("c5"),
                        new Square("c6"),
                        new Square("c7"),
                        new Square("c8"),
                },
                {
                        new Square("d3"),
                        new Square("e3"),
                        new Square("f3"),
                        new Square("g3"),
                        new Square("h3"),
                },
                {
                        new Square("c2"),
                        new Square("c1"),
                },
                {
                        new Square("b3"),
                        new Square("a3"),
                }
        };
        Square[][] actual = rook.getCapableSquares();
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < actual.length; i++) {
            assertArrayEquals(expected[i], actual[i]);
        }
    }

    @Test
    void getName() {
        assertEquals("Rook", new Rook(false, new Square("a1")).getName());
    }

    @Test
    void getAbbrevName() {
        assertEquals("R", new Rook(false, new Square("a1")).getAbbrevName());
    }
}