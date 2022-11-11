package games.puertorico.model;

import java.util.ArrayList;

/**
 * A TradingHouse handles all trade operations during the Trader Phase.
 * <p>
 * A Game of "Puerto Rico" should need exactly one TradingHouse object in its model.
 */
public class TradingHouse {

    private static final int CAPACITY = 4;
    private final ArrayList<Good> goods = new ArrayList<>(CAPACITY);

    /**
     * Returns the base doubloon value for trading a type of good.
     *
     * @param good An instance of Good: corn, indigo, etc.
     * @return An integer 0-4.
     */
    public int getDoubloonsForGood(Good good) {
        return good.ordinal();
    }

    /**
     * Returns whether the game rules permit the given Player to trade the given Good.
     *
     * @param player A Player object, including their goods and buildings.
     * @param good   A type of good: the player must possess at least one of these.
     * @return A boolean indicating whether the trade is allowed.
     */
    public boolean playerCanTradeGood(Player player, Good good) {
        if (goods.size() == CAPACITY) {
            return false;
        }
        if (!player.hasGood(good)) {
            return false;
        }
        if (!goods.contains(good)) {
            return true;
        }
        return player.hasOccupiedBuilding(BuildingType.OFFICE);
    }

    /**
     * Performs a trade with a Player by removing their good, adding it to the TradingHouse, and
     * awarding doubloons.
     * <p>
     * Note: the caller *must* check if the trade is permitted by calling
     * {@link #playerCanTradeGood(Player, Good)} before this method.
     *
     * @param player A Player who is trading with the TradingHouse.
     * @param good   A unit of Good to subtract from the player and add to the TradingHouse.
     */
    public void tradeGoodWithPlayer(Player player, Good good) {
        player.removeGoods(good, 1);
        goods.add(good);
        int doubloons = getValueOfTradeWithPlayer(player, good);
        player.addDoubloons(doubloons);
    }

    /**
     * Clears the TradingHouse if it is full. Called at the end of the Trader Phase.
     */
    public void clearIfFull() {
        if (goods.size() == CAPACITY) {
            goods.clear();
        }
    }

    /**
     * Returns the number of doubloons that will be awarded if the Player trades the given Good.
     *
     * @param player A Player, including their buildings.
     * @param good   A Good type to trade.
     * @return An int result including the base good value, plus buildings and the Trader role.
     */
    public int getValueOfTradeWithPlayer(Player player, Good good) {
        int value = getDoubloonsForGood(good);

        // The Trader role gets +1 on all trades.
        if (player.isActingRole(RoleCardType.TRADER)) {
            value += 1;
        }
        if (player.hasOccupiedBuilding(BuildingType.SMALL_MARKET)) {
            value += 1;
        } else if (player.hasOccupiedBuilding(BuildingType.LARGE_MARKET)) {
            value += 2;
        }
        return value;
    }
}
