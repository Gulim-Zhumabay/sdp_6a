package hotel.simulation;

import hotel.event.EventType;
import hotel.event.RoomEvent;
import hotel.listener.RevenueManagerListener;
import hotel.observer.BookingSource;
import hotel.observer.ListenerRegistry;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SimulationRunnerTest {
    private static final List<RoomRef> ROOMS = List.of(
            new RoomRef(1, "101"), new RoomRef(1, "102"), new RoomRef(2, "201"));

    private final BookingSource bookings = new BookingSource(new ListenerRegistry<RoomEvent>());

    @Test
    void four_producers_with_1000_events_each_deliver_exactly_4000() {
        AtomicInteger delivered = new AtomicInteger();
        RevenueManagerListener revenue = new RevenueManagerListener();
        bookings.subscribe(EventType.booking_created, e -> delivered.incrementAndGet());
        bookings.subscribe(EventType.booking_cancelled, e -> delivered.incrementAndGet());
        bookings.subscribe(EventType.booking_created, revenue);
        bookings.subscribe(EventType.booking_cancelled, revenue);

        new SimulationRunner(bookings, ROOMS, 4, 1000).run();

        assertEquals(4000, delivered.get());
        assertEquals(0, revenue.activeBookings());
    }

    @Test
    void failing_listener_is_reported_and_run_does_not_hang() {
        bookings.subscribe(EventType.booking_created, e -> {
            throw new IllegalStateException("boom");
        });

        assertThrows(IllegalStateException.class, () -> new SimulationRunner(bookings, ROOMS, 3, 10).run());
    }
}