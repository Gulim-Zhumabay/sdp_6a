package hotel.visitor;

import hotel.model.*;

public class RevenueVisitor implements Visitor<Double> {

    @Override
    public Double visitHotel(Hotel hotel) {
        return hotel.getFloors().stream().mapToDouble(floor -> floor.accept(this)).sum();
    }

    @Override
    public Double visitFloor(Floor floor) {
        return floor.getUnits().stream().mapToDouble(unit -> unit.accept(this)).sum();
    }

    @Override
    public Double visitStandardRoom(StandardRoom room) {
        return income(room);
    }

    @Override
    public Double visitSuite(Suite suite) {
        return income(suite);
    }

    @Override
    public Double visitConferenceHall(ConferenceHall hall) {
        return income(hall);
    }

    @Override
    public Double visitRestaurant(Restaurant restaurant) {
        return restaurant.getDailyRevenue();
    }

    @Override
    public Double visitSpa(Spa spa) {
        return income(spa);
    }

    private double income(Bookable bookable) {
        return bookable.isOccupied() ? bookable.getPrice() : 0;
    }
}