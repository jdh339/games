package games.newpuertorico.model;


import java.util.Arrays;
import java.util.Random;

/**
 * The PlantationInventory maintains the deck of Plantations and quarries.
 *
 * Any time players get Plantations during the Settler Phase, they will draw them from this class.
 * The class maintains an internal collection of quarries, plus face-down, face-up, and discarded
 * Plantations (which may be recycled if the face-down pile is exhausted).
 */
public class PlantationInventory {

    private final int[] faceDownPlantations = new int[]{0, 10, 10, 10, 10, 10};
    private final int[] faceUpPlantations = new int[]{8, 0, 0, 0, 0, 0};
    private final int[] discardedPlantations = new int[]{0, 0, 0, 0, 0, 0};
    private final Random random;
    private int numPlayers;

    public PlantationInventory() {
        random = new Random();
    }

    /**
     * Gives one initial Plantation to all the players, as part of game setup.
     * @param players An array of Players, where the first player is the first Governor.
     */
    public void giveInitialPlantationsToPlayers(Player[] players) {
        numPlayers = players.length;

        // First two players always get Indigo
        players[0].addPlantation(getPlantationFromFaceDownPile(PlantationType.INDIGO));
        players[1].addPlantation(getPlantationFromFaceDownPile(PlantationType.INDIGO));
        if (numPlayers == 3) {
            // In a three player game, the 3rd player gets corn.
            players[2].addPlantation(getPlantationFromFaceDownPile(PlantationType.CORN));
        }
        if (numPlayers == 4) {
            // In a four player game, the 3rd and 4th players gets corn.
            players[2].addPlantation(getPlantationFromFaceDownPile(PlantationType.CORN));
            players[3].addPlantation(getPlantationFromFaceDownPile(PlantationType.CORN));
        }
        if (numPlayers == 5) {
            // In a five player game, the 3rd player gets indigo and the 4th and 5th get corn.
            players[2].addPlantation(getPlantationFromFaceDownPile(PlantationType.INDIGO));
            players[3].addPlantation(getPlantationFromFaceDownPile(PlantationType.CORN));
            players[4].addPlantation(getPlantationFromFaceDownPile(PlantationType.CORN));
        }
    }

    /**
     * Returns a Plantation object of the given type from the face-down pile.
     * @param plantationType A PlantationType to remove from the face-down pile.
     * @return A new Plantation object.
     */
    private Plantation getPlantationFromFaceDownPile(PlantationType plantationType) {
        faceDownPlantations[plantationType.ordinal()] -= 1;
        return new Plantation(plantationType);
    }

    /**
     * Lays out random plantations face up. There will be one more than the number of players.
     */
    public void layOutPlantations() {
        for (int i = 0; i <= numPlayers; i++) {
            PlantationType randomType = getRandomPlantationTypeFromFaceDownPile();
            faceDownPlantations[randomType.ordinal()] -= 1;
            faceUpPlantations[randomType.ordinal()] += 1;
        }
    }

    /**
     * Randomly selects a plantation type from the faceDownPlantations.
     *
     * @return A PlantationType. Never returns a quarry since those aren't "face down".
     */
    public PlantationType getRandomPlantationTypeFromFaceDownPile() {
        int total = Arrays.stream(faceDownPlantations).sum();
        // TODO - handle the case where plantations are low or empty.

        // randomInt will be a value in the interval [0,total).
        // A value of 0 indicates "pick the first available from corn, indigo, sugar, etc."
        // Note that there are always 0 quarries in the faceDownPlantations.
        int randomInt = random.nextInt(total);
        int cumulativeSum = 0;
        for (int i = 0; i < faceDownPlantations.length; i++) {
            cumulativeSum += faceDownPlantations[i];
            if (randomInt < cumulativeSum) {
                // Pick the type of plantation indicated by i.
                return PlantationType.values()[i];
            }
        }
        return PlantationType.COFFEE;
    }

    /**
     * Returns info about of the available face up plantations.
     * @return An int array of length 6, counting the plantation types available to draw.
     */
    public int[] getAvailableFaceUpPlantations() {
        return faceUpPlantations.clone();  // Clone so the caller can't modify the face up list.
    }

    public boolean playerCanSelectPlantation(Player player, PlantationType plantationType) {
        if (faceUpPlantations[plantationType.ordinal()] <= 0) {
            return false;
        }
        if (plantationType.equals(PlantationType.QUARRY)) {
            return player.isActingRole(RoleCardType.SETTLER) ||
                    player.hasOccupiedBuilding(BuildingType.CONSTRUCTION_HUT);
        } else {
            return true;
        }
    }

    public void givePlantationToPlayer(Player player, PlantationType plantationType) {
        Plantation plantation = new Plantation(plantationType);
        faceUpPlantations[plantationType.ordinal()] -= 1;
        player.addPlantation(plantation);
    }
}
