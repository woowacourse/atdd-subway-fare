package wooteco.subway.path.domain;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.IntPredicate;

public enum DiscountRate {
    ADULTS(0, 0, (age) -> 19 <= age),
    TEENAGERS(350, 0.2, (age) -> 13 <= age),
    PRESCHOOLER(350, 0.5, (age) -> 6 <= age),
    BABIES(0, 1, (age) -> 6 > age);

    private final int deductionFare;
    private final double discountRate;
    private final IntPredicate predicate;

    DiscountRate(int deductionFare, double discountRate, IntPredicate predicate) {
        this.deductionFare = deductionFare;
        this.discountRate = discountRate;
        this.predicate = predicate;
    }

    public static DiscountRate compareAgeBaseline(int age) {
        return Arrays.stream(values())
            .filter(discountRate -> discountRate.predicate.test(age))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException("해당하는 DiscountRate 객체가 없습니다."));
    }

    public int calculateFare(int currentFare) {
        return (int) ((currentFare - deductionFare) * (1 - discountRate));
    }
}
