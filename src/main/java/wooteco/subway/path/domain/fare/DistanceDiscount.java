package wooteco.subway.path.domain.fare;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public enum DistanceDiscount {
    BASIC_DISTANCE(distance -> distance < 10, basicDiscountPolicy()),
    MIDDLE_DISTANCE(distance -> distance > 10 && distance <= 50, middleDistanceDiscountPolicy()),
    LONG_DISTANCE(distance -> distance > 50, longDistanceDiscountPolicy());

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

    static UnaryOperator<Integer> basicDiscountPolicy() {
        return distance -> 1250;
    }

    static UnaryOperator<Integer> middleDistanceDiscountPolicy() {
        return distance -> 1250 + (distance / 5 - 1) * 100;
    }

    static UnaryOperator<Integer> longDistanceDiscountPolicy() {
        return distance -> 2050 + ((distance - 50) / 8 + 1) * 100;
    }
}
