package hotel.visitor;

import hotel.model.*;

import java.util.List;

public class JsonExportVisitor implements Visitor<String> {

    @Override
    public String visitHotel(Hotel hotel) {
        return "{\"type\":\"hotel\",\"name\":\"" + hotel.getName()
                + "\",\"floors\":" + toArray(hotel.getFloors()) + "}";
    }

    @Override
    public String visitFloor(Floor floor) {
        return "{\"type\":\"floor\",\"number\":" + floor.getNumber()
                + ",\"units\":" + toArray(floor.getUnits()) + "}";
    }

    @Override
    public String visitStandardRoom(StandardRoom room) {
        return "{" + bookableFields("standard_room", room) + "}";
    }

    @Override
    public String visitSuite(Suite suite) {
        return "{" + bookableFields("suite", suite) + ",\"bedrooms\":" + suite.getBedrooms() + "}";
    }

    @Override
    public String visitConferenceHall(ConferenceHall hall) {
        return "{" + bookableFields("conference_hall", hall) + ",\"capacity\":" + hall.getCapacity() + "}";
    }

    @Override
    public String visitRestaurant(Restaurant restaurant) {
        return "{\"type\":\"restaurant\",\"id\":\"" + restaurant.getId()
                + "\",\"dailyRevenue\":" + restaurant.getDailyRevenue() + "}";
    }

    private String bookableFields(String type, Bookable bookable) {
        return "\"type\":\"" + type + "\",\"id\":\"" + bookable.getId()
                + "\",\"price\":" + bookable.getPrice()
                + ",\"occupied\":" + bookable.isOccupied()
                + ",\"dirty\":" + bookable.isDirty();
    }

    private String toArray(List<? extends Element> items) {
        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < items.size(); i++) {
            if (i > 0) {
                result.append(",");
            }
            result.append(items.get(i).accept(this));
        }
        return result.append("]").toString();
    }
}