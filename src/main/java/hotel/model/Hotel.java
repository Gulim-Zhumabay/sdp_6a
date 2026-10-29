package hotel.model;

import hotel.visitor.Visitor;

import java.util.List;

public class Hotel implements Element {
    private final String name;
    private final List<Floor> floors;

    public Hotel(String name, List<Floor> floors) {
        this.name = name;
        this.floors = List.copyOf(floors);
    }

    public String getName() {
        return name;
    }

    public List<Floor> getFloors() {
        return floors;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitHotel(this);
    }
}