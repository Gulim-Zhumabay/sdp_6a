package hotel.listener;

import hotel.event.OccupancyEvent;
import hotel.event.RoomEvent;
import hotel.observer.EventSource;
import hotel.observer.ListenerRegistry;
import hotel.observer.Observer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FloorOccupancyAggregator extends EventSource<OccupancyEvent> implements Observer<RoomEvent> {
    private final Map<Integer, Integer> occupiedByFloor = new ConcurrentHashMap<>();

    public FloorOccupancyAggregator(ListenerRegistry<OccupancyEvent> registry) {
        super(registry);
    }

    @Override
    public void onEvent(RoomEvent event) {
        switch (event.type()) {
            case guest_checked_in -> update(event.floor(), 1);
            case guest_checked_out -> update(event.floor(), -1);
            default -> { }
        }
    }

    private void update(int floor, int delta) {
        int occupied = occupiedByFloor.compute(floor, (f, old) -> Math.max(0, (old == null ? 0 : old) + delta));
        publish(new OccupancyEvent(floor, occupied));
    }
}