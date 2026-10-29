package hotel.visitor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RevenueVisitorTest {

    @Test
    void sums_income_of_occupied_units_and_restaurant() {
        double revenue = SampleHotel.create().accept(new RevenueVisitor());

        assertEquals(1900.0, revenue);
    }
}