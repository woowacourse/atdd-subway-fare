package wooteco.subway.path.domain;

public class Fare {

    public static final int BASIC_DISTANCE = 10;
    public static final int MIDDLE_DISTANCE = 50;

    public static final int BASIC_FARE = 1250;
    public static final int OVER_FARE = 100;

    public static int calculateFare(int distance) {
        if (distance <= BASIC_DISTANCE) {
            return BASIC_FARE;
        }
        return calculateFarePerDistance(distance - BASIC_DISTANCE) + BASIC_FARE;
    }

    public static int calculateFareWithLine(int distance, int overLineFare) {
        return calculateFare(distance) + overLineFare;
    }

    private static int calculateFarePerDistance(int distance) {
        if (distance <= MIDDLE_DISTANCE) {
            return calculateOverFare(distance, 5, OVER_FARE);
        }
        return calculateOverFare(distance, 8, OVER_FARE);
    }


    private static int calculateOverFare(int distance, int overDistance, int overFare) {
        return (int) ((Math.ceil((distance - 1) / overDistance) + 1) * overFare);
    }
}
