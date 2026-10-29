package hotel.observer;

import hotel.event.EventType;
import hotel.event.RoomEvent;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ListenerRegistryTest {
    private final ListenerRegistry<RoomEvent> registry = new ListenerRegistry<>();

    @Test
    void delivers_only_subscribed_type() {
        List<RoomEvent> received = new ArrayList<>();
        registry.add(EventType.room_dirty, received::add);

        registry.publish(new RoomEvent(EventType.room_dirty, 1, "101"));
        registry.publish(new RoomEvent(EventType.room_cleaned, 1, "101"));

        assertEquals(1, received.size());
    }

    @Test
    void listener_can_unsubscribe_inside_handler() {
        AtomicInteger count = new AtomicInteger();
        Observer<RoomEvent> once = new Observer<>() {
            @Override
            public void onEvent(RoomEvent event) {
                count.incrementAndGet();
                registry.remove(EventType.room_dirty, this);
            }
        };
        registry.add(EventType.room_dirty, once);

        registry.publish(new RoomEvent(EventType.room_dirty, 1, "101"));
        registry.publish(new RoomEvent(EventType.room_dirty, 1, "101"));

        assertEquals(1, count.get());
    }
}