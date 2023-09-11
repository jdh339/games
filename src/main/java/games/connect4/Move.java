package games.connect4;

import games.common.RegularMove;

public record Move(boolean red, int column) implements RegularMove {

    @Override
    public String getCanonicalName() {
        char c = this.red ? 'R' : 'B';
        return c + String.valueOf(column);
    }
}
