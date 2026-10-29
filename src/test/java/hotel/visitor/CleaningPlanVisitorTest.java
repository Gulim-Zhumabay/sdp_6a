package hotel.visitor;

import hotel.model.Bookable;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CleaningPlanVisitorTest {

    @Test
    void lists_dirty_units_of_whole_hotel() {
        List<Bookable> plan = SampleHotel.create().accept(new CleaningPlanVisitor());

        assertEquals(List.of("102", "H1"), plan.stream().map(Bookable::getId).toList());
    }

    @Test
    void lists_dirty_units_of_one_floor_only() {
        List<Bookable> plan = SampleHotel.create().getFloors().get(0).accept(new CleaningPlanVisitor());

        assertEquals(List.of("102"), plan.stream().map(Bookable::getId).toList());
    }
}