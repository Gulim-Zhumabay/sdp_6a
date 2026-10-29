package hotel.observer;

import hotel.event.Event;
import hotel.event.EventType;

public abstract class EventSource<E extends Event> implements Subject<E> {
    private final ListenerRegistry<E> registry;

    protected EventSource(ListenerRegistry<E> registry) {
        this.registry = registry;
    }

    @Override
    public void subscribe(EventType type, Observer<E> observer) {
        registry.add(type, observer);
    }

    @Override
    public void unsubscribe(EventType type, Observer<E> observer) {
        registry.remove(type, observer);
    }

    protected void publish(E event) {
        registry.publish(event);
    }
}