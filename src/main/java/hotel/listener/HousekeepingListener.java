package hotel.listener;

import hotel.event.RoomEvent;
import hotel.observer.Observer;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class HousekeepingListener implements Observer<RoomEvent> {
    private final Set<String> dirtyRooms = ConcurrentHashMap.newKeySet();

    @Override
    public void onEvent(RoomEvent event) {
        switch (event.type()) {
            case room_dirty -> dirtyRooms.add(event.roomId());
            case room_cleaned -> dirtyRooms.remove(event.roomId());
            default -> { }
        }
    }

    public Set<String> dirtyRooms() {
        return Set.copyOf(dirtyRooms);
    }
}