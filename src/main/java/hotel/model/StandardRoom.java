package hotel.model;

import hotel.visitor.Visitor;

public class StandardRoom extends Bookable {

    public StandardRoom(String id, double price) {
        super(id, price);
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitStandardRoom(this);
    }
}