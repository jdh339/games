package games.chess.model;

import static org.junit.jupiter.api.Assertions.fail;

class TestUtils {
    
    protected final static String resourcesDir = "src/main/resources/chess/positions/";
    
    protected static Game parseGameFromFileOrFail(String fileName) {
        try {
            return new Parser().parseFromFENFile(resourcesDir + fileName);
        } catch (Exception e) {
            fail("Failed during test setup: cannot parse file " + fileName);
            return new Game();
        }
    } 
}