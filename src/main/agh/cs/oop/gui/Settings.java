package agh.cs.oop.gui;

public class Settings {
    private int mapWidth;
    private int mapHeight;
    private int startEnergy;
    private int moveEnergy;
    private int plantEnergy;
    private double jungleRatio;
    private int initialAliens;
    private int gridSize;

    // Default values of Settings
    public Settings() {
        this.setMapSize(10, 10);
        this.setInitialAliens(15);
        this.setEnergyValues(100, 5, 10);
        this.setJungleRatio(0.2);
        this.setGridSize(35);
    }

    public int getMapWidth() {
        return this.mapWidth;
    }

    public int getMapHeight() {
        return this.mapHeight;
    }

    public void setMapSize(int mapWidth, int mapHeight) {
        if (mapWidth <= 0) throw new IllegalArgumentException("Width of the map can not be lesser than 1");
        if (mapHeight <= 0) throw new IllegalArgumentException("Height of the map can not be lesser than 1");

        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
    }

    public int getStartEnergy() {
        return this.startEnergy;
    }

    public int getMoveEnergy() {
        return this.moveEnergy;
    }

    public int getPlantEnergy() {
        return this.plantEnergy;
    }

    public void setEnergyValues(int startEnergy, int moveEnergy, int plantEnergy) {
        if (startEnergy < 1) throw new IllegalArgumentException("Initial energy of Aliens can not be lesser than 1");
        if (moveEnergy < 0) throw new IllegalArgumentException("Energy loss while moving everyday can not be lesser than 0");
        if (plantEnergy < 0) throw new IllegalArgumentException("Energy boost after eating mushrooms can not be lesser than 0");

        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
    }

    public double getJungleRatio() {
        return this.jungleRatio;
    }

    public void setJungleRatio(double jungleRatio) {
        if (jungleRatio < 0) throw new IllegalArgumentException("Ratio of the jungle to the map can not be lesser than 0");
        if (jungleRatio > 1) throw new IllegalArgumentException("Ratio of the jungle to the map can not be greater than 1");

        this.jungleRatio = jungleRatio;
    }

    public int getInitialAliens() {
        return this.initialAliens;
    }

    public void setInitialAliens(int initialAliens) {
        if (initialAliens < 1) {
            throw new IllegalArgumentException("Quantity of initial Aliens can not be lesser than 1");
        } else if (initialAliens > this.mapWidth * this.mapHeight) {
            throw new IllegalArgumentException("Quantity of initial Aliens can not be greater than number of fields on the map");
        }

        this.initialAliens = initialAliens;
    }

    public int getGridSize() {
        return this.gridSize;
    }

    public void setGridSize(int gridSize) {
        if (gridSize < 30) throw new IllegalArgumentException("Grid size can not be lesser than 30");

        this.gridSize = gridSize;
    }
}
