package hotel.model;

import hotel.visitor.Visitor;

public class ConferenceHall extends Bookable {
    private final int capacity;

    public ConferenceHall(String id, double price, int capacity) {
        super(id, price);
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitConferenceHall(this);
    }
}