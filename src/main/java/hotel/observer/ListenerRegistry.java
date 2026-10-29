package hotel.observer;

import hotel.event.Event;
import hotel.event.EventType;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ListenerRegistry<E extends Event> {
    private final Map<EventType, List<Observer<E>>> listeners = new ConcurrentHashMap<>();

    public void add(EventType type, Observer<E> observer) {
        listeners.computeIfAbsent(type, t -> new CopyOnWriteArrayList<>()).add(observer);
    }

    public void remove(EventType type, Observer<E> observer) {
        List<Observer<E>> list = listeners.get(type);
        if (list != null) {
            list.remove(observer);
        }
    }

    public void publish(E event) {
        for (Observer<E> observer : listeners.getOrDefault(event.type(), List.of())) {
            observer.onEvent(event);
        }
    }
}