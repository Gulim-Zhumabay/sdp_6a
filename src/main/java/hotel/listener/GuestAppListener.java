package hotel.listener;

import hotel.event.RoomEvent;
import hotel.observer.Observer;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GuestAppListener implements Observer<RoomEvent> {
    private final Queue<String> notifications = new ConcurrentLinkedQueue<>();

    @Override
    public void onEvent(RoomEvent event) {
        switch (event.type()) {
            case booking_created -> notifications.add("room " + event.roomId() + ": booking confirmed");
            case booking_cancelled -> notifications.add("room " + event.roomId() + ": booking cancelled");
            case guest_checked_in -> notifications.add("room " + event.roomId() + ": welcome");
            default -> { }
        }
    }

    public List<String> notifications() {
        return List.copyOf(notifications);
    }
}