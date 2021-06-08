package wooteco.subway.path.application;

import org.springframework.stereotype.Service;

@Service
public class DefaultFareCalculator implements FareCalculator {

    private static final int BASIC_FARE = 1250;
    private static final int ADDITIONAL_FARE = 100;
    private static final int TEN_KILOMETER = 10;
    private static final int FIFTY_KILOMETER = 50;
    private static final int DISTANCE_BY_FARE_POLICY_FOR_TEN_KILOMETER = 5;
    private static final int DISTANCE_BY_FARE_POLICY_FOR_FIFTY_KILOMETER = 8;

    public int calculateFare(int distance, int extraFare) {
        int fare = BASIC_FARE;
        if (distance > TEN_KILOMETER) {
            fare += calculateOverFare(
                distance - TEN_KILOMETER,
                DISTANCE_BY_FARE_POLICY_FOR_TEN_KILOMETER
            );
        }
        if (distance > FIFTY_KILOMETER) {
            fare += calculateOverFare(
                distance - FIFTY_KILOMETER,
                DISTANCE_BY_FARE_POLICY_FOR_FIFTY_KILOMETER
            );
        }
        return fare + extraFare;
    }

    private int calculateOverFare(int distance, int distanceByFarePolicy) {
        return (int) ((Math.ceil((distance - 1) / distanceByFarePolicy) + 1) * ADDITIONAL_FARE);
    }
}
