package hotel.listener;

import hotel.event.RoomEvent;
import hotel.observer.Observer;

import java.util.concurrent.atomic.AtomicInteger;

public class RevenueManagerListener implements Observer<RoomEvent> {
    private final AtomicInteger activeBookings = new AtomicInteger();

    @Override
    public void onEvent(RoomEvent event) {
        switch (event.type()) {
            case booking_created -> activeBookings.incrementAndGet();
            case booking_cancelled -> activeBookings.decrementAndGet();
            default -> { }
        }
    }

    public int activeBookings() {
        return activeBookings.get();
    }
}