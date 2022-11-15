package games.chess.model.piece;

import games.chess.model.Move;
import games.chess.model.Square;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PawnTest {

    @Test
    void getCapableSquaresWhiteHasNotMoved() {
        Pawn pawn = new Pawn(true, new Square("e2"));
        Square[][] expected = new Square[][]{
                {
                        new Square("e3"),
                        new Square("e4")
                }
        };
        Square[][] actual = pawn.getCapableSquares();
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < actual.length; i++) {
            assertArrayEquals(expected[i], actual[i]);
        }
    }

    @Test
    void getCapableSquaresWhiteHasMoved() {
        Pawn pawn = new Pawn(true, new Square("e2"));
        pawn.makeMove(new Move(pawn, new Square("e4")));
        Square[][] expected = new Square[][]{
                {new Square("e5")}
        };
        Square[][] actual = pawn.getCapableSquares();
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < actual.length; i++) {
            assertArrayEquals(expected[i], actual[i]);
        }
    }

    @Test
    void getCapableSquaresBlackHasNotMoved() {
        Pawn pawn = new Pawn(false, new Square("d7"));
        Square[][] expected = new Square[][]{
                {
                        new Square("d6"),
                        new Square("d5")
                }
        };
        Square[][] actual = pawn.getCapableSquares();
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < actual.length; i++) {
            assertArrayEquals(expected[i], actual[i]);
        }
    }

    @Test
    void getCapableSquaresBlackHasMoved() {
        Pawn pawn = new Pawn(false, new Square("d7"));
        pawn.makeMove(new Move(pawn, new Square("d5")));
        Square[][] expected = new Square[][]{
                {new Square("d4")}
        };
        Square[][] actual = pawn.getCapableSquares();
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < actual.length; i++) {
            assertArrayEquals(expected[i], actual[i]);
        }
    }

    @Test
    void getName() {
        assertEquals("Pawn", new Pawn(true, new Square("a2")).getName());
    }

    @Test
    void getAbbrevName() {
        assertEquals("", new Pawn(true, new Square("a2")).getAbbrevName());
    }
}