package hotel.visitor;

import hotel.model.*;

import java.util.List;
import java.util.Optional;

public class RoomLookupVisitor implements Visitor<Optional<Bookable>> {
    private final String roomId;

    public RoomLookupVisitor(String roomId) {
        this.roomId = roomId;
    }

    @Override
    public Optional<Bookable> visitHotel(Hotel hotel) {
        return firstMatch(hotel.getFloors());
    }

    @Override
    public Optional<Bookable> visitFloor(Floor floor) {
        return firstMatch(floor.getUnits());
    }

    @Override
    public Optional<Bookable> visitStandardRoom(StandardRoom room) {
        return match(room);
    }

    @Override
    public Optional<Bookable> visitSuite(Suite suite) {
        return match(suite);
    }

    @Override
    public Optional<Bookable> visitConferenceHall(ConferenceHall hall) {
        return match(hall);
    }

    @Override
    public Optional<Bookable> visitRestaurant(Restaurant restaurant) {
        return Optional.empty();
    }

    private Optional<Bookable> match(Bookable bookable) {
        return bookable.getId().equals(roomId) ? Optional.of(bookable) : Optional.empty();
    }

    @Override
    public Optional<Bookable> visitSpa(Spa spa) {
        return match(spa);
    }

    private Optional<Bookable> firstMatch(List<? extends Element> items) {
        for (Element item : items) {
            Optional<Bookable> found = item.accept(this);
            if (found.isPresent()) {
                return found;
            }
        }
        return Optional.empty();
    }
}