package hotel.listener;

import hotel.event.RoomEvent;
import hotel.model.Bookable;
import hotel.observer.Observer;
import hotel.observer.RoomStatusSource;
import hotel.service.FloorLocator;
import hotel.visitor.RoomLookupVisitor;

public class HotelStateUpdater implements Observer<RoomEvent> {
    private final FloorLocator floors;
    private final RoomStatusSource roomStatus;

    public HotelStateUpdater(FloorLocator floors, RoomStatusSource roomStatus) {
        this.floors = floors;
        this.roomStatus = roomStatus;
    }

    @Override
    public void onEvent(RoomEvent event) {
        Bookable room = find(event);
        switch (event.type()) {
            case guest_checked_in -> room.setOccupied(true);
            case guest_checked_out -> {
                room.setOccupied(false);
                room.setDirty(true);
                roomStatus.markDirty(event.floor(), event.roomId());
            }
            case room_cleaned -> room.setDirty(false);
            default -> { }
        }
    }

    private Bookable find(RoomEvent event) {
        return floors.floor(event.floor())
                .accept(new RoomLookupVisitor(event.roomId()))
                .orElseThrow(() -> new IllegalArgumentException("no room " + event.roomId()));
    }
}