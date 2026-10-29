package hotel.visitor;

import hotel.model.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RevenueVisitorTest {

    @Test
    void sums_income_of_occupied_units_and_restaurant() {
        StandardRoom r101 = new StandardRoom("101", 100);
        StandardRoom r102 = new StandardRoom("102", 100);
        Suite s103 = new Suite("103", 300, 2);
        ConferenceHall hall = new ConferenceHall("H1", 500, 50);
        Restaurant restaurant = new Restaurant("R1", 1000);
        r101.setOccupied(true);
        s103.setOccupied(true);
        hall.setOccupied(true);
        Hotel hotel = new Hotel("test", List.of(
                new Floor(1, List.of(r101, r102, s103)),
                new Floor(2, List.of(hall, restaurant))));

        double revenue = hotel.accept(new RevenueVisitor());

        assertEquals(1900.0, revenue);
    }
}