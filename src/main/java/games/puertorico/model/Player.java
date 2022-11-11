package games.puertorico.model;

import java.util.ArrayList;

public class Player {

    private final int[] goods = new int[5];
    private final ArrayList<Building> buildings = new ArrayList<>();
    private final ArrayList<Plantation> plantations = new ArrayList<>();

    private int doubloons = 0;
    private int freeColonists = 0;
    private RoleCard roleCard;


    public int getQuantityOfGood(Good good) {
        return goods[good.ordinal()];
    }

    public void addGoods(Good good, int quantity) {
        goods[good.ordinal()] += quantity;
    }

    public void removeGoods(Good good, int quantity) {
        goods[good.ordinal()] -= quantity;
    }

    public boolean hasGood(Good good) {
        return getQuantityOfGood(good) > 0;
    }

    public int getDoubloons() {
        return doubloons;
    }

    public void addDoubloons(int quantity) {
        doubloons += quantity;
    }

    public void removeDoubloons(int quantity) {
        doubloons -= quantity;
    }

    public void addBuilding(Building building) {
        buildings.add(building);
    }

    public boolean hasBuilding(BuildingType buildingType) {
        return buildings.stream().anyMatch(b -> b.isType(buildingType));
    }

    public boolean hasOccupiedBuilding(BuildingType buildingType) {
        return buildings.stream().anyMatch(b -> b.isType(buildingType) && b.isOccupied());
    }

    public boolean isActingRole(RoleCardType roleCardType) {
        return roleCard != null && roleCard.isType(roleCardType);
    }

    public void selectRoleCard(RoleCard roleCard) {
        this.roleCard = roleCard;
        addDoubloons(roleCard.getDoubloons());
        roleCard.claim();
    }

    /**
     * Returns whether the Player can take the given role card.
     *
     * @param roleCard A role card in the game.
     * @return A boolean, true if the Player doesn't have a role yet and the card is unclaimed.
     */
    public boolean canClaimRoleCard(RoleCard roleCard) {
        return !roleCard.isClaimed() && this.roleCard == null;
    }

    /**
     * Returns the total number of empty colonist spaces in this Player's buildings.
     *
     * @return An int which can help for re-loading the colonist ship.
     */
    public int getEmptyColonistSpacesInBuildings() {
        return buildings.stream().mapToInt(Building::getEmptyColonistSpaces).sum();
    }

    public int getFreeColonists() {
        return freeColonists;
    }

    public void addFreeColonist() {
        freeColonists += 1;
    }

    public int getBuildingSpacesLeft() {
        final int totalSpaces = 12;
        int spacesUsed = buildings.stream().mapToInt(Building::getSpacesRequired).sum();
        return totalSpaces - spacesUsed;
    }

    public void addPlantation(Plantation plantation) {
        plantations.add(plantation);
    }

    public int countOccupiedPlantationsOfType(PlantationType plantationType) {
        return countPlantations(false)[plantationType.ordinal()];
    }

    public int countPlantationsOfType(PlantationType plantationType) {
        return countPlantations(true)[plantationType.ordinal()];
    }

    private int[] countPlantations(boolean includeUnoccupiedPlantations) {
        int[] occupiedPlantationsCount = new int[PlantationType.values().length];
        for (Plantation plantation : this.plantations) {
            if (plantation.isOccupied() || includeUnoccupiedPlantations) {
                occupiedPlantationsCount[plantation.getType().ordinal()] += 1;
            }
        }
        return occupiedPlantationsCount;
    }
}
