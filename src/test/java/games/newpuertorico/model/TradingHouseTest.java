package games.newpuertorico.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TradingHouseTest {
    private TradingHouse tradingHouse;
    private Player player;

    @BeforeEach
    void setUp() {
        this.tradingHouse = new TradingHouse();
        this.player = new Player();
    }

    @Test
    void testGetDoubloonsForGood() {
        assertEquals(0, tradingHouse.getDoubloonsForGood(Good.CORN));
        assertEquals(1, tradingHouse.getDoubloonsForGood(Good.INDIGO));
        assertEquals(2, tradingHouse.getDoubloonsForGood(Good.SUGAR));
        assertEquals(3, tradingHouse.getDoubloonsForGood(Good.TOBACCO));
        assertEquals(4, tradingHouse.getDoubloonsForGood(Good.COFFEE));
    }

    @Test
    void testPlayerCanTradeGoodWithEmptyTradingHouse() {
        player.addGoods(Good.CORN, 1);
        assertTrue(tradingHouse.playerCanTradeGood(player, Good.CORN));
    }

    @Test
    void testPlayerCannotTradeGoodWithTradingHouseContainsGood() {
        player.addGoods(Good.CORN, 2);
        tradingHouse.tradeGoodWithPlayer(player, Good.CORN);
        assertFalse(tradingHouse.playerCanTradeGood(player, Good.CORN));
    }

    @Test
    void testPlayerCannotTradeGoodTheyDoNotOwn() {
        assertFalse(tradingHouse.playerCanTradeGood(player, Good.CORN));
    }

    @Test
    void testPlayerCanTradeGoodsOfDifferentType() {
        player.addGoods(Good.CORN, 1);
        player.addGoods(Good.INDIGO, 1);
        tradingHouse.tradeGoodWithPlayer(player, Good.CORN);
        assertTrue(tradingHouse.playerCanTradeGood(player, Good.INDIGO));
    }

    @Test
    void testPlayerCannotTradeGoodsWithFullTradingHouse() {
        player.addGoods(Good.CORN, 1);
        player.addGoods(Good.INDIGO, 1);
        player.addGoods(Good.SUGAR, 1);
        player.addGoods(Good.TOBACCO, 1);
        player.addGoods(Good.COFFEE, 1);
        tradingHouse.tradeGoodWithPlayer(player, Good.CORN);
        tradingHouse.tradeGoodWithPlayer(player, Good.INDIGO);
        tradingHouse.tradeGoodWithPlayer(player, Good.SUGAR);
        tradingHouse.tradeGoodWithPlayer(player, Good.TOBACCO);
        assertFalse(tradingHouse.playerCanTradeGood(player, Good.COFFEE));
    }

    @Test
    void testPlayerWithOccupiedOfficeCanTradeAnExistingGood() {
        player.addGoods(Good.SUGAR, 2);
        Building office = new Building(BuildingType.OFFICE);
        office.addColonist();
        player.addBuilding(office);
        tradingHouse.tradeGoodWithPlayer(player, Good.SUGAR);
        assertTrue(tradingHouse.playerCanTradeGood(player, Good.SUGAR));
    }

    @Test
    void testTradingHouseCanBeClearedIfFull() {
        player.addGoods(Good.CORN, 2);
        player.addGoods(Good.INDIGO, 2);
        player.addGoods(Good.SUGAR, 2);
        player.addGoods(Good.TOBACCO, 2);
        tradingHouse.tradeGoodWithPlayer(player, Good.CORN);
        tradingHouse.tradeGoodWithPlayer(player, Good.INDIGO);
        tradingHouse.tradeGoodWithPlayer(player, Good.SUGAR);
        tradingHouse.tradeGoodWithPlayer(player, Good.TOBACCO);
        tradingHouse.clearIfFull();
        assertTrue(tradingHouse.playerCanTradeGood(player, Good.CORN));
        assertTrue(tradingHouse.playerCanTradeGood(player, Good.INDIGO));
        assertTrue(tradingHouse.playerCanTradeGood(player, Good.SUGAR));
        assertTrue(tradingHouse.playerCanTradeGood(player, Good.TOBACCO));
    }

    @Test
    void testTradingHouseWillNotBeClearedIfNotFull() {
        player.addGoods(Good.CORN, 2);
        tradingHouse.tradeGoodWithPlayer(player, Good.CORN);
        tradingHouse.clearIfFull();
        assertFalse(tradingHouse.playerCanTradeGood(player, Good.CORN));
    }

    @Test
    void testTradeGoodsWithPlayerWhoHasNoBuildingsAwardsBaseDoubloons() {
        Good corn = Good.CORN;
        player.addGoods(corn, 1);
        tradingHouse.tradeGoodWithPlayer(player, corn);
        int expected = tradingHouse.getDoubloonsForGood(corn);
        assertEquals(expected, player.getDoubloons());
    }

    @Test
    void testGetValueOfTradeWithPlayerWithOccupiedSmallMarket() {
        Good corn = Good.CORN;
        player.addGoods(corn, 1);
        Building smallMarket = new Building(BuildingType.SMALL_MARKET);
        smallMarket.addColonist();
        player.addBuilding(smallMarket);
        int expected = tradingHouse.getDoubloonsForGood(corn) + 1;
        assertEquals(expected, tradingHouse.getValueOfTradeWithPlayer(player, corn));
    }

    @Test
    void testGetValueOfTradeWithPlayerWithUnoccupiedSmallMarket() {
        Good corn = Good.CORN;
        player.addGoods(corn, 1);
        Building smallMarket = new Building(BuildingType.SMALL_MARKET);
        player.addBuilding(smallMarket);
        int expected = tradingHouse.getDoubloonsForGood(corn);
        assertEquals(expected, tradingHouse.getValueOfTradeWithPlayer(player, corn));
    }

    @Test
    void testGetValueOfTradeWithPlayerWithOccupiedLargeMarket() {
        Good sugar = Good.SUGAR;
        player.addGoods(sugar, 1);
        Building largeMarket = new Building(BuildingType.LARGE_MARKET);
        largeMarket.addColonist();
        player.addBuilding(largeMarket);
        int expected = tradingHouse.getDoubloonsForGood(sugar) + 2;
        assertEquals(expected, tradingHouse.getValueOfTradeWithPlayer(player, sugar));
    }

    @Test
    void testGetValueOfTradeWithPlayerWhoIsTheTrader() {
        Good sugar = Good.SUGAR;
        RoleCard trader = new RoleCard(RoleCardType.TRADER);
        player.selectRoleCard(trader);
        player.addGoods(sugar, 1);
        int expected = tradingHouse.getDoubloonsForGood(sugar) + 1;
        assertEquals(expected, tradingHouse.getValueOfTradeWithPlayer(player, sugar));
    }
}
