package games.puertorico.model;

/**
 * The ColonistShip class controls distribution of Colonists, including the colonist supply.
 * <p>
 * It implements the major functions of the Mayor Phase. A game model should have 1 of
 * these objects instantiated.
 */
public class ColonistShip {
    private static final int THREE_PLAYER_COLONIST_SUPPLY = 75;
    private static final int FOUR_PLAYER_COLONIST_SUPPLY = 95;
    private static final int FIVE_PLAYER_COLONIST_SUPPLY = 115;

    private int colonistsOnShip;
    private int colonistsInSupply;

    public ColonistShip(int numPlayers) {
        this.colonistsOnShip = numPlayers;
        if (numPlayers == 3) {
            colonistsInSupply = THREE_PLAYER_COLONIST_SUPPLY;
        } else if (numPlayers == 4) {
            colonistsInSupply = FOUR_PLAYER_COLONIST_SUPPLY;
        } else {
            colonistsInSupply = FIVE_PLAYER_COLONIST_SUPPLY;
        }
    }

    /**
     * Returns the current number of colonists on the ship.
     *
     * @return An int, readable by everyone.
     */
    public int getColonistsOnShip() {
        return colonistsOnShip;
    }

    /**
     * Returns the current number of colonists in the supply pool.
     *
     * @return An int, readable by everyone. The supply is strictly decreasing until game end.
     */
    public int getColonistsInSupply() {
        return colonistsInSupply;
    }

    /**
     * Reloads the ship for the given group of Players.
     *
     * @param players An array of Players in the game model.
     * @return A boolean, true if the reload succeeded. When false, the game will end soon!
     */
    public boolean reloadColonistShipForPlayers(Player[] players) {
        int colonistsToAdd = 0;
        for (Player player : players) {
            colonistsToAdd += player.getEmptyColonistSpacesInBuildings();
        }
        colonistsToAdd = Math.max(players.length, colonistsToAdd);
        boolean canReloadShip = colonistsToAdd <= colonistsInSupply;
        if (canReloadShip) {
            colonistsInSupply -= colonistsToAdd;
            colonistsOnShip = colonistsToAdd;
        }
        return canReloadShip;
    }

    /**
     * Performs the Mayor Phase action of giving players colonists.
     * <p>
     * The Mayor will get one from the supply, if there are any. Colonists on the ship will
     * be distributed one by one, starting with the Mayor.
     *
     * @param players An array of Players, in turn order. At least one must be the Mayor now.
     */
    public void giveColonistsToPlayers(Player[] players) {
        // Get the index of the Mayor. If no Player is the Mayor, this will fail.
        int mayorIndex = -1;
        for (int i = 0; i < players.length; i++) {
            if (players[i].isActingRole(RoleCardType.MAYOR)) {
                mayorIndex = i;

                // While we're here, give the Mayor a colonist from the supply, if there are any.
                if (colonistsInSupply > 0) {
                    colonistsInSupply -= 1;
                    players[i].addFreeColonist();
                }
                break;
            }
        }

        // Starting with the Mayor, give each player a colonist from the ship.
        int indexOfPlayerToPickColonist = mayorIndex;
        while (colonistsOnShip > 0) {
            colonistsOnShip -= 1;
            players[indexOfPlayerToPickColonist].addFreeColonist();

            // Increment index and take the modulo to move to next player.
            indexOfPlayerToPickColonist = (indexOfPlayerToPickColonist + 1) % players.length;
        }
    }
}
