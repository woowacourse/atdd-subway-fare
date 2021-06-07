package wooteco.subway.domain.fareCalculator;

public class DefaultFareCalculator implements FareCalculator {

    private static final int DEFAULT_FARE = 1250;
    private static final int TEN_KILOMETER = 10;
    private static final int FIFTY_KILOMETER = 50;
    private static final int DISTANCE_BY_POLICY_FOR_TEN_KILOMETER = 5;
    private static final int DISTANCE_BY_POLICY_FOR_FIFTY_KILOMETER = 8;
    private static final int ADDITIONAL_FARE = 100;

    public int calculateFare(int distance, int extraFare) {
        int fare = DEFAULT_FARE;
        if (distance > TEN_KILOMETER) {
            int maxDistance = Math.min(distance, FIFTY_KILOMETER);
            fare += calculateOverFare(maxDistance - TEN_KILOMETER,
                DISTANCE_BY_POLICY_FOR_TEN_KILOMETER);
        }
        if (distance > FIFTY_KILOMETER) {
            fare += calculateOverFare(distance - FIFTY_KILOMETER,
                DISTANCE_BY_POLICY_FOR_FIFTY_KILOMETER);
        }
        return fare + extraFare;
    }

    private int calculateOverFare(int distance, int distancePolicy) {
        return (int) ((Math.ceil((distance - 1) / distancePolicy) + 1) * ADDITIONAL_FARE);
    }
}