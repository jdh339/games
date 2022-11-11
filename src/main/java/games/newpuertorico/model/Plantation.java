package games.newpuertorico.model;

public class Plantation {

    private final PlantationType plantationType;
    private boolean occupied = false;

    public Plantation(PlantationType plantationType) {
        this.plantationType = plantationType;
    }

    public void addColonist() {
        this.occupied = true;
    }

    public PlantationType getType() {
        return this.plantationType;
    }

    public boolean isOccupied() {
        return occupied;
    }
}
