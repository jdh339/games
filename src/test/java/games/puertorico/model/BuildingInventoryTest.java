package games.puertorico.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BuildingInventoryTest {
    private BuildingInventory buildingInventory;

    @BeforeEach
    void setUp() {
        buildingInventory = new BuildingInventory();
    }

    @Test
    void testPlayerCannotPurchaseBuildingIfBroke() {
        Player player = new Player();
        assertFalse(buildingInventory.playerCanPurchaseBuilding(player, BuildingType.SMALL_MARKET));
    }

    @Test
    void testPlayerCannotPurchaseBuildingIfPlayerAlreadyHasThatBuilding() {
        Player player = new Player();
        player.addDoubloons(100);
        player.addBuilding(new Building(BuildingType.SUGAR_MILL));
        assertFalse(buildingInventory.playerCanPurchaseBuilding(player, BuildingType.SUGAR_MILL));
    }

    @Test
    void testPlayerCannotPurchaseBuildingIfNoBuildingsRemain() {
        Player p1 = new Player();
        Player p2 = new Player();
        p1.addDoubloons(100);
        p2.addDoubloons(100);
        buildingInventory.sellBuildingToPlayer(p1, BuildingType.CITY_HALL);
        assertFalse(buildingInventory.playerCanPurchaseBuilding(p2, BuildingType.CITY_HALL));
    }

    @Test
    void testPlayerCannotPurchaseBuildingIfTheirIslandIsFull() {
        Player player = new Player();
        player.addDoubloons(100);
        player.addBuilding(new Building(BuildingType.CITY_HALL));
        player.addBuilding(new Building(BuildingType.FORTRESS));
        player.addBuilding(new Building(BuildingType.CUSTOMS_HOUSE));
        player.addBuilding(new Building(BuildingType.RESIDENCE));
        player.addBuilding(new Building(BuildingType.SMALL_SUGAR_MILL));
        player.addBuilding(new Building(BuildingType.SMALL_INDIGO_PLANT));
        player.addBuilding(new Building(BuildingType.SMALL_MARKET));
        player.addBuilding(new Building(BuildingType.SMALL_WAREHOUSE));
        assertFalse(buildingInventory.playerCanPurchaseBuilding(player, BuildingType.HOSPICE));
    }

    @Test
    void testPlayerCanPurchaseBuildingIfTheyHaveEnoughDoubloons() {
        Player player = new Player();
        player.addDoubloons(1);
        assertTrue(buildingInventory.playerCanPurchaseBuilding(player, BuildingType.SMALL_MARKET));
    }

    @Test
    void testPlayerCanPurchaseBuildingIfTheyDoNotHaveEnoughDoubloonsButAreTheBuilder() {
        RoleCard builder = new RoleCard(RoleCardType.BUILDER);
        Player player = new Player();
        player.addDoubloons(3);
        assertFalse(buildingInventory.playerCanPurchaseBuilding(player, BuildingType.HOSPICE));
        player.selectRoleCard(builder);
        assertTrue(buildingInventory.playerCanPurchaseBuilding(player, BuildingType.HOSPICE));
    }

    @Test
    void testGetPriceForPlayerToPurchaseBuildingReturnsBaseCost() {
        Player player = new Player();
        for (BuildingType buildingType : BuildingType.values()) {
            int baseCost = Building.getBaseCostForBuildingType(buildingType);
            assertEquals(baseCost, buildingInventory.getPriceForPlayerToPurchaseBuilding(player,
                    buildingType));
        }
    }

    @Test
    void testGetPriceForPlayerToPurchaseBuildingIsOneLessIfTheyAreBuilder() {
        RoleCard builder = new RoleCard(RoleCardType.BUILDER);
        Player player = new Player();
        player.selectRoleCard(builder);
        for (BuildingType buildingType : BuildingType.values()) {
            int expected = Building.getBaseCostForBuildingType(buildingType) - 1;
            assertEquals(expected, buildingInventory.getPriceForPlayerToPurchaseBuilding(player,
                    buildingType));
        }
    }

    @Test
    void testGetPriceForPlayerToPurchaseBuildingIsBaseCostIfTheyHaveAnUnoccupiedQuarry() {
        Player player = new Player();
        Plantation quarry = new Plantation(PlantationType.QUARRY);
        player.addPlantation(quarry);
        for (BuildingType buildingType : BuildingType.values()) {
            int expected = Building.getBaseCostForBuildingType(buildingType);
            assertEquals(expected, buildingInventory.getPriceForPlayerToPurchaseBuilding(player,
                    buildingType));
        }
    }

    @Test
    void testGetPriceForPlayerToPurchaseBuildingIsOneLessIfTheyHaveAnOccupiedQuarry() {
        Player player = new Player();
        Plantation quarry = new Plantation(PlantationType.QUARRY);
        quarry.addColonist();
        player.addPlantation(quarry);
        for (BuildingType buildingType : BuildingType.values()) {
            int expected = Building.getBaseCostForBuildingType(buildingType) - 1;
            assertEquals(expected, buildingInventory.getPriceForPlayerToPurchaseBuilding(player,
                    buildingType));
        }
    }

    @Test
    void testGetPriceForPlayerToPurchaseBuildingIsDiscountedByQuarriesWithBuildingColumn() {
        Player player = new Player();
        Plantation q1 = new Plantation(PlantationType.QUARRY);
        Plantation q2 = new Plantation(PlantationType.QUARRY);
        Plantation q3 = new Plantation(PlantationType.QUARRY);
        q1.addColonist();
        q2.addColonist();
        q3.addColonist();
        player.addPlantation(q1);
        player.addPlantation(q2);
        player.addPlantation(q3);

        assertEquals(0, buildingInventory.getPriceForPlayerToPurchaseBuilding(player,
                BuildingType.SMALL_MARKET));
        assertEquals(2, buildingInventory.getPriceForPlayerToPurchaseBuilding(player,
                BuildingType.HOSPICE));
        assertEquals(3, buildingInventory.getPriceForPlayerToPurchaseBuilding(player,
                BuildingType.COFFEE_ROASTER));
    }

    @Test
    void testNoBuildingMayBePurchasedForNegativeDoubloons() {
        Player player = new Player();
        RoleCard builder = new RoleCard(RoleCardType.BUILDER);
        player.selectRoleCard(builder);
        Plantation quarry = new Plantation(PlantationType.QUARRY);
        quarry.addColonist();
        player.addPlantation(quarry);
        assertEquals(0, buildingInventory.getPriceForPlayerToPurchaseBuilding(player,
                BuildingType.SMALL_MARKET));
    }

    @Test
    void testPlayerPurchasesBuildingAndItDisappears() {
        Player p1 = new Player();
        Player p2 = new Player();
        p1.addDoubloons(10);
        p2.addDoubloons(10);
        assertTrue(buildingInventory.playerCanPurchaseBuilding(p1, BuildingType.GUILD_HALL));
        buildingInventory.sellBuildingToPlayer(p1, BuildingType.GUILD_HALL);
        assertFalse(buildingInventory.playerCanPurchaseBuilding(p2, BuildingType.GUILD_HALL));
    }

    @Test
    void testPlayerPurchasesBuildingAndLosesDoubloons() {
        Player player = new Player();
        player.addDoubloons(10);
        buildingInventory.sellBuildingToPlayer(player, BuildingType.GUILD_HALL);
        assertEquals(0, player.getDoubloons());
    }

    @Test
    void testPlayerPurchasesBuildingAndItAffectsTheirBuildingList() {
        Player player = new Player();
        player.addDoubloons(20);
        buildingInventory.sellBuildingToPlayer(player, BuildingType.GUILD_HALL);
        assertFalse(player.hasOccupiedBuilding(BuildingType.GUILD_HALL));
        assertTrue(player.hasBuilding(BuildingType.GUILD_HALL));
        assertEquals(1, player.getEmptyColonistSpacesInBuildings());
        assertEquals(10, player.getBuildingSpacesLeft());
    }
}
