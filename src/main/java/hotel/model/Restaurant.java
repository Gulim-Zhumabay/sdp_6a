package hotel.model;

import hotel.visitor.Visitor;

public class Restaurant implements Element {
    private final String id;
    private final double dailyRevenue;

    public Restaurant(String id, double dailyRevenue) {
        this.id = id;
        this.dailyRevenue = dailyRevenue;
    }

    public String getId() {
        return id;
    }

    public double getDailyRevenue() {
        return dailyRevenue;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitRestaurant(this);
    }
}