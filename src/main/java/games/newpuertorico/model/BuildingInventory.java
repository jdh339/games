package games.newpuertorico.model;

public class BuildingInventory {

    // We don't create the Building objects until they're needed.
    // Instead, just keep a counter of the number of available Buildings, indexed by type.
    private final int[] inventory = new int[BuildingType.values().length];

    public BuildingInventory() {
        // Initialize the inventory.
        for (BuildingType buildingType : BuildingType.values()) {
            switch (buildingType) {
                case SMALL_INDIGO_PLANT:
                case SMALL_SUGAR_MILL:
                    inventory[buildingType.ordinal()] = 4;
                    break;
                case INDIGO_PLANT:
                case SUGAR_MILL:
                case TOBACCO_STORAGE:
                case COFFEE_ROASTER:
                    inventory[buildingType.ordinal()] = 3;
                    break;
                case CITY_HALL:
                case GUILD_HALL:
                case FORTRESS:
                case RESIDENCE:
                case CUSTOMS_HOUSE:
                    inventory[buildingType.ordinal()] = 1;
                    break;
                default:
                    inventory[buildingType.ordinal()] = 2;
            }
        }
    }


    /**
     * Indicates whether the game rules allow the given player to purchase the given building.
     *
     * @param player       A Player object, including their doubloons, plantations, role, city spaces.
     * @param buildingType A type of Building: there must be at least one in the inventory.
     * @return A boolean, true if the purchase is allowed.
     */
    public boolean playerCanPurchaseBuilding(Player player, BuildingType buildingType) {
        if (!buildingInInventory(buildingType)) {
            return false;
        }
        if (player.hasBuilding(buildingType)) {
            return false;
        }
        int citySpacesRequired = Building.getSpacesRequiredForBuildingType(buildingType);
        if (player.getBuildingSpacesLeft() < citySpacesRequired) {
            return false;
        }
        return getPriceForPlayerToPurchaseBuilding(player, buildingType) <= player.getDoubloons();
    }

    /**
     * Returns the price for a given Player to purchase a given Building.
     *
     * @param player       A Player, including their role, and quarries.
     * @param buildingType A BuildingType to purchase.
     * @return A cost in doubloons, including any discounts for quarries or the Builder role.
     */
    public int getPriceForPlayerToPurchaseBuilding(Player player, BuildingType buildingType) {
        int price = Building.getBaseCostForBuildingType(buildingType);
        if (player.isActingRole(RoleCardType.BUILDER)) {
            price -= 1;
        }
        // Apply a discount for quarries. The max discount is equal to the building column.
        int occupiedQuarries = player.countOccupiedPlantationsOfType(PlantationType.QUARRY);
        int buildingColumn = Building.getBuildingColumnForBuildingType(buildingType);
        price -= Math.min(occupiedQuarries, buildingColumn);

        return Math.max(price, 0);  // Minimum price is 0 doubloons.
    }

    /**
     * Sells a Building to the player.
     * <p>
     * Creates the Building object, decrements the number of this building type available in the
     * BuildingInventory, gives the new Building to the player and subtracts doubloons
     * according to the price.
     * <p>
     * Note: the caller *must* check if the sale is permitted by calling
     * {@link #playerCanPurchaseBuilding(Player, BuildingType)} before this method.
     *
     * @param player A Player, including their role, buildings, doubloons, and plantations.
     * @param buildingType A BuildingType that the player would like to purchase.
     */
    public void sellBuildingToPlayer(Player player, BuildingType buildingType) {
        int price = getPriceForPlayerToPurchaseBuilding(player, buildingType);
        player.removeDoubloons(price);
        inventory[buildingType.ordinal()] -= 1;
        Building building = new Building(buildingType);
        player.addBuilding(building);
    }

    private boolean buildingInInventory(BuildingType buildingType) {
        return inventory[buildingType.ordinal()] > 0;
    }
}
