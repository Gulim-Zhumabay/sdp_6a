package hotel;

import hotel.event.CleaningPlanEvent;
import hotel.event.Event;
import hotel.event.EventType;
import hotel.event.OccupancyEvent;
import hotel.event.RoomEvent;
import hotel.listener.CleaningPlanner;
import hotel.listener.CleaningTaskBoard;
import hotel.listener.FloorOccupancyAggregator;
import hotel.listener.FrontDeskListener;
import hotel.listener.GuestAppListener;
import hotel.listener.HotelStateUpdater;
import hotel.listener.HousekeepingListener;
import hotel.listener.RevenueManagerListener;
import hotel.model.ConferenceHall;
import hotel.model.Floor;
import hotel.model.Hotel;
import hotel.model.Restaurant;
import hotel.model.StandardRoom;
import hotel.model.Suite;
import hotel.observer.BookingSource;
import hotel.observer.ListenerRegistry;
import hotel.observer.Observer;
import hotel.observer.RoomStatusSource;
import hotel.observer.Subject;
import hotel.service.FloorLocator;
import hotel.simulation.RoomRef;
import hotel.simulation.SimulationRunner;
import hotel.visitor.JsonExportVisitor;
import hotel.visitor.OccupancyVisitor;
import hotel.visitor.RevenueVisitor;

import java.util.List;

public class Main {
    private static final int DEFAULT_PRODUCERS = 3;
    private static final int EVENTS_PER_PRODUCER = 1000;

    public static void main(String[] args) {
        int producers = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PRODUCERS;

        Hotel hotelTree = buildHotel();
        FloorLocator floors = new FloorLocator(hotelTree);

        BookingSource bookings = new BookingSource(new ListenerRegistry<RoomEvent>());
        RoomStatusSource roomStatus = new RoomStatusSource(new ListenerRegistry<RoomEvent>());
        FloorOccupancyAggregator occupancy = new FloorOccupancyAggregator(new ListenerRegistry<OccupancyEvent>());
        CleaningPlanner planner = new CleaningPlanner(new ListenerRegistry<CleaningPlanEvent>(), floors);

        FrontDeskListener frontDesk = new FrontDeskListener();
        HousekeepingListener housekeeping = new HousekeepingListener();
        RevenueManagerListener revenueManager = new RevenueManagerListener();
        GuestAppListener guestApp = new GuestAppListener();
        HotelStateUpdater stateUpdater = new HotelStateUpdater(floors, roomStatus);
        CleaningTaskBoard board = new CleaningTaskBoard();

        subscribeAll(bookings, revenueManager, EventType.booking_created, EventType.booking_cancelled);
        subscribeAll(bookings, guestApp,
                EventType.booking_created, EventType.booking_cancelled, EventType.guest_checked_in);
        subscribeAll(bookings, frontDesk, EventType.guest_checked_in, EventType.guest_checked_out);
        subscribeAll(bookings, stateUpdater, EventType.guest_checked_in, EventType.guest_checked_out);
        subscribeAll(bookings, occupancy, EventType.guest_checked_in, EventType.guest_checked_out);
        subscribeAll(roomStatus, housekeeping, EventType.room_dirty, EventType.room_cleaned);
        subscribeAll(roomStatus, stateUpdater, EventType.room_cleaned);
        subscribeAll(roomStatus, planner, EventType.room_dirty, EventType.room_cleaned);
        subscribeAll(planner, board, EventType.cleaning_plan_ready);

        bookings.checkIn(1, "101");
        bookings.checkIn(1, "102");
        bookings.checkIn(2, "201");
        System.out.println("occupied rooms: " + frontDesk.occupiedCount());
        System.out.println("revenue: " + hotelTree.accept(new RevenueVisitor()));
        System.out.println("occupancy: " + hotelTree.accept(new OccupancyVisitor()));

        bookings.checkOut(1, "101");
        System.out.println("cleaning plan floor 1 after checkout: " + board.planFor(1));
        roomStatus.markCleaned(1, "101");
        System.out.println("cleaning plan floor 1 after cleaning: " + board.planFor(1));

        List<RoomRef> rooms = List.of(
                new RoomRef(1, "101"), new RoomRef(1, "102"), new RoomRef(1, "103"),
                new RoomRef(2, "201"), new RoomRef(2, "202"), new RoomRef(2, "203"));
        new SimulationRunner(bookings, rooms, producers, EVENTS_PER_PRODUCER).run();
        System.out.println("active bookings after " + producers + " producers: " + revenueManager.activeBookings());
        System.out.println("guest app notifications: " + guestApp.notifications().size());

        System.out.println(hotelTree.accept(new JsonExportVisitor()));
    }

    private static Hotel buildHotel() {
        return new Hotel("grand", List.of(
                new Floor(1, List.of(
                        new StandardRoom("101", 100),
                        new StandardRoom("102", 100),
                        new Suite("103", 300, 2))),
                new Floor(2, List.of(
                        new StandardRoom("201", 120),
                        new StandardRoom("202", 120),
                        new Suite("203", 350, 3))),
                new Floor(3, List.of(
                        new ConferenceHall("H1", 500, 50),
                        new Restaurant("R1", 1000)))));
    }

    private static <E extends Event> void subscribeAll(Subject<E> subject, Observer<E> observer, EventType... types) {
        for (EventType type : types) {
            subject.subscribe(type, observer);
        }
    }
}