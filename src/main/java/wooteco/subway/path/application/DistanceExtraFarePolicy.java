package wooteco.subway.path.application;

import wooteco.subway.path.domain.Fare;
import wooteco.subway.path.exception.InvalidDistanceArgumentException;

import java.util.Arrays;

public enum DistanceExtraFarePolicy {
    UNDER_10KM(0, 0.0),
    UNDER_50KM(10, 5.0),
    OVER_50KM(50, 8.0);

    private final int lowerBound;
    private final double chargeUnit;

    DistanceExtraFarePolicy(int lowerBound, double chargeUnit) {
        this.lowerBound = lowerBound;
        this.chargeUnit = chargeUnit;
    }

    public static Fare calculateFareByDistance(int distance) {
        if (distance <= 10) {
            return FarePolicy.BASIC_FARE;
        }

        DistanceExtraFarePolicy lastInstance = Arrays.stream(values())
                .filter(policy -> policy.lowerBound < distance)
                .reduce((first, second) -> second)
                .orElseThrow(InvalidDistanceArgumentException::new);

        return lastInstance.calculateFare(distance);
    }

    private Fare calculateFare(int distance) {
        return calculateExtraFare(distance - this.lowerBound).add(calculateFareByDistance(this.lowerBound));
    }

    private Fare calculateExtraFare(int distance) {
        int extraFare = (int) (Math.ceil(distance / this.chargeUnit) * 100);
        return new Fare(extraFare);
    }
}
