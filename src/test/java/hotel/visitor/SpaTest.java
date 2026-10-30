package hotel.visitor;

import hotel.model.Bookable;
import hotel.model.Floor;
import hotel.model.Hotel;
import hotel.model.Spa;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SpaTest {

    private static Spa spa() {
        Spa spa = new Spa("SPA1", 80, 4);
        spa.setOccupied(true);
        spa.setDirty(true);
        return spa;
    }

    private static Hotel hotelWithSpa() {
        return new Hotel("test", List.of(new Floor(1, List.of(spa()))));
    }

    @Test
    void spa_counts_in_revenue_and_occupancy() {
        assertEquals(80.0, hotelWithSpa().accept(new RevenueVisitor()));
        assertEquals(new OccupancyReport(1, 1), hotelWithSpa().accept(new OccupancyVisitor()));
    }

    @Test
    void spa_is_found_in_cleaning_plan_and_lookup() {
        List<Bookable> plan = hotelWithSpa().accept(new CleaningPlanVisitor());

        assertEquals(List.of("SPA1"), plan.stream().map(Bookable::getId).toList());
        assertEquals("SPA1", hotelWithSpa().accept(new RoomLookupVisitor("SPA1")).orElseThrow().getId());
    }

    @Test
    void spa_is_exported_to_json() {
        String json = spa().accept(new JsonExportVisitor());

        assertEquals("{\"type\":\"spa\",\"id\":\"SPA1\",\"price\":80.0,\"occupied\":true,\"dirty\":true,\"treatmentRooms\":4}", json);
    }
}