package hotel.visitor;

import hotel.model.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonExportVisitorTest {

    @Test
    void exports_nested_structure() {
        StandardRoom room = new StandardRoom("101", 100);
        room.setOccupied(true);
        Hotel hotel = new Hotel("test", List.of(new Floor(1, List.of(room, new Restaurant("R1", 50)))));

        String json = hotel.accept(new JsonExportVisitor());

        assertEquals("""
                {"type":"hotel","name":"test","floors":[{"type":"floor","number":1,"units":[{"type":"standard_room","id":"101","price":100.0,"occupied":true,"dirty":false},{"type":"restaurant","id":"R1","dailyRevenue":50.0}]}]}""", json);
    }

    @Test
    void exports_suite_with_bedrooms() {
        String json = new Suite("S1", 300, 2).accept(new JsonExportVisitor());

        assertEquals("{\"type\":\"suite\",\"id\":\"S1\",\"price\":300.0,\"occupied\":false,\"dirty\":false,\"bedrooms\":2}", json);
    }
}