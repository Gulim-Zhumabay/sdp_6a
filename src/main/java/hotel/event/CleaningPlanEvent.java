package hotel.event;

import java.util.List;

public record CleaningPlanEvent(EventType type, int floor, List<String> roomIds) implements Event {

    public CleaningPlanEvent {
        roomIds = List.copyOf(roomIds);
    }

    public CleaningPlanEvent(int floor, List<String> roomIds) {
        this(EventType.cleaning_plan_ready, floor, roomIds);
    }
}