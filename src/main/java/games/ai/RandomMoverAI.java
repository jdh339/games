package games.ai;

import games.common.InputPlayer;
import games.common.RegularGame;
import games.common.RegularMove;

import java.util.Random;

public class RandomMoverAI implements InputPlayer {

    @Override
    public String getDescription() {
        return "Random Moves AI";
    }

    public RegularMove selectMove(RegularGame game) {
        RegularMove[] legalMoves = game.getLegalMoves();
        return legalMoves[new Random().nextInt(legalMoves.length)];
    }

    @Override
    public void notifyOpponentMoved(RegularMove move) {}
}
