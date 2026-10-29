package hotel.model;

import hotel.visitor.Visitor;

import java.util.List;

public class Floor implements Element {
    private final int number;
    private final List<Element> units;

    public Floor(int number, List<Element> units) {
        this.number = number;
        this.units = List.copyOf(units);
    }

    public int getNumber() {
        return number;
    }

    public List<Element> getUnits() {
        return units;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitFloor(this);
    }
}