package hotel.listener;

import hotel.event.EventType;
import hotel.event.RoomEvent;
import hotel.observer.BookingSource;
import hotel.observer.ListenerRegistry;
import hotel.observer.RoomStatusSource;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ListenersTest {
    private final BookingSource bookings = new BookingSource(new ListenerRegistry<RoomEvent>());
    private final RoomStatusSource roomStatus = new RoomStatusSource(new ListenerRegistry<RoomEvent>());

    @Test
    void front_desk_tracks_occupied_rooms() {
        FrontDeskListener frontDesk = new FrontDeskListener();
        bookings.subscribe(EventType.guest_checked_in, frontDesk);
        bookings.subscribe(EventType.guest_checked_out, frontDesk);

        bookings.checkIn(1, "101");
        bookings.checkIn(2, "201");
        bookings.checkOut(1, "101");

        assertEquals(1, frontDesk.occupiedCount());
    }

    @Test
    void housekeeping_tracks_dirty_rooms() {
        HousekeepingListener housekeeping = new HousekeepingListener();
        roomStatus.subscribe(EventType.room_dirty, housekeeping);
        roomStatus.subscribe(EventType.room_cleaned, housekeeping);

        roomStatus.markDirty(1, "101");
        roomStatus.markDirty(1, "102");
        roomStatus.markCleaned(1, "101");

        assertEquals(Set.of("102"), housekeeping.dirtyRooms());
    }

    @Test
    void revenue_manager_counts_active_bookings() {
        RevenueManagerListener revenue = new RevenueManagerListener();
        bookings.subscribe(EventType.booking_created, revenue);
        bookings.subscribe(EventType.booking_cancelled, revenue);

        bookings.createBooking(1, "101");
        bookings.createBooking(1, "102");
        bookings.createBooking(2, "201");
        bookings.cancelBooking(2, "201");

        assertEquals(2, revenue.activeBookings());
    }

    @Test
    void guest_app_gets_confirmation() {
        GuestAppListener guestApp = new GuestAppListener();
        bookings.subscribe(EventType.booking_created, guestApp);

        bookings.createBooking(1, "101");

        assertEquals(List.of("room 101: booking confirmed"), guestApp.notifications());
    }
}