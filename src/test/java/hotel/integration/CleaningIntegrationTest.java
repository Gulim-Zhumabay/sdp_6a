package hotel.integration;

import hotel.event.CleaningPlanEvent;
import hotel.event.EventType;
import hotel.event.RoomEvent;
import hotel.listener.CleaningPlanner;
import hotel.listener.CleaningTaskBoard;
import hotel.listener.HotelStateUpdater;
import hotel.model.Floor;
import hotel.model.Hotel;
import hotel.model.StandardRoom;
import hotel.observer.BookingSource;
import hotel.observer.ListenerRegistry;
import hotel.observer.RoomStatusSource;
import hotel.service.FloorLocator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CleaningIntegrationTest {
    private final BookingSource bookings = new BookingSource(new ListenerRegistry<RoomEvent>());
    private final RoomStatusSource roomStatus = new RoomStatusSource(new ListenerRegistry<RoomEvent>());
    private final CleaningTaskBoard board = new CleaningTaskBoard();
    private final List<CleaningPlanEvent> published = new ArrayList<>();

    CleaningIntegrationTest() {
        StandardRoom r101 = new StandardRoom("101", 100);
        StandardRoom r102 = new StandardRoom("102", 100);
        StandardRoom r201 = new StandardRoom("201", 100);
        r201.setDirty(true);
        Hotel hotel = new Hotel("test", List.of(
                new Floor(1, List.of(r101, r102)),
                new Floor(2, List.of(r201))));
        FloorLocator floors = new FloorLocator(hotel);
        HotelStateUpdater updater = new HotelStateUpdater(floors, roomStatus);
        CleaningPlanner planner = new CleaningPlanner(new ListenerRegistry<CleaningPlanEvent>(), floors);

        bookings.subscribe(EventType.guest_checked_out, updater);
        roomStatus.subscribe(EventType.room_cleaned, updater);
        roomStatus.subscribe(EventType.room_dirty, planner);
        roomStatus.subscribe(EventType.room_cleaned, planner);
        planner.subscribe(EventType.cleaning_plan_ready, board);
        planner.subscribe(EventType.cleaning_plan_ready, published::add);
    }

    @Test
    void checkout_creates_plan_for_that_floor_only() {
        bookings.checkOut(1, "101");

        assertEquals(List.of("101"), board.planFor(1));
        assertEquals(List.of(), board.planFor(2));
        assertEquals(1, published.size());
    }

    @Test
    void plan_collects_all_dirty_rooms_of_the_floor() {
        bookings.checkOut(1, "101");
        bookings.checkOut(1, "102");

        assertEquals(List.of("101", "102"), board.planFor(1));
    }

    @Test
    void cleaning_a_room_clears_it_from_the_plan() {
        bookings.checkOut(1, "101");
        roomStatus.markCleaned(1, "101");

        assertEquals(List.of(), board.planFor(1));
    }

    @Test
    void cleaned_room_leaves_the_plan() {
        bookings.checkOut(1, "101");
        roomStatus.markCleaned(1, "101");
        bookings.checkOut(1, "102");

        assertEquals(List.of("102"), board.planFor(1));
    }

}