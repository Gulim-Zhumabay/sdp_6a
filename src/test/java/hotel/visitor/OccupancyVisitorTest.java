package hotel.visitor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OccupancyVisitorTest {

    @Test
    void counts_units_and_occupied_across_whole_tree() {
        OccupancyReport report = SampleHotel.create().accept(new OccupancyVisitor());

        assertEquals(4, report.totalUnits());
        assertEquals(3, report.occupiedUnits());
        assertEquals(0.75, report.rate());
    }
}