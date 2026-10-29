package hotel.visitor;

import hotel.model.*;

public class OccupancyVisitor implements Visitor<OccupancyReport> {
    private int total;
    private int occupied;

    @Override
    public OccupancyReport visitHotel(Hotel hotel) {
        hotel.getFloors().forEach(floor -> floor.accept(this));
        return report();
    }

    @Override
    public OccupancyReport visitFloor(Floor floor) {
        floor.getUnits().forEach(unit -> unit.accept(this));
        return report();
    }

    @Override
    public OccupancyReport visitStandardRoom(StandardRoom room) {
        return count(room);
    }

    @Override
    public OccupancyReport visitSuite(Suite suite) {
        return count(suite);
    }

    @Override
    public OccupancyReport visitConferenceHall(ConferenceHall hall) {
        return count(hall);
    }

    @Override
    public OccupancyReport visitRestaurant(Restaurant restaurant) {
        return report();
    }

    private OccupancyReport count(Bookable bookable) {
        total++;
        if (bookable.isOccupied()) {
            occupied++;
        }
        return report();
    }

    private OccupancyReport report() {
        return new OccupancyReport(total, occupied);
    }
}