package wooteco.subway.path.domain.fare;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public enum DistanceDiscount {
    BASIC_DISTANCE(distance -> distance <= 10, distance -> 1250),
    MIDDLE_DISTANCE(distance -> distance > 10 && distance <= 50, distance -> 1250 + (int) (Math.ceil((double) (distance - 10) / 5)) * 100),
    LONG_DISTANCE(distance -> distance > 50, distance -> 2050 + ((int) (Math.ceil((double) (distance - 50) / 8)) * 100));

    DistanceDiscount(Predicate<Integer> figureOutDiscount, UnaryOperator<Integer> discountPolicy) {
        this.figureOutDiscount = figureOutDiscount;
        this.discountPolicy = discountPolicy;
    }

    Predicate<Integer> figureOutDiscount;
    UnaryOperator<Integer> discountPolicy;

    public static int calculateFareAfterDiscount(int distance) {
        DistanceDiscount findDistance = Arrays.stream(DistanceDiscount.values())
                .filter(value -> value.figureOutDiscount.test(distance))
                .findAny()
                .orElseThrow(RuntimeException::new);

        return findDistance.discountPolicy.apply(distance);
    }
}
