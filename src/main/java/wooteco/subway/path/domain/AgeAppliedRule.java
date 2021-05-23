package wooteco.subway.path.domain;

import java.util.Arrays;
import java.util.function.IntPredicate;

public enum AgeAppliedRule {
    INFANT(0, 350, age -> (0 <= age) && (age < 6)),
    CHILD(0.5, 350, age -> (6 <= age) && (age < 13)),
    JUVENILLE(0.80, 350, age -> (13 <= age) && (age < 19)),
    OTHER(1.00, 0, age -> (19 <= age));

    private double appliedRate;
    private int deductedFare;
    private IntPredicate isAgeInterval;

    AgeAppliedRule(double appliedRate, int deductedFare, IntPredicate isAgeInterval) {
        this.appliedRate = appliedRate;
        this.deductedFare = deductedFare;
        this.isAgeInterval = isAgeInterval;
    }

    public static AgeAppliedRule matchRule(int age) {
        return Arrays.stream(AgeAppliedRule.values())
            .filter(element -> element.isAgeInterval.test(age))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("해당 되는 나이가 존재하지 않습니다."));
    }

    public double getAppliedRate() {
        return appliedRate;
    }

    public int getDeductedFare() {
        return deductedFare;
    }
}
