package hotel.simulation;

import hotel.observer.BookingSource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class SimulationRunner {
    private static final int STOP_TIMEOUT_SECONDS = 10;

    private final BookingSource bookings;
    private final List<RoomRef> rooms;
    private final int producerCount;
    private final int eventsPerProducer;

    public SimulationRunner(BookingSource bookings, List<RoomRef> rooms, int producerCount, int eventsPerProducer) {
        if (rooms.isEmpty() || producerCount < 1 || eventsPerProducer < 0) {
            throw new IllegalArgumentException("need rooms, at least one producer and non-negative events");
        }
        this.bookings = bookings;
        this.rooms = List.copyOf(rooms);
        this.producerCount = producerCount;
        this.eventsPerProducer = eventsPerProducer;
    }

    public void run() {
        ExecutorService executor = Executors.newFixedThreadPool(producerCount);
        try {
            List<Future<?>> futures = new ArrayList<>();
            for (int producer = 0; producer < producerCount; producer++) {
                futures.add(executor.submit(producerTask(producer)));
            }
            for (Future<?> future : futures) {
                future.get();
            }
        } catch (ExecutionException e) {
            throw new IllegalStateException("producer failed", e.getCause());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("interrupted while waiting for producers", e);
        } finally {
            stop(executor);
        }
    }

    private Runnable producerTask(int producer) {
        return () -> {
            for (int i = 0; i < eventsPerProducer && !Thread.currentThread().isInterrupted(); i++) {
                RoomRef room = rooms.get((producer + i) % rooms.size());
                if (i % 2 == 0) {
                    bookings.createBooking(room.floor(), room.roomId());
                } else {
                    bookings.cancelBooking(room.floor(), room.roomId());
                }
            }
        };
    }

    private void stop(ExecutorService executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(STOP_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}