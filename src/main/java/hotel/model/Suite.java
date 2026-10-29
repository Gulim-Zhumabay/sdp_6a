package hotel.model;

import hotel.visitor.Visitor;

public class Suite extends Bookable {
    private final int bedrooms;

    public Suite(String id, double price, int bedrooms) {
        super(id, price);
        this.bedrooms = bedrooms;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitSuite(this);
    }
}