package hotel.observer;

import hotel.event.EventType;
import hotel.event.RoomEvent;

public class RoomStatusSource extends EventSource<RoomEvent> {

    public RoomStatusSource(ListenerRegistry<RoomEvent> registry) {
        super(registry);
    }

    public void markDirty(int floor, String roomId) {
        publish(new RoomEvent(EventType.room_dirty, floor, roomId));
    }

    public void markCleaned(int floor, String roomId) {
        publish(new RoomEvent(EventType.room_cleaned, floor, roomId));
    }
}