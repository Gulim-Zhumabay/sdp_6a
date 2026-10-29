package hotel.observer;

import hotel.event.Event;
import hotel.event.EventType;

public interface Subject<E extends Event> {
    void subscribe(EventType type, Observer<E> observer);

    void unsubscribe(EventType type, Observer<E> observer);
}