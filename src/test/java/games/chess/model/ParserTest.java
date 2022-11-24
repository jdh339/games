package games.chess.model;

import games.chess.model.piece.Pawn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.regex.Matcher;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    Parser parser;
    String resourcesDir = "src/main/resources/chess/positions/";

    @BeforeEach
    void setUp() {
        parser = new Parser();
    }

    @Test
    void loadFENFileThrowsIfNotFound() {
        assertThrows(FileNotFoundException.class, () -> parser.loadFENFile("notfound.txt"));
    }

    @Test
    void canParseFENStartPosition() {
        String fenString = null;
        try {
            fenString = parser.loadFENFile(resourcesDir + "start_position.fen");
        } catch (FileNotFoundException e) {
            fail("Failed to parse, file not found!");
        }
        assertTrue(parser.matchFENString(fenString).matches());
    }

    @Test
    void matchFENStringFromFileReturnsCorrectGroupsForStarterPosition() {
        String fenString = null;
        try {
            fenString = parser.loadFENFile(resourcesDir + "start_position.fen");
        } catch (FileNotFoundException e) {
            fail("Failed to parse, file not found!");
        }
        Matcher matcher = parser.matchFENString(fenString);
        String expectedBoard = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
        String expectedPlayerToMove = "w";
        String expectedCastling = "KQkq";
        String expectedEnPassant = "-";
        String expectedHalfMovesCounter = "0";
        String expectedMoveNumber = "1";
        assertTrue(matcher.matches());
        assertEquals(expectedBoard, matcher.group("board"));
        assertEquals(expectedPlayerToMove, matcher.group("playerToMove"));
        assertEquals(expectedCastling, matcher.group("castling"));
        assertEquals(expectedEnPassant, matcher.group("enPassant"));
        assertEquals(expectedHalfMovesCounter, matcher.group("halfMovesCounter"));
        assertEquals(expectedMoveNumber, matcher.group("moveNumber"));
    }

    @Test
    void matchFENStringFromFileReturnsCorrectGroupsForScandinavian() {
        String fenString = null;
        try {
            fenString = parser.loadFENFile(resourcesDir + "scandinavian.fen");
        } catch (FileNotFoundException e) {
            fail("Failed to parse, file not found!");
        }
        Matcher matcher = parser.matchFENString(fenString);
        String expectedBoard = "rnbqkbnr/ppp1pppp/8/3p4/4P3/8/PPPP1PPP/RNBQKBNR";
        String expectedPlayerToMove = "w";
        String expectedCastling = "KQkq";
        String expectedEnPassant = "d6";
        String expectedHalfMovesCounter = "0";
        String expectedMoveNumber = "2";
        assertTrue(matcher.matches());
        assertEquals(expectedBoard, matcher.group("board"));
        assertEquals(expectedPlayerToMove, matcher.group("playerToMove"));
        assertEquals(expectedCastling, matcher.group("castling"));
        assertEquals(expectedEnPassant, matcher.group("enPassant"));
        assertEquals(expectedHalfMovesCounter, matcher.group("halfMovesCounter"));
        assertEquals(expectedMoveNumber, matcher.group("moveNumber"));
    }
    
    @Test
    void parseFromFENFileThrowsInvalidFENExceptionOnBadFormat() {
        String[] badFiles = new String[] {
                "bad_first_mover.fen",
                "too_few_rows.fen",
                "too_long_rows.fen",
                "too_many_rows.fen",
        };
        for (String badFile : badFiles) {
            String badFilePath = resourcesDir + "invalid_fen/" + badFile;
            assertThrows(InvalidFENFileException.class, () -> parser.parseFromFENFile(badFilePath));
        }
    }

    @Test
    void parseFromFENFileThrowsInvalidFENExceptionWrongSizeRows() {
        String[] badFiles = new String[] {
                "too_many_pawns.fen",
                "too_few_squares_in_row.fen",
                "too_many_squares_in_row.fen",
        };
        for (String badFile : badFiles) {
            String badFilePath = resourcesDir + "invalid_fen/" + badFile;
            assertThrows(InvalidFENFileException.class, () -> parser.parseFromFENFile(badFilePath));
        }
    }
    
    @Test
    void canCreateGameFromFEN() {
        Game game = null;
        try {
            game = parser.parseFromFENFile(resourcesDir + "scandinavian.fen");
        } catch (Exception e) {
            fail("Failed to parse, file not found!");
        }
        assertNotNull(game);
        assertTrue(game.getNextPlayer().isWhite());
        assertEquals(new Square("d6"), game.getEnPassantSquare());
        assertTrue(game.getPieceAt(new Square("d5")) instanceof Pawn);
    }
}