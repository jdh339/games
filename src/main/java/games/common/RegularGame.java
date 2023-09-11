package games.common;

public interface RegularGame {

    public String getCurrentPlayer();

    public String getLastPlayer();

    public String getWinner();

    public boolean isFinished();

    public RegularMove[] getLegalMoves();

    public RegularGame cloneWithUpdate(RegularMove move);
}
