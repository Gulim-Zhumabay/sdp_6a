package hotel.listener;

import hotel.event.CleaningPlanEvent;
import hotel.observer.Observer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CleaningTaskBoard implements Observer<CleaningPlanEvent> {
    private final Map<Integer, List<String>> plans = new ConcurrentHashMap<>();

    @Override
    public void onEvent(CleaningPlanEvent event) {
        plans.put(event.floor(), event.roomIds());
    }

    public List<String> planFor(int floor) {
        return plans.getOrDefault(floor, List.of());
    }
}