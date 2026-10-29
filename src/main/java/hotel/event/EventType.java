package hotel.event;

public enum EventType {
    booking_created,
    booking_cancelled,
    guest_checked_in,
    guest_checked_out,
    room_dirty,
    room_cleaned,
    cleaning_plan_ready,
    floor_occupancy_changed
}