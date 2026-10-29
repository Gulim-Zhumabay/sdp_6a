package hotel.event;

public record RoomEvent(EventType type, int floor, String roomId) implements Event {
}