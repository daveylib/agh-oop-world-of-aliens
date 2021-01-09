package agh.cs.oop.engine;

import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Arrays;

public class Alien {
    private final GameMap gameMap;
    private final ReadOnlyObjectWrapper<Vector2d> position;
    private WorldDirection orientation;
    private final ReadOnlyIntegerWrapper energy;
    private final Genotype genotype;
    private final ReadOnlyIntegerWrapper age = new ReadOnlyIntegerWrapper(0);
    private final ObservableList<Alien> children = FXCollections.observableArrayList(
            alien -> new Observable[] {
                    alien.childrenQuantity
            }
    );
    private final ReadOnlyIntegerWrapper childrenQuantity = new ReadOnlyIntegerWrapper(0);
    private final ReadOnlyIntegerWrapper descendantQuantity = new ReadOnlyIntegerWrapper(0);

    public Alien(GameMap gameMap, Vector2d initialPos, WorldDirection orientation, int startEnergy, Genotype genotype) {
        this.gameMap = gameMap;
        this.position = new ReadOnlyObjectWrapper<>(initialPos);
        this.orientation = orientation;
        this.energy = new ReadOnlyIntegerWrapper(startEnergy);
        this.genotype = genotype;
    }

    // Add new child, increment childrenQuantity and add listener to handle updates of alien's descendants
    public void addChild(Alien child) {
        this.children.add(child);
        this.childrenQuantity.setValue(this.childrenQuantity.getValue() + 1);
        this.descendantQuantity.setValue(this.descendantQuantity.getValue() + 1);

        child.childrenQuantity.addListener((observable, oldValue, newValue) -> {
            int newDescendantQuality = this.descendantQuantity.intValue() - oldValue.intValue() + newValue.intValue();
            this.descendantQuantity.setValue(newDescendantQuality);
        });
    }

    // Secure getters for properties
    public Vector2d getPosition() {
        return this.position.getValue();
    }

    public ReadOnlyObjectProperty<Vector2d> getPositionProperty() {
        return this.position.getReadOnlyProperty();
    }

    public int getEnergy() {
        return this.energy.getValue();
    }

    public ReadOnlyIntegerProperty getEnergyProperty() {
        return this.energy.getReadOnlyProperty();
    }

    public int getAge() {
        return this.age.getValue();
    }

    public ReadOnlyIntegerProperty getAgeProperty() {
        return this.age.getReadOnlyProperty();
    }

    public int getChildrenQuantity() {
        return this.childrenQuantity.intValue();
    }

    public ReadOnlyIntegerProperty getChildrenQuantityProperty() {
        return this.childrenQuantity.getReadOnlyProperty();
    }

    public int getDescendantQuantity() {
        return this.descendantQuantity.intValue();
    }

    public ReadOnlyIntegerProperty getDescendantQuantityProperty() {
        return this.descendantQuantity.getReadOnlyProperty();
    }

    public long getLongGenotype() {
        return this.genotype.getLongGenotype();
    }

    public String getPrettyGenotype() {
        return Arrays.toString(this.genotype.getGenotype());    // this.genotype.toString()?
    }

    // Get preferred rotation from genotype
    public byte getPrefRotation() { // raczej random niż pref
        byte[] genotype = this.genotype.getGenotype();
        byte randomIdx = (byte) (Math.random() * 32);

        return genotype[randomIdx];
    }

    public void growOld() {
        this.age.setValue(this.age.getValue() + 1);
    }

    // Move alien forward or rotate right
    public void move(MoveDirection direction) { // zwierzę miało samo wybierać ruch na podstawie genów
        switch (direction) {
            case FORWARD:
                Vector2d newPosition = this.getPosition().add(this.orientation.toUnitVector());
                Vector2d correctPosition = this.gameMap.correctPosition(newPosition);

                this.changePosition(correctPosition);

                break;
            case RIGHT:
                this.orientation = this.orientation.next();
                break;
            default:
                throw new IllegalArgumentException("Direction " + direction + " is not a valid one");
        }
    }

    public void changePosition(Vector2d newPosition) {
        this.position.setValue(newPosition);
    }

    public void boostEnergy(int boost) {
        this.energy.setValue(this.energy.getValue() + boost);
    }

    public void dropEnergy(int drop) {
        this.energy.setValue(this.energy.getValue() - drop);
    }

    @Override
    public String toString() {
        return "Animal: " + this.getPosition().toString();
    }
}
