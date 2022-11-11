package games.puertorico.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ColonistShipTest {

    private static final int THREE_PLAYER_COLONIST_POOL = 75;
    private static final int FOUR_PLAYER_COLONIST_POOL = 95;
    private static final int FIVE_PLAYER_COLONIST_POOL = 115;

    private ColonistShip threePlayerShip;
    private ColonistShip fourPlayerShip;
    private ColonistShip fivePlayerShip;

    @BeforeEach
    void setUp() {
        threePlayerShip = new ColonistShip(3);
        fourPlayerShip = new ColonistShip(4);
        fivePlayerShip = new ColonistShip(5);
    }

    @Test
    void testColonistShipsInitialLoad() {
        assertEquals(3, threePlayerShip.getColonistsOnShip());
        assertEquals(4, fourPlayerShip.getColonistsOnShip());
        assertEquals(5, fivePlayerShip.getColonistsOnShip());
    }

    @Test
    void testTotalColonistShipPool() {
        assertEquals(THREE_PLAYER_COLONIST_POOL, threePlayerShip.getColonistsInSupply());
        assertEquals(FOUR_PLAYER_COLONIST_POOL, fourPlayerShip.getColonistsInSupply());
        assertEquals(FIVE_PLAYER_COLONIST_POOL, fivePlayerShip.getColonistsInSupply());
    }

    @Test
    void testReloadColonistShipForPlayersWithNoBuildingsIsNumPlayers() {
        RoleCard mayor = new RoleCard(RoleCardType.MAYOR);
        Player p1 = new Player();
        Player p2 = new Player();
        Player p3 = new Player();
        p1.selectRoleCard(mayor);
        Player[] players = new Player[]{p1, p2, p3};
        threePlayerShip.giveColonistsToPlayers(players);
        boolean canReload = threePlayerShip.reloadColonistShipForPlayers(players);
        assertTrue(canReload);
        assertEquals(3, threePlayerShip.getColonistsOnShip());
    }

    @Test
    void testReloadColonistShipForPlayersWithSomeEmptyBuildingSpacesIsCorrect() {
        RoleCard mayor = new RoleCard(RoleCardType.MAYOR);
        Player p1 = new Player();
        Player p2 = new Player();
        Player p3 = new Player();
        p1.selectRoleCard(mayor);
        Player[] players = new Player[]{p1, p2, p3};
        threePlayerShip.giveColonistsToPlayers(players);

        Building indigoPlant = new Building(BuildingType.INDIGO_PLANT);
        indigoPlant.addColonist();
        p1.addBuilding(indigoPlant);
        Building coffeeRoaster = new Building(BuildingType.COFFEE_ROASTER);
        p2.addBuilding(coffeeRoaster);
        boolean canReload = threePlayerShip.reloadColonistShipForPlayers(players);
        assertTrue(canReload);
        assertEquals(4, threePlayerShip.getColonistsOnShip());
    }

    @Test
    void testReloadColonistShipForPlayersReturnsCorrectBoolean() {
        RoleCard mayor = new RoleCard(RoleCardType.MAYOR);
        Player p3 = new Player();
        p3.selectRoleCard(mayor);
        Player[] players = new Player[]{new Player(), new Player(), p3, new Player()};
        fourPlayerShip.giveColonistsToPlayers(players);
        while (fourPlayerShip.getColonistsInSupply() >= 4) {
            assertTrue(fourPlayerShip.reloadColonistShipForPlayers(players));
            fourPlayerShip.giveColonistsToPlayers(players);
        }
        assertFalse(fourPlayerShip.reloadColonistShipForPlayers(players));
    }

    @Test
    void testGiveColonistsToPlayersForThreePlayersAndThreeColonists() {
        RoleCard mayor = new RoleCard(RoleCardType.MAYOR);
        Player p1 = new Player();
        Player p2 = new Player();
        Player p3 = new Player();
        p3.selectRoleCard(mayor);
        Player[] players = new Player[]{p1, p2, p3};
        threePlayerShip.giveColonistsToPlayers(players);
        assertEquals(0, threePlayerShip.getColonistsOnShip());
        assertEquals(THREE_PLAYER_COLONIST_POOL - 1, threePlayerShip.getColonistsInSupply());
        assertEquals(1, p1.getFreeColonists());
        assertEquals(1, p2.getFreeColonists());
        assertEquals(2, p3.getFreeColonists());
    }

    @Test
    void testGiveColonistsToPlayersForFourPlayersAndSixColonists() {
        RoleCard mayor = new RoleCard(RoleCardType.MAYOR);
        Player p1 = new Player();
        Player p2 = new Player();
        Player p3 = new Player();
        Player p4 = new Player();
        p2.selectRoleCard(mayor);
        p1.addBuilding(new Building(BuildingType.SUGAR_MILL));
        p4.addBuilding(new Building(BuildingType.TOBACCO_STORAGE));
        Player[] players = new Player[]{p1, p2, p3, p4};
        fourPlayerShip.reloadColonistShipForPlayers(players);
        assertEquals(6, fourPlayerShip.getColonistsOnShip());

        fourPlayerShip.giveColonistsToPlayers(players);
        assertEquals(0, fourPlayerShip.getColonistsOnShip());
        assertEquals(FOUR_PLAYER_COLONIST_POOL - 7, fourPlayerShip.getColonistsInSupply());
        assertEquals(1, p1.getFreeColonists());
        assertEquals(3, p2.getFreeColonists());
        assertEquals(2, p3.getFreeColonists());
        assertEquals(1, p4.getFreeColonists());
    }
}
