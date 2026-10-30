package hotel.model;

import hotel.visitor.Visitor;

public class Spa extends Bookable {
    private final int treatmentRooms;

    public Spa(String id, double price, int treatmentRooms) {
        super(id, price);
        this.treatmentRooms = treatmentRooms;
    }

    public int getTreatmentRooms() {
        return treatmentRooms;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitSpa(this);
    }
}