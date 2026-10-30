package hotel.visitor;

import hotel.model.Bookable;
import hotel.model.Floor;
import hotel.model.Restaurant;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MostExpensiveUnitVisitorTest {

    @Test
    void finds_most_expensive_unit_of_whole_hotel() {
        Optional<Bookable> best = SampleHotel.create().accept(new MostExpensiveUnitVisitor());

        assertEquals("H1", best.orElseThrow().getId());
    }

    @Test
    void finds_most_expensive_unit_of_one_floor() {
        Optional<Bookable> best = SampleHotel.create().getFloors().get(0).accept(new MostExpensiveUnitVisitor());

        assertEquals("103", best.orElseThrow().getId());
    }

    @Test
    void returns_empty_when_nothing_has_a_price() {
        Floor onlyRestaurant = new Floor(1, List.of(new Restaurant("R1", 100)));

        assertEquals(Optional.empty(), onlyRestaurant.accept(new MostExpensiveUnitVisitor()));
    }
}