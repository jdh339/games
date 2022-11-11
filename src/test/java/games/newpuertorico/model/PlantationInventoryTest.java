package games.newpuertorico.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlantationInventoryTest {
    private PlantationInventory plantationInventory;

    @BeforeEach
    void setUp() {
        plantationInventory = new PlantationInventory();
    }

    @Test
    void testPlantationInventoryGivesInitialPlantationsForThreePlayers() {
        Player p1 = new Player();
        Player p2 = new Player();
        Player p3 = new Player();
        Player[] players = new Player[]{p1, p2, p3};
        plantationInventory.giveInitialPlantationsToPlayers(players);
        assertEquals(1, p1.countPlantationsOfType(PlantationType.INDIGO));
        assertEquals(1, p2.countPlantationsOfType(PlantationType.INDIGO));
        assertEquals(1, p3.countPlantationsOfType(PlantationType.CORN));
    }

    @Test
    void testPlantationInventoryGivesInitialPlantationsForFourPlayers() {
        Player p1 = new Player();
        Player p2 = new Player();
        Player p3 = new Player();
        Player p4 = new Player();
        Player[] players = new Player[]{p1, p2, p3, p4};
        plantationInventory.giveInitialPlantationsToPlayers(players);
        assertEquals(1, p1.countPlantationsOfType(PlantationType.INDIGO));
        assertEquals(1, p2.countPlantationsOfType(PlantationType.INDIGO));
        assertEquals(1, p3.countPlantationsOfType(PlantationType.CORN));
        assertEquals(1, p4.countPlantationsOfType(PlantationType.CORN));
    }

    @Test
    void testPlantationInventoryGivesInitialPlantationsForFivePlayers() {
        Player p1 = new Player();
        Player p2 = new Player();
        Player p3 = new Player();
        Player p4 = new Player();
        Player p5 = new Player();
        Player[] players = new Player[]{p1, p2, p3, p4, p5};
        plantationInventory.giveInitialPlantationsToPlayers(players);
        assertEquals(1, p1.countPlantationsOfType(PlantationType.INDIGO));
        assertEquals(1, p2.countPlantationsOfType(PlantationType.INDIGO));
        assertEquals(1, p3.countPlantationsOfType(PlantationType.INDIGO));
        assertEquals(1, p4.countPlantationsOfType(PlantationType.CORN));
        assertEquals(1, p5.countPlantationsOfType(PlantationType.CORN));
    }

    @Test
    void testPlantationInventoryStartsWithCorrectNumberOfQuarries() {
        Player[] players = new Player[]{new Player(), new Player(), new Player(), new Player()};
        plantationInventory.giveInitialPlantationsToPlayers(players);
        plantationInventory.layOutPlantations();
        int[] plantationsAvailable = plantationInventory.getAvailableFaceUpPlantations();
        assertEquals(8, plantationsAvailable[PlantationType.QUARRY.ordinal()]);
    }

    @Test
    void testPlantationInventoryStartsWithOneMoreThanTheNumberOfPlayers() {
        Player[] players = new Player[]{new Player(), new Player(), new Player(), new Player()};
        plantationInventory.giveInitialPlantationsToPlayers(players);
        plantationInventory.layOutPlantations();
        int[] plantationsAvailable = plantationInventory.getAvailableFaceUpPlantations();
        int totalPlantations = 0;
        for (PlantationType plantationType : PlantationType.values()) {
            if (plantationType.equals(PlantationType.QUARRY)) {
                continue;
            }
            totalPlantations += plantationsAvailable[plantationType.ordinal()];
        }
        assertEquals(players.length + 1, totalPlantations);
    }


    @Test
    void testPlayerCanPickPlantationWhenPlantationIsThere() {
        Player p = new Player();
        Player[] players = new Player[]{p, new Player(), new Player(), new Player(), new Player()};
        plantationInventory.giveInitialPlantationsToPlayers(players);
        plantationInventory.layOutPlantations();
        int[] plantationsAvailable = plantationInventory.getAvailableFaceUpPlantations();
        PlantationType availableType = null;
        for (int i = 1; i < plantationsAvailable.length; i++) {
            if (plantationsAvailable[i] > 0) {
                availableType = PlantationType.values()[i];
                break;
            }
        }
        assertNotNull(availableType);
        assertTrue(plantationInventory.playerCanSelectPlantation(p, availableType));
    }

    @Test
    void testPlayerCannotPickPlantationWhenPlantationIsNotThere() {
        Player player = new Player();
        Player[] players = new Player[]{player, new Player(), new Player()};
        plantationInventory.giveInitialPlantationsToPlayers(players);
        plantationInventory.layOutPlantations();
        int[] plantationsAvailable = plantationInventory.getAvailableFaceUpPlantations();
        PlantationType unavailableType = null;
        for (int i = 1; i < plantationsAvailable.length; i++) {
            if (plantationsAvailable[i] == 0) {
                unavailableType = PlantationType.values()[i];
                break;
            }
        }
        assertNotNull(unavailableType);
        assertFalse(plantationInventory.playerCanSelectPlantation(player, unavailableType));
    }

    @Test
    void testPlayerCannotPickQuarryIfNotTheSettler() {
        Player player = new Player();
        Player[] players = new Player[]{player, new Player(), new Player()};
        plantationInventory.giveInitialPlantationsToPlayers(players);
        plantationInventory.layOutPlantations();
        assertFalse(plantationInventory.playerCanSelectPlantation(player, PlantationType.QUARRY));
    }

    @Test
    void testPlayerCanPickQuarryIfTheyAreTheSettler() {
        Player player = new Player();
        RoleCard settler = new RoleCard(RoleCardType.SETTLER);
        player.selectRoleCard(settler);
        Player[] players = new Player[]{new Player(), player, new Player()};
        plantationInventory.giveInitialPlantationsToPlayers(players);
        plantationInventory.layOutPlantations();
        assertTrue(plantationInventory.playerCanSelectPlantation(player, PlantationType.QUARRY));
    }

    @Test
    void testPlayerCanPickQuarryIfTheyHaveAnOccupiedConstructionHut() {
        Player player = new Player();
        Building constructionHut = new Building(BuildingType.CONSTRUCTION_HUT);
        constructionHut.addColonist();
        player.addBuilding(constructionHut);
        Player[] players = new Player[]{new Player(), player, new Player()};
        plantationInventory.giveInitialPlantationsToPlayers(players);
        plantationInventory.layOutPlantations();
        assertTrue(plantationInventory.playerCanSelectPlantation(player, PlantationType.QUARRY));
    }

    @Test
    void testPlayerCannotPickQuarryIfThereAreNoQuarriesLeft() {
        Player player = new Player();
        RoleCard settler = new RoleCard(RoleCardType.SETTLER);
        player.selectRoleCard(settler);
        Player[] players = new Player[]{player, new Player(), new Player()};
        plantationInventory.giveInitialPlantationsToPlayers(players);
        plantationInventory.layOutPlantations();
        plantationInventory.givePlantationToPlayer(player, PlantationType.QUARRY);
        plantationInventory.givePlantationToPlayer(player, PlantationType.QUARRY);
        plantationInventory.givePlantationToPlayer(player, PlantationType.QUARRY);
        plantationInventory.givePlantationToPlayer(player, PlantationType.QUARRY);
        plantationInventory.givePlantationToPlayer(player, PlantationType.QUARRY);
        plantationInventory.givePlantationToPlayer(player, PlantationType.QUARRY);
        plantationInventory.givePlantationToPlayer(player, PlantationType.QUARRY);
        assertTrue(plantationInventory.playerCanSelectPlantation(player, PlantationType.QUARRY));
        plantationInventory.givePlantationToPlayer(player, PlantationType.QUARRY);
        assertFalse(plantationInventory.playerCanSelectPlantation(player, PlantationType.QUARRY));
    }

    @Test
    void testGivePlantationToPlayerRemovesAQuarry() {
        Player player = new Player();
        RoleCard settler = new RoleCard(RoleCardType.SETTLER);
        player.selectRoleCard(settler);
        Player[] players = new Player[]{player, new Player(), new Player()};
        plantationInventory.giveInitialPlantationsToPlayers(players);
        plantationInventory.layOutPlantations();
        int[] availablePlantations = plantationInventory.getAvailableFaceUpPlantations();
        assertEquals(8, availablePlantations[PlantationType.QUARRY.ordinal()]);
        plantationInventory.givePlantationToPlayer(player, PlantationType.QUARRY);
        availablePlantations = plantationInventory.getAvailableFaceUpPlantations();
        assertEquals(7, availablePlantations[PlantationType.QUARRY.ordinal()]);
    }

    @Test
    void testGivePlantationToPlayerRemovesAFaceUpPlantation() {
        Player player = new Player();
        Player[] players = new Player[]{player, new Player(), new Player()};
        plantationInventory.giveInitialPlantationsToPlayers(players);
        plantationInventory.layOutPlantations();
        int[] plantationsAvailable = plantationInventory.getAvailableFaceUpPlantations();
        PlantationType availableType = null;
        int availableNum = 0;
        for (int i = 1; i < plantationsAvailable.length; i++) {
            if (plantationsAvailable[i] > 0) {
                availableType = PlantationType.values()[i];
                availableNum = plantationsAvailable[i];
                break;
            }
        }
        assertNotNull(availableType);
        plantationInventory.givePlantationToPlayer(player, availableType);
        plantationsAvailable = plantationInventory.getAvailableFaceUpPlantations();
        assertEquals(availableNum - 1, plantationsAvailable[availableType.ordinal()]);
    }

    @Test
    void testGivePlantationToPlayerGivesThemAPlantation() {
        Player player = new Player();
        Player[] players = new Player[]{player, new Player(), new Player()};
        plantationInventory.giveInitialPlantationsToPlayers(players);
        plantationInventory.layOutPlantations();
        int[] plantationsAvailable = plantationInventory.getAvailableFaceUpPlantations();
        PlantationType availableType = null;
        for (int i = 1; i < plantationsAvailable.length; i++) {
            if (plantationsAvailable[i] > 0) {
                availableType = PlantationType.values()[i];
                break;
            }
        }
        assertNotNull(availableType);
        int expected = player.countPlantationsOfType(availableType) + 1;
        plantationInventory.givePlantationToPlayer(player, availableType);
        assertEquals(expected, player.countPlantationsOfType(availableType));
    }

    @Test
    void testPlantationInventoryDiscardsUnpickedPlantations() {

    }

    @Test
    void testPlantationInventoryRecyclesDiscardedPlantations() {

    }

    @Test
    void testPlayerCanPickRandomPlantation() {

    }

    @Test
    void testGiveRandomPlantationToPlayer() {

    }

    @Test
    void testGivePlantationToPlayer() {

    }
}
