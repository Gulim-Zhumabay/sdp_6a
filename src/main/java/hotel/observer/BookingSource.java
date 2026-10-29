package hotel.observer;

import hotel.event.EventType;
import hotel.event.RoomEvent;

public class BookingSource extends EventSource<RoomEvent> {

    public BookingSource(ListenerRegistry<RoomEvent> registry) {
        super(registry);
    }

    public void createBooking(int floor, String roomId) {
        publish(new RoomEvent(EventType.booking_created, floor, roomId));
    }

    public void cancelBooking(int floor, String roomId) {
        publish(new RoomEvent(EventType.booking_cancelled, floor, roomId));
    }

    public void checkIn(int floor, String roomId) {
        publish(new RoomEvent(EventType.guest_checked_in, floor, roomId));
    }

    public void checkOut(int floor, String roomId) {
        publish(new RoomEvent(EventType.guest_checked_out, floor, roomId));
    }
}