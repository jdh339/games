package games.chess.model.piece;

import games.chess.model.Square;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class QueenTest {

    @Test
    void getCapableSquares() {
        Queen queen = new Queen(true, new Square("g4"));
        Square[][] expected = new Square[][]{
                {
                        new Square("g5"),
                        new Square("g6"),
                        new Square("g7"),
                        new Square("g8"),
                },
                {
                        new Square("h4"),
                },
                {
                        new Square("g3"),
                        new Square("g2"),
                        new Square("g1"),
                },
                {
                        new Square("f4"),
                        new Square("e4"),
                        new Square("d4"),
                        new Square("c4"),
                        new Square("b4"),
                        new Square("a4"),
                },
                {
                        new Square("h5"),
                },
                {
                        new Square("h3"),
                },
                {
                        new Square("f3"),
                        new Square("e2"),
                        new Square("d1"),
                },
                {
                        new Square("f5"),
                        new Square("e6"),
                        new Square("d7"),
                        new Square("c8"),
                },
        };
        Square[][] actual = queen.getCapableSquares();
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < actual.length; i++) {
            assertArrayEquals(expected[i], actual[i]);
        }
    }

    @Test
    void getCapableSquaresInCorner() {
        Queen queen = new Queen(false, new Square("h8"));
        Square[][] expected = new Square[][]{
                {},
                {},
                {
                        new Square("h7"),
                        new Square("h6"),
                        new Square("h5"),
                        new Square("h4"),
                        new Square("h3"),
                        new Square("h2"),
                        new Square("h1"),
                },
                {
                        new Square("g8"),
                        new Square("f8"),
                        new Square("e8"),
                        new Square("d8"),
                        new Square("c8"),
                        new Square("b8"),
                        new Square("a8"),
                },
                {},
                {},
                {
                        new Square("g7"),
                        new Square("f6"),
                        new Square("e5"),
                        new Square("d4"),
                        new Square("c3"),
                        new Square("b2"),
                        new Square("a1"),
                },
                {}
        };
        Square[][] actual = queen.getCapableSquares();
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < actual.length; i++) {
            assertArrayEquals(expected[i], actual[i]);
        }
    }

    @Test
    void getName() {
        assertEquals("Queen", new Queen(true, new Square("d1")).getName());
    }

    @Test
    void getAbbrevName() {
        assertEquals("Q", new Queen(false, new Square("d8")).getAbbrevName());
    }

    @Test
    void getFENAbbrevName() {
        Queen whiteQueen = new Queen(true, new Square("d1"));
        Queen blackQueen = new Queen(false, new Square("d8"));
        assertEquals('Q', whiteQueen.getFENAbbrevName());
        assertEquals('q', blackQueen.getFENAbbrevName());
    }
}