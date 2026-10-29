package hotel.listener;

import hotel.event.EventType;
import hotel.event.OccupancyEvent;
import hotel.event.RoomEvent;
import hotel.observer.BookingSource;
import hotel.observer.ListenerRegistry;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FloorOccupancyAggregatorTest {
    private final BookingSource bookings = new BookingSource(new ListenerRegistry<RoomEvent>());
    private final FloorOccupancyAggregator aggregator =
            new FloorOccupancyAggregator(new ListenerRegistry<OccupancyEvent>());
    private final List<OccupancyEvent> received = new ArrayList<>();

    FloorOccupancyAggregatorTest() {
        bookings.subscribe(EventType.guest_checked_in, aggregator);
        bookings.subscribe(EventType.guest_checked_out, aggregator);
        aggregator.subscribe(EventType.floor_occupancy_changed, received::add);
    }

    @Test
    void counts_per_floor_and_republishes() {
        bookings.checkIn(1, "101");
        bookings.checkIn(1, "102");
        bookings.checkIn(2, "201");
        bookings.checkOut(1, "101");

        assertEquals(List.of(
                new OccupancyEvent(1, 1),
                new OccupancyEvent(1, 2),
                new OccupancyEvent(2, 1),
                new OccupancyEvent(1, 1)), received);
    }

    @Test
    void occupancy_never_goes_negative() {
        bookings.checkOut(3, "301");

        assertEquals(List.of(new OccupancyEvent(3, 0)), received);
    }
}