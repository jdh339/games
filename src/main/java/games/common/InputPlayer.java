package games.common;

public interface InputPlayer {

    String getDescription();

    RegularMove selectMove(RegularGame game);

    void notifyOpponentMoved(RegularMove move);
}
