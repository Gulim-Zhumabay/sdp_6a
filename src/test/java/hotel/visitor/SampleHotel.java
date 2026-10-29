package hotel.visitor;

import hotel.model.*;

import java.util.List;

final class SampleHotel {

    private SampleHotel() {
    }

    static Hotel create() {
        StandardRoom r101 = new StandardRoom("101", 100);
        StandardRoom r102 = new StandardRoom("102", 100);
        Suite s103 = new Suite("103", 300, 2);
        ConferenceHall hall = new ConferenceHall("H1", 500, 50);
        Restaurant restaurant = new Restaurant("R1", 1000);
        r101.setOccupied(true);
        s103.setOccupied(true);
        hall.setOccupied(true);
        r102.setDirty(true);
        hall.setDirty(true);
        return new Hotel("sample", List.of(
                new Floor(1, List.of(r101, r102, s103)),
                new Floor(2, List.of(hall, restaurant))));
    }
}