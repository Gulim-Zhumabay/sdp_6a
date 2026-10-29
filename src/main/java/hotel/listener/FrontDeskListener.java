package hotel.listener;

import hotel.event.RoomEvent;
import hotel.observer.Observer;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class FrontDeskListener implements Observer<RoomEvent> {
    private final Set<String> occupiedRooms = ConcurrentHashMap.newKeySet();

    @Override
    public void onEvent(RoomEvent event) {
        switch (event.type()) {
            case guest_checked_in -> occupiedRooms.add(event.roomId());
            case guest_checked_out -> occupiedRooms.remove(event.roomId());
            default -> { }
        }
    }

    public int occupiedCount() {
        return occupiedRooms.size();
    }
}