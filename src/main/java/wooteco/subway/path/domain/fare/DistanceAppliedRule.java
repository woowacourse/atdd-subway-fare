package wooteco.subway.path.domain.fare;

import java.util.Arrays;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

public enum DistanceAppliedRule {
    BASIC_FARE(
        distance -> distance <= 10,
        distance -> 0
    ),
    SHORT_INTERVAL_FARE(
        distance -> (10 < distance) && (distance <= 50),
        DistanceAppliedRule::calculateShortIntervalFare
    ),
    LONG_INTERVAL_FARE(
        distance -> distance > 50,
        DistanceAppliedRule::calculateBothIntervalFare
    );

    private static final int LONG_INTERVAL_CRITERION = 50;
    private static final int EXCLUSION_FARE_DISTANCE = 10;
    private static final int SHORT_INTERVAL_UNIT = 5;
    private static final int LONG_INTERVAL_UNIT = 8;
    private static final int EXTRA_FARE_UNIT = 100;

    private final IntPredicate matchedFilter;
    private final IntUnaryOperator extraFareFilter;

    DistanceAppliedRule(IntPredicate matchedFilter, IntUnaryOperator extraFareFilter) {
        this.matchedFilter = matchedFilter;
        this.extraFareFilter = extraFareFilter;
    }

    private static DistanceAppliedRule matchedRule(int distance) {
        return Arrays.stream(values())
            .filter(rule -> rule.matchedFilter.test(distance))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("해당하는 거리가 존재하지 않습니다."));
    }

    public static int applyRule(int fare, int distance) {
        return fare + matchedRule(distance)
            .extraFareFilter
            .applyAsInt(distance);
    }

    private static int calculateBothIntervalFare(int distance) {
        int longIntervalQuantity = distance - LONG_INTERVAL_CRITERION;
        return calculateShortIntervalFare(distance - longIntervalQuantity)
            + calculateLongIntervalFare(longIntervalQuantity);
    }

    private static int calculateShortIntervalFare(int distance) {
        distance -= EXCLUSION_FARE_DISTANCE;
        int unitCount = (distance - 1) / SHORT_INTERVAL_UNIT + 1;
        return unitCount * EXTRA_FARE_UNIT;
    }

    private static int calculateLongIntervalFare(int distance) {
        int unitCount = (distance - 1) / LONG_INTERVAL_UNIT + 1;
        return unitCount * EXTRA_FARE_UNIT;
    }
}
