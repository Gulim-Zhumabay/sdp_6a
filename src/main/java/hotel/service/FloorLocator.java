package hotel.service;

import hotel.model.Floor;
import hotel.model.Hotel;

import java.util.HashMap;
import java.util.Map;

public class FloorLocator {
    private final Map<Integer, Floor> floors = new HashMap<>();

    public FloorLocator(Hotel hotel) {
        for (Floor floor : hotel.getFloors()) {
            floors.put(floor.getNumber(), floor);
        }
    }

    public Floor floor(int number) {
        Floor floor = floors.get(number);
        if (floor == null) {
            throw new IllegalArgumentException("no floor " + number);
        }
        return floor;
    }
}