package games.newpuertorico.model;

/**
 * A Building represents any of the various buildings in Puerto Rico.
 * <p>
 * This class also contains static methods for building details: cost, capacity, and column.
 */
public class Building {

    private final BuildingType buildingType;
    private int numColonists = 0;

    /**
     * Creates a Building of the given type.
     *
     * @param buildingType A BuildingType, on of the various kinds in Puerto Rico.
     */
    public Building(BuildingType buildingType) {
        this.buildingType = buildingType;

    }

    /**
     * Returns the column of the Building on the game board.
     * <p>
     * The column indicates the number of victory points awarded for this building at the end of
     * the game, and the max discount that may be given from quarries.
     *
     * @param buildingType A BuildingType.
     * @return An int between 1 and 4.
     */
    public static int getBuildingColumnForBuildingType(BuildingType buildingType) {
        int buildingColumn;
        switch (buildingType) {
            case SMALL_INDIGO_PLANT:
            case SMALL_SUGAR_MILL:
            case SMALL_MARKET:
            case HACIENDA:
            case CONSTRUCTION_HUT:
            case SMALL_WAREHOUSE:
                buildingColumn = 1;
                break;
            case INDIGO_PLANT:
            case SUGAR_MILL:
            case HOSPICE:
            case OFFICE:
            case LARGE_MARKET:
            case LARGE_WAREHOUSE:
                buildingColumn = 2;
                break;
            case TOBACCO_STORAGE:
            case COFFEE_ROASTER:
            case FACTORY:
            case UNIVERSITY:
            case HARBOR:
            case WHARF:
                buildingColumn = 3;
                break;
            default:
                buildingColumn = 4;
                break;
        }
        return buildingColumn;
    }

    /**
     * Returns the number of colonist spaces in a given building type.
     *
     * @param buildingType A BuildingType.
     * @return An int: 3,2, or 1.
     */
    public static int getColonistCapacityForBuildingType(BuildingType buildingType) {
        int capacity;
        switch (buildingType) {
            case INDIGO_PLANT:
            case SUGAR_MILL:
            case TOBACCO_STORAGE:
                capacity = 3;
                break;
            case COFFEE_ROASTER:
                capacity = 2;
                break;
            default:
                capacity = 1;
        }
        return capacity;
    }

    /**
     * Returns the base cost in doubloons for any building of the given type.
     *
     * @param buildingType A type of building.
     * @return An integer representing the base cost, before discounts for quarry, Builder, etc.
     */
    public static int getBaseCostForBuildingType(BuildingType buildingType) {
        int cost = 0;
        switch (buildingType) {
            case SMALL_INDIGO_PLANT:
            case SMALL_MARKET:
                cost = 1;
                break;
            case SMALL_SUGAR_MILL:
            case HACIENDA:
            case CONSTRUCTION_HUT:
                cost = 2;
                break;
            case SMALL_WAREHOUSE:
            case INDIGO_PLANT:
                cost = 3;
                break;
            case SUGAR_MILL:
            case HOSPICE:
                cost = 4;
                break;
            case OFFICE:
            case LARGE_MARKET:
            case TOBACCO_STORAGE:
                cost = 5;
                break;
            case LARGE_WAREHOUSE:
            case COFFEE_ROASTER:
                cost = 6;
                break;
            case FACTORY:
                cost = 7;
                break;
            case UNIVERSITY:
            case HARBOR:
                cost = 8;
                break;
            case WHARF:
                cost = 9;
                break;
            case GUILD_HALL:
            case RESIDENCE:
            case FORTRESS:
            case CUSTOMS_HOUSE:
            case CITY_HALL:
                cost = 10;
                break;
        }
        return cost;
    }

    /**
     * Returns the number of city spaces a building takes up.
     *
     * @param buildingType A type of building.
     * @return An integer, 2 for large buildings and 1 for all others.
     */
    public static int getSpacesRequiredForBuildingType(BuildingType buildingType) {
        switch (buildingType) {
            case GUILD_HALL:
            case RESIDENCE:
            case FORTRESS:
            case CUSTOMS_HOUSE:
            case CITY_HALL:
                return 2;
            default:
                return 1;
        }
    }

    /**
     * Adds a single colonist to the building.
     */
    public void addColonist() {
        numColonists += 1;
    }

    /**
     * Returns whether the Building is of the given type.
     *
     * @param buildingType A BuildingType enum.
     * @return A boolean, True if the types match.
     */
    public boolean isType(BuildingType buildingType) {
        return this.buildingType.equals(buildingType);
    }

    /**
     * Returns whether the Building has at least 1 colonist.
     *
     * @return A boolean.
     */
    public boolean isOccupied() {
        return numColonists > 0;
    }

    /**
     * Returns the number of empty colonist spaces on this Building.
     *
     * @return An int.
     */
    public int getEmptyColonistSpaces() {
        return getColonistCapacity() - numColonists;
    }

    /**
     * Returns the number of colonists this Building can hold.
     *
     * @return An int between 1 and 3.
     */
    public int getColonistCapacity() {
        return getColonistCapacityForBuildingType(buildingType);
    }

    /**
     * Returns the base cost of this Building.
     *
     * @return An int, the cost of this building in doubloons.
     */
    public int getBaseCost() {
        return Building.getBaseCostForBuildingType(this.buildingType);
    }

    /**
     * Returns the number of spaces (city tiles) this building takes up.
     *
     * @return An int.
     */
    public int getSpacesRequired() {
        return Building.getSpacesRequiredForBuildingType(this.buildingType);
    }
}
