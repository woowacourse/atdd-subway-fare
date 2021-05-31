package wooteco.subway.path.domain;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public enum ExtraFare {
    BASIC_FARE(0, 10, 0, 0, distance -> 0),
    FIRST_RANGE_FARE(10, 50, 5, 100, ExtraFare::getExtraFareOfFirstRange),
    SECOND_RANGE_FARE(50, Integer.MAX_VALUE, 8, 100, ExtraFare::getExtraFareOfSecondRange);

    private final int minRange;
    private final int maxRange;
    private final int overDistance;
    private final int overFare;
    private final UnaryOperator<Integer> calculator;

    ExtraFare(int minRange, int maxRange, int overDistance, int overFare,
        UnaryOperator<Integer> calculator) {
        this.minRange = minRange;
        this.maxRange = maxRange;
        this.overDistance = overDistance;
        this.overFare = overFare;
        this.calculator = calculator;
    }

    public static int calculateFare(int distance) {
        final ExtraFare extraFare = Arrays.stream(values())
            .filter(decideExtraFarePredicate(distance))
            .findAny()
            .orElseThrow(() -> new NoSuchElementException("해당하는 ExtraFare 객체가 없습니다."));

        return extraFare.calculator.apply(distance);
    }

    private static Predicate<ExtraFare> decideExtraFarePredicate(int distance) {
        return element -> element.minRange < distance && element.maxRange >= distance;
    }

    private static int getExtraFareOfFirstRange(int distance) {
        return calculateOverFare(distance - BASIC_FARE.maxRange,
            FIRST_RANGE_FARE.overDistance,
            FIRST_RANGE_FARE.overFare
        );
    }

    private static int getExtraFareOfSecondRange(int distance) {
        int beforeFare = getExtraFareOfFirstRange(FIRST_RANGE_FARE.maxRange);
        return beforeFare + calculateOverFare(
            distance - FIRST_RANGE_FARE.maxRange,
            SECOND_RANGE_FARE.overDistance,
            SECOND_RANGE_FARE.overFare
        );
    }

    private static int calculateOverFare(int distance, int overDistance, int overFare) {
        return (int) ((Math.ceil((distance - 1) / overDistance) + 1) * overFare);
    }
}
