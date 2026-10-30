package hotel.simulation;

import hotel.event.EventType;
import hotel.event.OccupancyEvent;
import hotel.event.RoomEvent;
import hotel.listener.FloorOccupancyAggregator;
import hotel.observer.BookingSource;
import hotel.observer.ListenerRegistry;
import hotel.observer.Observer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConcurrentObserverTest {
    private final BookingSource bookings = new BookingSource(new ListenerRegistry<RoomEvent>());

    @Test
    void aggregator_does_not_lose_updates() throws Exception {
        FloorOccupancyAggregator aggregator = new FloorOccupancyAggregator(new ListenerRegistry<OccupancyEvent>());
        AtomicInteger received = new AtomicInteger();
        AtomicInteger highest = new AtomicInteger();
        bookings.subscribe(EventType.guest_checked_in, aggregator);
        aggregator.subscribe(EventType.floor_occupancy_changed, e -> {
            received.incrementAndGet();
            highest.accumulateAndGet(e.occupiedRooms(), Math::max);
        });

        runInParallel(4, () -> {
            for (int i = 0; i < 1000; i++) {
                bookings.checkIn(1, "101");
            }
        });

        assertEquals(4000, received.get());
        assertEquals(4000, highest.get());
    }

    @Test
    void subscribing_and_unsubscribing_during_publishing_loses_nothing() throws Exception {
        AtomicInteger permanent = new AtomicInteger();
        bookings.subscribe(EventType.booking_created, e -> permanent.incrementAndGet());
        AtomicBoolean running = new AtomicBoolean(true);
        ExecutorService churnPool = Executors.newSingleThreadExecutor();
        Future<?> churn = churnPool.submit(() -> {
            Observer<RoomEvent> temporary = e -> { };
            while (running.get()) {
                bookings.subscribe(EventType.booking_created, temporary);
                bookings.unsubscribe(EventType.booking_created, temporary);
            }
        });

        try {
            runInParallel(4, () -> {
                for (int i = 0; i < 1000; i++) {
                    bookings.createBooking(1, "101");
                }
            });
        } finally {
            running.set(false);
            churn.get();
            churnPool.shutdown();
        }

        assertEquals(4000, permanent.get());
    }

    private void runInParallel(int threads, Runnable task) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        try {
            List<Future<?>> futures = new ArrayList<>();
            for (int t = 0; t < threads; t++) {
                futures.add(executor.submit(task));
            }
            for (Future<?> future : futures) {
                future.get();
            }
        } finally {
            executor.shutdown();
        }
    }
}