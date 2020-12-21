package agh.cs.oop.engine;

import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertEquals;

public class AlienTest {
    @Test
    void moveTest() {
        GameMap gameMap = new GameMap(10, 10, 0.2);

        byte[] genotypeArray = new byte[] {0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 7, 7};
        Genotype genotype = new Genotype(genotypeArray);
        Alien alien = new Alien(gameMap, new Vector2d(2, 2), WorldDirection.NORTH, 100, genotype);

        assertEquals(alien.getPosition().toString(), "(2,2)");

        alien.move(MoveDirection.FORWARD);
        alien.move(MoveDirection.RIGHT);
        alien.move(MoveDirection.RIGHT);
        alien.move(MoveDirection.FORWARD);
        alien.move(MoveDirection.FORWARD);
        alien.move(MoveDirection.FORWARD);
        alien.move(MoveDirection.FORWARD);
        assertEquals(alien.getPosition().toString(), "(6,3)");

        alien.move(MoveDirection.RIGHT);
        alien.move(MoveDirection.FORWARD);
        alien.move(MoveDirection.FORWARD);
        alien.move(MoveDirection.FORWARD);
        assertEquals(alien.getPosition().toString(), "(9,0)");

        alien.move(MoveDirection.RIGHT);
        alien.move(MoveDirection.RIGHT);
        alien.move(MoveDirection.RIGHT);
        alien.move(MoveDirection.FORWARD);
        alien.move(MoveDirection.FORWARD);
        assertEquals(alien.getPosition().toString(), "(7,0)");

        alien.move(MoveDirection.RIGHT);
        alien.move(MoveDirection.FORWARD);
        alien.move(MoveDirection.FORWARD);
        alien.move(MoveDirection.FORWARD);
        alien.move(MoveDirection.FORWARD);
        assertEquals(alien.getPosition().toString(), "(3,4)");
    }

    @Test
    void energyTest() {
        GameMap gameMap = new GameMap(10, 10, 0.2);

        byte[] genotypeArray = new byte[] {0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 7, 7};
        Genotype genotype = new Genotype(genotypeArray);
        Alien alien = new Alien(gameMap, new Vector2d(2, 2), WorldDirection.NORTH, 100, genotype);

        assertEquals(alien.getEnergy(), 100);

        alien.dropEnergy(5);
        alien.dropEnergy(8);
        alien.dropEnergy(2);
        assertEquals(alien.getEnergy(), 85);

        alien.boostEnergy(10);
        alien.boostEnergy(20);
        assertEquals(alien.getEnergy(), 115);

        alien.dropEnergy(120);
        assertEquals(alien.getEnergy(), -5);
    }

    @Test
    void ageTest() {
        GameMap gameMap = new GameMap(10, 10, 0.2);

        byte[] genotypeArray = new byte[] {0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 7, 7};
        Genotype genotype = new Genotype(genotypeArray);
        Alien alien = new Alien(gameMap, new Vector2d(2, 2), WorldDirection.NORTH, 100, genotype);

        assertEquals(alien.getAge(), 0);

        alien.growOld();
        alien.growOld();
        alien.growOld();
        assertEquals(alien.getAge(), 3);

        for (int i = 0; i < 250; i++) alien.growOld();
        assertEquals(alien.getAge(), 253);
    }

    @Test
    void genotypeTest() {
        GameMap gameMap = new GameMap(10, 10, 0.2);

        byte[] genotypeArray = new byte[] {0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 7, 7};
        Genotype genotype = new Genotype(genotypeArray);
        Alien alien = new Alien(gameMap, new Vector2d(2, 2), WorldDirection.NORTH, 100, genotype);

        assertEquals(alien.getLongGenotype(), genotype.getLongGenotype());

        String prettyGenotype = "[0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 7, 7]";
        assertEquals(alien.getPrettyGenotype(), prettyGenotype);
    }

    @Test
    void childrenTest() {
        GameMap gameMap = new GameMap(10, 10, 0.2);

        byte[] genotypeArray = new byte[] {0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 7, 7};
        Genotype genotype = new Genotype(genotypeArray);

        Alien parentA = new Alien(gameMap, new Vector2d(2, 2), WorldDirection.NORTH, 100, genotype);
        assertEquals(parentA.getChildrenQuantity(), 0);

        Alien parentB = new Alien(gameMap, new Vector2d(2, 2), WorldDirection.NORTH, 100, genotype);
        assertEquals(parentB.getChildrenQuantity(), 0);

        Alien child = new Alien(gameMap, new Vector2d(3, 3), WorldDirection.NORTH, 100, genotype);
        parentA.addChild(child);
        parentB.addChild(child);

        assertEquals(parentA.getChildrenQuantity(), 1);
        assertEquals(parentB.getChildrenQuantity(), 1);
    }

    @Test
    void descendantTest() {
        GameMap gameMap = new GameMap(10, 10, 0.2);

        byte[] genotypeArray = new byte[] {0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 7, 7};
        Genotype genotype = new Genotype(genotypeArray);

        Alien parentA = new Alien(gameMap, new Vector2d(2, 2), WorldDirection.NORTH, 100, genotype);
        assertEquals(parentA.getChildrenQuantity(), 0);
        assertEquals(parentA.getDescendantQuantity(), 0);

        Alien parentB = new Alien(gameMap, new Vector2d(2, 2), WorldDirection.NORTH, 100, genotype);
        assertEquals(parentB.getChildrenQuantity(), 0);
        assertEquals(parentB.getDescendantQuantity(), 0);

        Alien childA = new Alien(gameMap, new Vector2d(3, 3), WorldDirection.NORTH, 100, genotype);
        parentA.addChild(childA);
        parentB.addChild(childA);

        assertEquals(parentA.getChildrenQuantity(), 1);
        assertEquals(parentA.getDescendantQuantity(), 1);

        assertEquals(parentB.getChildrenQuantity(), 1);
        assertEquals(parentB.getDescendantQuantity(), 1);

        assertEquals(childA.getChildrenQuantity(), 0);
        assertEquals(childA.getDescendantQuantity(), 0);

        Alien childB = new Alien(gameMap, new Vector2d(4, 4), WorldDirection.NORTH, 100, genotype);
        assertEquals(childB.getChildrenQuantity(), 0);
        assertEquals(childB.getDescendantQuantity(), 0);

        childA.addChild(childB);
        assertEquals(childA.getChildrenQuantity(), 1);
        assertEquals(childA.getDescendantQuantity(), 1);

        assertEquals(parentA.getChildrenQuantity(), 1);
        assertEquals(parentA.getDescendantQuantity(), 2);

        assertEquals(parentB.getChildrenQuantity(), 1);
        assertEquals(parentB.getDescendantQuantity(), 2);
    }
}
