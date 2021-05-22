package wooteco.subway.path.domain;

public class SubwayPathFare {
    private static final int EXTRA_FARE_SECOND_BOUNDARY = 50;
    private static final int EXTRA_FARE_FIRST_BOUNDARY = 10;
    private static final int EXTRA_FARE_RATE_AFTER_SECOND_BOUNDARY = 8;
    private static final int EXTRA_FARE_RATE_AFTER_FIRST_BOUNDARY = 5;
    private static final int BASE_FARE = 1250;
    private static final int FARE_BETWEEN_10_TO_50 = 800;
    private static final int FARE_RATE = 100;

    private int distance;
    private int lineFare;

    public SubwayPathFare(int distance, int lineFare) {
        this.distance = distance;
        this.lineFare = lineFare;
    }

    public int getFare() {
        return calculateFareByDistance() + lineFare;
    }

    public int calculateFareByDistance() {
        int totalDistance = distance;
        if (totalDistance > EXTRA_FARE_SECOND_BOUNDARY) {
            return BASE_FARE + FARE_BETWEEN_10_TO_50 + (int)
                    ((Math.ceil((totalDistance - EXTRA_FARE_SECOND_BOUNDARY - 1) /
                            EXTRA_FARE_RATE_AFTER_SECOND_BOUNDARY)) + 1) * FARE_RATE;
        }
        if (EXTRA_FARE_FIRST_BOUNDARY < totalDistance && totalDistance <= EXTRA_FARE_SECOND_BOUNDARY) {
            return BASE_FARE + (int)
                    ((Math.ceil((totalDistance - EXTRA_FARE_FIRST_BOUNDARY - 1) /
                            EXTRA_FARE_RATE_AFTER_FIRST_BOUNDARY)) + 1) * FARE_RATE;
        }
        return BASE_FARE;
    }
}
