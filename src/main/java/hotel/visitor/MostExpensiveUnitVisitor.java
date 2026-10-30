package hotel.visitor;

import hotel.model.*;

import java.util.List;
import java.util.Optional;

public class MostExpensiveUnitVisitor implements Visitor<Optional<Bookable>> {

    @Override
    public Optional<Bookable> visitHotel(Hotel hotel) {
        return best(hotel.getFloors());
    }

    @Override
    public Optional<Bookable> visitFloor(Floor floor) {
        return best(floor.getUnits());
    }

    @Override
    public Optional<Bookable> visitStandardRoom(StandardRoom room) {
        return Optional.of(room);
    }

    @Override
    public Optional<Bookable> visitSuite(Suite suite) {
        return Optional.of(suite);
    }

    @Override
    public Optional<Bookable> visitConferenceHall(ConferenceHall hall) {
        return Optional.of(hall);
    }

    @Override
    public Optional<Bookable> visitRestaurant(Restaurant restaurant) {
        return Optional.empty();
    }

    private Optional<Bookable> best(List<? extends Element> items) {
        Optional<Bookable> best = Optional.empty();
        for (Element item : items) {
            Optional<Bookable> candidate = item.accept(this);
            if (candidate.isPresent() && (best.isEmpty() || candidate.get().getPrice() > best.get().getPrice())) {
                best = candidate;
            }
        }
        return best;
    }
}