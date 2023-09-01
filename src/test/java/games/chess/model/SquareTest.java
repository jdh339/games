package games.chess.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SquareTest {

    @Test
    void testEquals() {
        assertEquals(new Square("e4"), new Square("e4"));
        assertNotEquals(new Square("e1"), null);
    }
    
    @Test
    void canParseReturnsTrueForAllSquares() {
        for (int fileIndex = 0; fileIndex < 8; fileIndex++) {
            for (int rank = 1; rank <= 8; rank++) {
                char file = (char)(fileIndex + (int)'a');
                assertTrue(Square.canParse("" + file + rank));
            }
        }
    }
    
    @Test
    void canParseReturnsFalseForBadInput() {
        String[] tests = new String[]{"a0", "b9", "A2", "i3", "Re6"};
        for (String test : tests) {
            assertFalse(Square.canParse(test));
        }
    }

    @Test
    void parseFileIndex() {
        assertEquals(-1, Square.parseFileIndex("A5"));
        assertEquals(0, Square.parseFileIndex("a6"));
        assertEquals(5, Square.parseFileIndex("f7"));
        assertEquals(7, Square.parseFileIndex("h3"));
    }

    @Test
    void parseRankIndex() {
        assertEquals(-1, Square.parseRankIndex("b9"));
        assertEquals(0, Square.parseRankIndex("c1"));
        assertEquals(5, Square.parseRankIndex("c6"));
        assertEquals(7, Square.parseRankIndex("d8"));
    }

    @Test
    void getName() {
        assertEquals("a1", new Square(0, 0).getName());
        assertEquals("c3", new Square(2, 2).getName());
        assertEquals("d6", new Square(3, 5).getName());
        assertEquals("h8", new Square(7, 7).getName());
    }

    @Test
    void isOnBoard() {
        assertTrue(new Square(0,0).isOnBoard());
        assertTrue(new Square(7,7).isOnBoard());
        assertTrue(new Square("b8").isOnBoard());
        assertTrue(new Square("h1").isOnBoard());
        assertFalse(new Square(8, 8).isOnBoard());
        assertFalse(new Square(0, 8).isOnBoard());
        assertFalse(new Square(-1, 4).isOnBoard());
    }

}