package hotel.observer;

import hotel.event.Event;

public interface Observer<E extends Event> {
    void onEvent(E event);
}