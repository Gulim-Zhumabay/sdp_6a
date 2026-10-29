package hotel.visitor;

public record OccupancyReport(int totalUnits, int occupiedUnits) {

    public double rate() {
        return totalUnits == 0 ? 0 : (double) occupiedUnits / totalUnits;
    }
}