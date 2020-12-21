package agh.cs.oop.engine;

public class Mushroom {
    private final Vector2d position;

    public Mushroom(Vector2d initialPos) {
        this.position = initialPos;
    }

    public Vector2d getPosition() {
        return this.position;
    }

    @Override
    public String toString() {
        return "Grass: " + this.position;
    }
}
