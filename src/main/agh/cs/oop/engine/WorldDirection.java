package agh.cs.oop.engine;

public enum WorldDirection {
    NORTH("Północ", new Vector2d(0, 1)),
    NORTH_EAST("Północny Wschód", new Vector2d(1, 1)),
    EAST("Wschód", new Vector2d(1, 0)),
    SOUTH_EAST("Południowy Wschód", new Vector2d(1, -1)),
    SOUTH("Południe", new Vector2d(0, -1)),
    SOUTH_WEST("Południowy Zachod", new Vector2d(-1, -1)),
    WEST("Zachód", new Vector2d(-1, 0)),
    NORTH_WEST("Północny Zachód", new Vector2d(-1, 1));

    private final String desc;
    private final Vector2d vector;

    WorldDirection(String desc, Vector2d vector) {
        this.desc = desc;
        this.vector = vector;
    }

    public String toString() {
        return this.desc;
    }

    public WorldDirection next() {
        return values()[(this.ordinal() + 1) % values().length];
    }

    public Vector2d toUnitVector() {
        return this.vector;
    }

    public static WorldDirection random() { return values()[(int) (Math.random() * values().length)]; }
}

