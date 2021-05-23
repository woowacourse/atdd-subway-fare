package wooteco.subway.fare.domain;

import java.util.function.BiFunction;

public class Fare {
    private static final int DEFAULT_FARE = 1250;

    private final int totalDistance;
    private final BiFunction<Integer, Integer, Integer> extraFareCalculator;

    public Fare(int totalDistance) {
        this.totalDistance = totalDistance;
        this.extraFareCalculator = (distance, unitDistance) ->
                (int) Math.ceil(((double) (distance)) / unitDistance) * 100;
    }

    public int calculateFare() {
        if (totalDistance <= 10) {
            return DEFAULT_FARE;
        }
        if (totalDistance <= 50) {
            return DEFAULT_FARE + calculateExtraFare(totalDistance - 10, 5);
        }
        return DEFAULT_FARE + calculateExtraFare(40, 5) +
                calculateExtraFare(totalDistance - 50, 8);
    }

    private int calculateExtraFare(int distance, int unitDistance) {
        return extraFareCalculator.apply(distance, unitDistance);
    }
}
