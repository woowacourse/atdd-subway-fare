package wooteco.subway.path.domain;

import java.util.Arrays;
import java.util.function.IntPredicate;

public enum AgePolicy {
    ADULT(age -> age > 19, 0, 0),
    TEENAGER(age -> 13 <= age && age < 19, 350, 0.2),
    CHILD(age -> 6 <= age && age < 13, 350, 0.5),
    BABY(age -> age < 6, 0, 1);

    private final IntPredicate agePredicate;
    private final int deductionFare;
    private final double discountRate;

    AgePolicy(IntPredicate agePredicate, int deductionFare, double discountRate) {
        this.agePredicate = agePredicate;
        this.deductionFare = deductionFare;
        this.discountRate = discountRate;
    }

    public static AgePolicy of(int age) {
        return Arrays.stream(AgePolicy.values())
            .filter(agePolicy -> agePolicy.agePredicate.test(age))
            .findAny()
            .orElse(ADULT);
    }

    public int discount(int fare) {
        return (int) ((fare - deductionFare) * (1 - discountRate));
    }
}
