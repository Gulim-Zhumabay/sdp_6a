package hotel.event;

public record OccupancyEvent(EventType type, int floor, int occupiedRooms) implements Event {

    public OccupancyEvent(int floor, int occupiedRooms) {
        this(EventType.floor_occupancy_changed, floor, occupiedRooms);
    }
}