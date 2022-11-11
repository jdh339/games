package games.newpuertorico.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player();
    }

    @Test
    void testGetQuantityOfGood() {
        for (Good good : Good.values()) {
            assertEquals(0, player.getQuantityOfGood(good));
        }
        player.addGoods(Good.SUGAR, 3);
        assertEquals(3, player.getQuantityOfGood(Good.SUGAR));
        player.removeGoods(Good.SUGAR, 2);
        assertEquals(1, player.getQuantityOfGood(Good.SUGAR));
    }

    @Test
    void testHasGood() {
        for (Good good : Good.values()) {
            assertFalse(player.hasGood(good));
        }
        player.addGoods(Good.SUGAR, 3);
        assertTrue(player.hasGood(Good.SUGAR));
    }

    @Test
    void testGetDoubloons() {
        assertEquals(0, player.getDoubloons());
        player.addDoubloons(5);
        assertEquals(5, player.getDoubloons());
        player.removeDoubloons(3);
        assertEquals(2, player.getDoubloons());
    }

    @Test
    void testHasBuilding() {
        Building indigoPlant = new Building(BuildingType.INDIGO_PLANT);
        assertFalse(player.hasBuilding(BuildingType.INDIGO_PLANT));
        player.addBuilding(indigoPlant);
        assertTrue(player.hasBuilding(BuildingType.INDIGO_PLANT));
    }

    @Test
    void testHasOccupiedBuilding() {
        Building hospice = new Building(BuildingType.HOSPICE);
        player.addBuilding(hospice);
        assertFalse(player.hasOccupiedBuilding(BuildingType.HOSPICE));
        hospice.addColonist();
        assertTrue(player.hasOccupiedBuilding(BuildingType.HOSPICE));
    }

    @Test
    void testIsActingRole() {
        RoleCard mayor = new RoleCard(RoleCardType.MAYOR);
        assertFalse(player.isActingRole(RoleCardType.MAYOR));
        player.selectRoleCard(mayor);
        assertTrue(player.isActingRole(RoleCardType.MAYOR));
        assertFalse(player.isActingRole(RoleCardType.SETTLER));
    }

    @Test
    void testSelectingARoleCardAddsDoubloons() {
        RoleCard mayor = new RoleCard(RoleCardType.MAYOR);
        mayor.addDoubloonIfUnclaimed();
        player.selectRoleCard(mayor);
        assertEquals(1, player.getDoubloons());
    }

    @Test
    void testCannotSelectRoleCardIfAlreadyHasOne() {
        RoleCard settler = new RoleCard(RoleCardType.SETTLER);
        RoleCard mayor = new RoleCard(RoleCardType.MAYOR);
        player.selectRoleCard(settler);
        assertFalse(player.canClaimRoleCard(mayor));
    }

    @Test
    void testCanSelectRoleCard() {
        RoleCard mayor = new RoleCard(RoleCardType.MAYOR);
        assertTrue(player.canClaimRoleCard(mayor));
    }

    @Test
    void testCannotSelectRoleCardIfAlreadyClaimed() {
        RoleCard mayor = new RoleCard(RoleCardType.MAYOR);
        mayor.claim();
        assertFalse(player.canClaimRoleCard(mayor));
    }

    @Test
    void testGetEmptyColonistSpacesInBuildingsWithNoBuildings() {
        assertEquals(0, player.getEmptyColonistSpacesInBuildings());
    }

    @Test
    void testGetEmptyColonistSpacesInBuildingsWithSomeEmptySpaces() {
        Building smallMarket = new Building(BuildingType.SMALL_MARKET);
        smallMarket.addColonist();
        Building sugarMill = new Building(BuildingType.SUGAR_MILL);
        sugarMill.addColonist();
        Building tobaccoStorage = new Building(BuildingType.TOBACCO_STORAGE);
        player.addBuilding(smallMarket);
        player.addBuilding(sugarMill);
        player.addBuilding(tobaccoStorage);
        assertEquals(5, player.getEmptyColonistSpacesInBuildings());
    }
}