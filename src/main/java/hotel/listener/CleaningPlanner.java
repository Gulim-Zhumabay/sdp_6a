package hotel.listener;

import hotel.event.CleaningPlanEvent;
import hotel.event.RoomEvent;
import hotel.model.Bookable;
import hotel.observer.EventSource;
import hotel.observer.ListenerRegistry;
import hotel.observer.Observer;
import hotel.service.FloorLocator;
import hotel.visitor.CleaningPlanVisitor;

import java.util.List;

public class CleaningPlanner extends EventSource<CleaningPlanEvent> implements Observer<RoomEvent> {
    private final FloorLocator floors;

    public CleaningPlanner(ListenerRegistry<CleaningPlanEvent> registry, FloorLocator floors) {
        super(registry);
        this.floors = floors;
    }

    @Override
    public void onEvent(RoomEvent event) {
        List<Bookable> dirty = floors.floor(event.floor()).accept(new CleaningPlanVisitor());
        List<String> roomIds = dirty.stream().map(Bookable::getId).toList();
        publish(new CleaningPlanEvent(event.floor(), roomIds));
    }
}