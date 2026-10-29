package hotel.visitor;

import hotel.model.*;

public interface Visitor<R> {
    R visitHotel(Hotel hotel);

    R visitFloor(Floor floor);

    R visitStandardRoom(StandardRoom room);

    R visitSuite(Suite suite);

    R visitConferenceHall(ConferenceHall hall);

    R visitRestaurant(Restaurant restaurant);
}