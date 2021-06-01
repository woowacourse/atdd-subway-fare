package wooteco.subway.path.domain;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum DistanceFareRule {
    MIDDLE_DISTANCE(
            distance -> distance > 10,
            distance -> {
                if (distance > 50) {
                    return Fare.ADDITION.multiply(40 / 5);
                }
                return Fare.ADDITION.multiply(Math.ceil((distance - 10 - 1) / 5) + 1);
            }
    ),
    FAR_DISTANCE(
            distance -> distance > 50,
            distance -> Fare.ADDITION.multiply((Math.ceil((distance - 50 - 1) / 8) + 1))
    );

    private final Predicate<Integer> isInRange;
    private final Function<Integer, Fare> distanceRateStrategy;

    DistanceFareRule(Predicate<Integer> isInRange, Function<Integer, Fare> distanceRateStrategy) {
        this.isInRange = isInRange;
        this.distanceRateStrategy = distanceRateStrategy;
    }

    public static Fare calculateRate(int distance) {
        return Arrays.stream(values())
                .filter(rule -> rule.isInRange.test(distance))
                .map(rule -> rule.applyDistanceRate(distance))
                .reduce(Fare.BASIC, Fare::add);
    }

    private Fare applyDistanceRate(int distance) {
        return distanceRateStrategy.apply(distance);
    }
}
