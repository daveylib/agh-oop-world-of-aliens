package agh.cs.oop.engine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class GameMapTest {
    @Test
    void jungleSizeTest() {
        GameMap gameMap = new GameMap(10, 10, 0.2);

        Vector2d jungleUpperLeft = gameMap.getJungleUpperLeft();
        Vector2d jungleLowerRight = gameMap.getJungleLowerRight();

        assertEquals(jungleUpperLeft.toString(), "(3,2)");
        assertEquals(jungleLowerRight.toString(), "(6,6)");

        int jungleWidth = jungleLowerRight.getX() - jungleUpperLeft.getX() + 1;
        int jungleHeight = jungleLowerRight.getY() - jungleUpperLeft.getY() + 1;

        assertEquals(jungleWidth, 4);
        assertEquals(jungleHeight, 5);

        int jungleArea = jungleWidth * jungleHeight;
        assertEquals(jungleArea, (int) (10 * 10 * 0.2));
    }

    @Test
    void weirdJungleSizeTest() {
        GameMap gameMap = new GameMap(20, 1, 0.2);

        Vector2d jungleUpperLeft = gameMap.getJungleUpperLeft();
        Vector2d jungleLowerRight = gameMap.getJungleLowerRight();

        assertEquals(jungleUpperLeft.toString(), "(8,0)");
        assertEquals(jungleLowerRight.toString(), "(11,0)");

        int jungleWidth = jungleLowerRight.getX() - jungleUpperLeft.getX() + 1;
        int jungleHeight = jungleLowerRight.getY() - jungleUpperLeft.getY() + 1;

        assertEquals(jungleWidth, 4);
        assertEquals(jungleHeight, 1);

        int jungleArea = jungleWidth * jungleHeight;
        assertEquals(jungleArea, (int) (20 * 1 * 0.2));
    }

    @Test
    void placeAlienMethod() {
        GameMap gameMap = new GameMap(10, 10, 0.2);

        byte[] genotypeArray = new byte[] {0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 7, 7};
        Genotype genotype = new Genotype(genotypeArray);

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                assertTrue(gameMap.placeAlien(new Alien(gameMap, new Vector2d(x, y), WorldDirection.NORTH, 100, genotype)));
            }
        }

        Alien alienA = new Alien(gameMap, new Vector2d(2, 2), WorldDirection.NORTH, 100, genotype);
        assertTrue(gameMap.placeAlien(alienA));

        Alien alienB = new Alien(gameMap, new Vector2d(2, 2), WorldDirection.NORTH, 100, genotype);
        assertTrue(gameMap.placeAlien(alienB));
    }

    @Test
    void removeAlienMethod() {
        GameMap gameMap = new GameMap(10, 10, 0.2);

        byte[] genotypeArray = new byte[] {0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 7, 7};
        Genotype genotype = new Genotype(genotypeArray);

        Alien alienA = new Alien(gameMap, new Vector2d(2, 2), WorldDirection.NORTH, 100, genotype);
        assertTrue(gameMap.placeAlien(alienA));
        assertTrue(gameMap.removeAlien(alienA));

        Assertions.assertThrows(RuntimeException.class, () -> gameMap.removeAlien(alienA));
    }

    @Test
    void placeMushroomMethod() {
        GameMap gameMap = new GameMap(10, 10, 0.2);

        byte[] genotypeArray = new byte[] {0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 7, 7};
        Genotype genotype = new Genotype(genotypeArray);

        Alien alienA = new Alien(gameMap, new Vector2d(2, 2), WorldDirection.NORTH, 100, genotype);
        assertTrue(gameMap.placeAlien(alienA));

        Alien alienB = new Alien(gameMap, new Vector2d(3, 3), WorldDirection.NORTH, 100, genotype);
        assertTrue(gameMap.placeAlien(alienB));

        Alien alienC = new Alien(gameMap, new Vector2d(3, 3), WorldDirection.NORTH, 100, genotype);
        assertTrue(gameMap.placeAlien(alienC));

        Mushroom mushroomA = new Mushroom(new Vector2d(1, 1));
        assertTrue(gameMap.placeMushroom(mushroomA));

        Mushroom mushroomB = new Mushroom(new Vector2d(2, 2));
        assertTrue(gameMap.placeMushroom(mushroomB));

        Mushroom mushroomC = new Mushroom(new Vector2d(3, 3));
        assertTrue(gameMap.placeMushroom(mushroomC));

        Mushroom mushroomD = new Mushroom(new Vector2d(3, 3));
        Assertions.assertThrows(RuntimeException.class, () -> gameMap.placeMushroom(mushroomD));
    }

    @Test
    void removeMushroomMethod() {
        GameMap gameMap = new GameMap(10, 10, 0.2);

        Mushroom mushroomA = new Mushroom(new Vector2d(1, 1));
        assertTrue(gameMap.placeMushroom(mushroomA));
        assertTrue(gameMap.removeMushroom(mushroomA));

        Assertions.assertThrows(RuntimeException.class, () -> gameMap.removeMushroom(mushroomA));

        byte[] genotypeArray = new byte[] {0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 7, 7};
        Genotype genotype = new Genotype(genotypeArray);

        Alien alienA = new Alien(gameMap, new Vector2d(2, 2), WorldDirection.NORTH, 100, genotype);
        assertTrue(gameMap.placeAlien(alienA));

        Mushroom mushroomB = new Mushroom(new Vector2d(2, 2));
        assertTrue(gameMap.placeMushroom(mushroomB));
        assertTrue(gameMap.removeMushroom(mushroomB));

        Assertions.assertThrows(RuntimeException.class, () -> gameMap.removeMushroom(mushroomB));
    }

    @Test
    void getAliensFromFieldMethod() {
        GameMap gameMap = new GameMap(10, 10, 0.2);

        Vector2d testPost = new Vector2d(2, 2);

        assertEquals(gameMap.getAliensFromField(new Vector2d(2,2)).size(), 0);

        byte[] genotypeArray = new byte[] {0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 7, 7};
        Genotype genotype = new Genotype(genotypeArray);

        Alien alienA = new Alien(gameMap, testPost, WorldDirection.NORTH, 100, genotype);
        assertTrue(gameMap.placeAlien(alienA));

        assertEquals(gameMap.getAliensFromField(testPost).size(), 1);
        assertTrue(gameMap.getAliensFromField(testPost).contains(alienA));

        Alien alienB = new Alien(gameMap, testPost, WorldDirection.NORTH, 100, genotype);
        assertTrue(gameMap.placeAlien(alienB));

        assertEquals(gameMap.getAliensFromField(testPost).size(), 2);
        assertTrue(gameMap.getAliensFromField(testPost).contains(alienA));
        assertTrue(gameMap.getAliensFromField(testPost).contains(alienB));
    }

    @Test
    void isFreeOfMushroomsMethod() {
        GameMap gameMap = new GameMap(10, 10, 0.2);

        Vector2d testPost = new Vector2d(2,2);

        assertTrue(gameMap.isFreeOfMushroom(testPost));

        byte[] genotypeArray = new byte[] {0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 7, 7};
        Genotype genotype = new Genotype(genotypeArray);

        Alien alienA = new Alien(gameMap, testPost, WorldDirection.NORTH, 100, genotype);
        assertTrue(gameMap.placeAlien(alienA));

        assertTrue(gameMap.isFreeOfMushroom(testPost));

        Mushroom mushroomA = new Mushroom(testPost);
        assertTrue(gameMap.placeMushroom(mushroomA));

        assertFalse(gameMap.isFreeOfMushroom(testPost));
    }

    @Test
    void isFreeOfAliensMethod() {
        GameMap gameMap = new GameMap(10, 10, 0.2);

        Vector2d testPost = new Vector2d(2,2);

        assertTrue(gameMap.isFreeOfAliens(testPost));

        Mushroom mushroomA = new Mushroom(testPost);
        assertTrue(gameMap.placeMushroom(mushroomA));

        assertTrue(gameMap.isFreeOfAliens(testPost));

        byte[] genotypeArray = new byte[] {0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 7, 7};
        Genotype genotype = new Genotype(genotypeArray);

        Alien alienA = new Alien(gameMap, testPost, WorldDirection.NORTH, 100, genotype);
        assertTrue(gameMap.placeAlien(alienA));

        assertFalse(gameMap.isFreeOfAliens(testPost));
    }

    @Test
    void isFreeMethod() {
        GameMap gameMap = new GameMap(10, 10, 0.2);

        Vector2d testPost = new Vector2d(2,2);

        assertTrue(gameMap.isFree(testPost));

        byte[] genotypeArray = new byte[] {0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 7, 7};
        Genotype genotype = new Genotype(genotypeArray);

        Alien alienA = new Alien(gameMap, testPost, WorldDirection.NORTH, 100, genotype);
        assertTrue(gameMap.placeAlien(alienA));

        assertFalse(gameMap.isFree(testPost));

        assertTrue(gameMap.removeAlien(alienA));

        assertTrue(gameMap.isFree(testPost));

        Mushroom mushroomA = new Mushroom(testPost);
        assertTrue(gameMap.placeMushroom(mushroomA));

        assertFalse(gameMap.isFree(testPost));
    }

    @Test
    void getRandomFreeOutsideJungle() {
        GameMap gameMap = new GameMap(10, 10, 0.2);

        Vector2d jungleUpperLeft = gameMap.getJungleUpperLeft();
        Vector2d jungleLowerRight = gameMap.getJungleLowerRight();

        ArrayList<Vector2d> freeOutside = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Vector2d randomPos = gameMap.getRandomFreeOutsideJungle();

            if (randomPos != null) {
                assertTrue(gameMap.placeMushroom(new Mushroom(randomPos)));
                freeOutside.add(randomPos);
            }
        }

        assertEquals(freeOutside.size(), 80);

        for (Vector2d pos : freeOutside) {
            assertFalse(pos.follows(jungleUpperLeft) && pos.precedes(jungleLowerRight));
        }
    }

    @Test
    void getRandomFreeInsideJungle() {
        GameMap gameMap = new GameMap(10, 10, 0.2);

        Vector2d jungleUpperLeft = gameMap.getJungleUpperLeft();
        Vector2d jungleLowerRight = gameMap.getJungleLowerRight();

        ArrayList<Vector2d> freeInside = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Vector2d randomPos = gameMap.getRandomFreeInsideJungle();

            if (randomPos != null) {
                assertTrue(gameMap.placeMushroom(new Mushroom(randomPos)));
                freeInside.add(randomPos);
            }
        }

        assertEquals(freeInside.size(), 20);

        for (Vector2d pos : freeInside) {
            assertTrue(pos.follows(jungleUpperLeft) && pos.precedes(jungleLowerRight));
        }
    }
}
