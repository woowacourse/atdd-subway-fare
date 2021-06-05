package wooteco.subway.path.domain;

import java.util.Arrays;
import java.util.function.IntPredicate;

public enum AgePolicy {
    TWENTIES(age -> age > 19, 0, 0),
    TEENAGERS(age -> 13 <= age && age < 19, 350, 0.2),
    PRESCHOOLER(age -> 6 <= age && age < 13, 350, 0.5),
    BABIES(age -> age < 6, 0, 1);

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
                .orElseThrow(() -> new IllegalArgumentException("해당하는 나이가 존재하지 않습니다."));
    }

    public int calculateFareAppliedAgePolicy(int currentFare) {
        return (int) ((currentFare - deductionFare) * (1 - discountRate));
    }
}
