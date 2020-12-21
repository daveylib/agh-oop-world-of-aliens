package agh.cs.oop.engine;

import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Collections;

public class GameMap {
    private final int mapWidth;
    private final int mapHeight;
    private final double jungleRatio;
    private Vector2d jungleUpperLeft;
    private Vector2d jungleLowerRight;
    private final TreeMap<Vector2d, MapField> gameMap = new TreeMap<>();

    public GameMap(int mapWidth, int mapHeight, double jungleRatio) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.jungleRatio = jungleRatio;

        calcJungleSize();
    }

    public Vector2d getJungleUpperLeft() {
        return this.jungleUpperLeft;
    }

    public Vector2d getJungleLowerRight() {
        return this.jungleLowerRight;
    }

    // Calculate dimensions of a rectangular for the jungle and prepare vectors for corners
    private void calcJungleSize() throws RuntimeException {
        int minMapSide = Math.min(this.mapWidth, this.mapHeight);

        // Calculate area of the jungle and find factors of this number
        int jungleArea = (int) Math.round(this.mapWidth * this.mapHeight * this.jungleRatio);
        ArrayList<Integer> jungleAreaFactors = this.findFactors(jungleArea);

        // Check whether jungleArea is a prime number and it fits on the map
        if (jungleAreaFactors.size() == 2 && jungleAreaFactors.get(1) > minMapSide) {
            jungleAreaFactors = this.findFactors(jungleArea - 1);
        }

        // Get two the largest factors and check if they fits on the map
        int firstIndex = (jungleAreaFactors.size() / 2) - 1;
        int secondIndex = firstIndex + 1;

        while (!isRightForMap(jungleAreaFactors.get(firstIndex), jungleAreaFactors.get(secondIndex))) {
            if (firstIndex == 0 && secondIndex == jungleAreaFactors.size() - 1) {
                throw new RuntimeException("Jungle size for map " + this.mapWidth + "x" + this.mapHeight + " can not be found!");
            }

            firstIndex -= 1;
            secondIndex += 1;
        }

        // Find a proper orientation of a jungle to place it on the map
        int jungleWidth = jungleAreaFactors.get(firstIndex);
        int jungleHeight = jungleAreaFactors.get(secondIndex);

        if (jungleWidth > this.mapWidth || jungleHeight > this.mapHeight) {
            int tmp = jungleWidth;
            jungleWidth = jungleHeight;
            jungleHeight = tmp;
        }

        // Prepare vectors for the corners
        int upperLeftX = (int) Math.floor((double) (this.mapWidth - jungleWidth) / 2);
        int upperLeftY = (int) Math.floor((double) (this.mapHeight - jungleHeight) / 2);
        this.jungleUpperLeft = new Vector2d(upperLeftX, upperLeftY);

        int lowerRightX = upperLeftX + jungleWidth - 1;
        int lowerRightY = upperLeftY + jungleHeight - 1;
        this.jungleLowerRight = new Vector2d(lowerRightX, lowerRightY);
    }

    // Check whether rectangular with given sides fits the map
    private boolean isRightForMap(int firstSide, int secondSide) {
        if (firstSide <= this.mapWidth && secondSide <= this.mapHeight) {
            return true;
        } else {
            return secondSide <= this.mapWidth && firstSide <= this.mapHeight;
        }
    }

    // Find all factors of a given number and return them in a list
    private ArrayList<Integer> findFactors(int number) {
        ArrayList<Integer> factors = new ArrayList<>();

        for (int factor = 1; factor <= Math.sqrt(number); factor++) {
            if (number % factor == 0) {
                factors.add(factor);
                factors.add(number / factor);
            }
        }

        Collections.sort(factors);

        return factors;
    }

    // Correct position if it is placed outside the map
    public Vector2d correctPosition(Vector2d position) {
        int x = position.getX();
        int y = position.getY();

        if (x < 0) x += this.mapWidth;
        if (x >= this.mapWidth) x -= this.mapWidth;

        if (y < 0) y += this.mapHeight;
        if (y >= this.mapHeight) y -= this.mapHeight;

        return new Vector2d(x, y);
    }

    // Place new alien on the map
    public boolean placeAlien(Alien alien) {
        Vector2d alienPos = alien.getPosition();

        MapField mapField = this.getMapField(alienPos);
        mapField.aliens.add(alien);

        // Add listener to handle changes in the alien's position
        alien.getPositionProperty().addListener((observable, oldPos, newPos) -> {
            this.gameMap.get(oldPos).aliens.remove(alien);

            MapField newMapField = this.getMapField(newPos);
            newMapField.aliens.add(alien);
        });

        return true;
    }

    // Place new mushroom on the map
    public boolean placeMushroom(Mushroom mushroom) throws RuntimeException {
        Vector2d mushroomPos = mushroom.getPosition();

        if (!this.isFreeOfMushroom(mushroomPos)) {
            throw new RuntimeException("New mushroom can not be placed at " + mushroomPos);
        }

        MapField mapField = this.getMapField(mushroomPos);
        mapField.mushrooms.add(mushroom);

        return true;
    }

    // Remove existing alien from the map
    public boolean removeAlien(Alien alien) throws RuntimeException {
        Vector2d alienPos = alien.getPosition();

        if (this.isFreeOfAliens(alienPos)) {
            throw new RuntimeException("Alien from position " + alienPos + " can not be removed!");
        }

        MapField mapField = this.getMapField(alienPos);
        mapField.aliens.remove(alien);

        return true;
    }

    // Remove existing mushroom from the map
    public boolean removeMushroom(Mushroom mushroom) throws RuntimeException {
        Vector2d mushroomPos = mushroom.getPosition();

        if (this.isFreeOfMushroom(mushroomPos)) {
            throw new RuntimeException("Mushroom from position " + mushroomPos + " can not be removed!");
        }

        MapField mapField = this.getMapField(mushroomPos);
        mapField.mushrooms.remove(mushroom);

        return true;
    }

    // Get field from the map on given position or create one if it does not exist
    private MapField getMapField(Vector2d position) {
        MapField mapField = this.gameMap.get(position);

        if (mapField == null) {
            mapField = new MapField();
            this.gameMap.put(position, mapField);
        }

        return mapField;
    }

    // Get array of aliens from the map on given position
    public ArrayList<Alien> getAliensFromField(Vector2d position) {
        ArrayList<Alien> aliens = new ArrayList<>();

        MapField mapField = this.gameMap.get(position);

        if (mapField != null && mapField.aliens.size() != 0) {
            aliens.addAll(mapField.aliens);
        }

        return aliens;
    }

    // Check whether position on the map is free of mushroom
    public boolean isFreeOfMushroom(Vector2d position) {
        MapField mapField = this.gameMap.get(position);

        if (mapField == null) return true;

        return mapField.mushrooms.size() == 0;
    }

    // Check whether position on the map is free of aliens
    public boolean isFreeOfAliens(Vector2d position) {
        MapField mapField = this.gameMap.get(position);

        if (mapField == null) return true;

        return mapField.aliens.size() == 0;
    }

    // Check whether position on the map is completely free of any aliens and mushrooms
    public boolean isFree(Vector2d position) {
        MapField mapField = this.gameMap.get(position);

        if (mapField == null) return true;

        return mapField.aliens.size() == 0 && mapField.mushrooms.size() == 0;
    }

    // Check whether position on the map is inside the jungle
    private boolean isInsideJungle(Vector2d position) {
        return position.follows(this.jungleUpperLeft) && position.precedes(this.jungleLowerRight);
    }

    // Get a free random field from the map which is outside the jungle
    public Vector2d getRandomFreeOutsideJungle() {
        int posX = (int) (Math.random() * (this.mapWidth - 1));
        int posY = (int) (Math.random() * (this.mapHeight - 1));

        Vector2d randomPosition = new Vector2d(posX, posY);

        for (int x = 0; (x < this.mapWidth) && (!this.isFree(randomPosition) || this.isInsideJungle(randomPosition)); x++) {
            for (int y = 0; (y < this.mapHeight) && (!this.isFree(randomPosition) || this.isInsideJungle(randomPosition)); y++) {
                randomPosition = this.correctPosition(randomPosition.add(WorldDirection.NORTH_EAST.toUnitVector()));

                if (y == this.mapHeight - 1) randomPosition = this.correctPosition(randomPosition.add(WorldDirection.EAST.toUnitVector()));
            }
        }

        return isFree(randomPosition) && !isInsideJungle(randomPosition) ? randomPosition : null;
    }

    // Correct position if it is placed outside the jungle
    private Vector2d correctPositionInJungle(Vector2d position) {
        int jungleWidth = this.jungleLowerRight.getX() - this.jungleUpperLeft.getX();
        int jungleHeight = this.jungleLowerRight.getY() - this.jungleUpperLeft.getY();

        int x = position.getX();
        int y = position.getY();

        if (x < this.jungleUpperLeft.getX()) x += jungleWidth;
        if (x >= this.jungleLowerRight.getX()) x -= jungleWidth;

        if (y < this.jungleUpperLeft.getY()) y += jungleHeight;
        if (y >= this.jungleLowerRight.getY()) y -= jungleHeight;

        return new Vector2d(x, y);
    }

    // Get a free random field from the map which is inside the jungle
    public Vector2d getRandomFreeInsideJungle() {
        int jungleWidth = this.jungleLowerRight.getX() - this.jungleUpperLeft.getX();
        int jungleHeight = this.jungleLowerRight.getY() - this.jungleUpperLeft.getY();

        int posX = (int) ((Math.random() * (jungleWidth + 1)) + this.jungleUpperLeft.getX());
        int posY = (int) ((Math.random() * (jungleHeight + 1)) + this.jungleUpperLeft.getY());

        Vector2d randomPosition = new Vector2d(posX, posY);

        for (int x = 0; x < jungleWidth && !isFree(randomPosition); x++) {
            for (int y = 0; y < jungleHeight && !isFree(randomPosition); y++) {
                randomPosition = this.correctPositionInJungle(randomPosition.add(WorldDirection.NORTH_EAST.toUnitVector()));

                if (y == jungleHeight - 1) randomPosition = this.correctPositionInJungle(randomPosition.add(WorldDirection.EAST.toUnitVector()));
            }
        }

        return isFree(randomPosition) && isInsideJungle(randomPosition) ? randomPosition : null;
    }
}
