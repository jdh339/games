package games.chess.model.piece;

import games.chess.model.Square;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BishopTest {

    @Test
    void getCapableSquares() {
        Bishop bishop = new Bishop(false, new Square("d6"));
        Square[][] expected = new Square[][]{
                {
                        new Square("e7"),
                        new Square("f8"),
                },
                {
                        new Square("e5"),
                        new Square("f4"),
                        new Square("g3"),
                        new Square("h2"),
                },
                {
                        new Square("c5"),
                        new Square("b4"),
                        new Square("a3"),
                },
                {
                        new Square("c7"),
                        new Square("b8"),
                }
        };
        Square[][] actual = bishop.getCapableSquares();
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < actual.length; i++) {
            assertArrayEquals(expected[i], actual[i]);
        }
    }

    @Test
    void getName() {
        assertEquals("Bishop", new Bishop(true, new Square("a3")).getName());
    }

    @Test
    void getAbbrevName() {
        assertEquals("B", new Bishop(true, new Square("a3")).getAbbrevName());
    }
}