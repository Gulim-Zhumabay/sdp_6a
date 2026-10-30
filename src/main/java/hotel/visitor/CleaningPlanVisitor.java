package hotel.visitor;

import hotel.model.*;

import java.util.List;

public class CleaningPlanVisitor implements Visitor<List<Bookable>> {

    @Override
    public List<Bookable> visitHotel(Hotel hotel) {
        return hotel.getFloors().stream().flatMap(floor -> floor.accept(this).stream()).toList();
    }

    @Override
    public List<Bookable> visitFloor(Floor floor) {
        return floor.getUnits().stream().flatMap(unit -> unit.accept(this).stream()).toList();
    }

    @Override
    public List<Bookable> visitStandardRoom(StandardRoom room) {
        return dirtyOnly(room);
    }

    @Override
    public List<Bookable> visitSuite(Suite suite) {
        return dirtyOnly(suite);
    }

    @Override
    public List<Bookable> visitConferenceHall(ConferenceHall hall) {
        return dirtyOnly(hall);
    }

    @Override
    public List<Bookable> visitRestaurant(Restaurant restaurant) {
        return List.of();
    }

    @Override
    public List<Bookable> visitSpa(Spa spa) {
        return dirtyOnly(spa);
    }

    private List<Bookable> dirtyOnly(Bookable bookable) {
        return bookable.isDirty() ? List.of(bookable) : List.of();
    }
}