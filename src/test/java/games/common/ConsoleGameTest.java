package games.common;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

public class ConsoleGameTest {

    void writeIn(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
    }

    @Test
    void getIntegerInputAcceptsValidInteger() {
        writeIn("");
    }

    @Test
    void getIntegerInputRejectsInputOutsideRange() {
        writeIn();
    }
}
