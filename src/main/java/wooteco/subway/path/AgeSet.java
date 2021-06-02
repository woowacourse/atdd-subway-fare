package wooteco.subway.path;

import java.util.Arrays;
import wooteco.subway.exception.SubwayException;
import wooteco.subway.path.exception.PathExceptionSet;

public enum AgeSet {

    ADULT() {
        @Override
        boolean isContainAge(int age) {
            return age >= ADUlT_AGE;
        }

        @Override
        public int ageDisCount(int fare) {
            return 0;
        }
    },
    TEENAGER() {
        @Override
        boolean isContainAge(int age) {
            return age < ADUlT_AGE && age >= TEENAGER_AGE;
        }

        @Override
        public int ageDisCount(int fare) {
            return (int) ((fare - DEDUCTIBLE_AMOUNT) * TEENAGER_DISCOUNT);
        }
    },
    CHILD() {
        @Override
        boolean isContainAge(int age) {
            return age < TEENAGER_AGE && age >= CHILD_AGE;
        }

        @Override
        public int ageDisCount(int fare) {
            return (int) ((fare - DEDUCTIBLE_AMOUNT) * CHILDREN_DISCOUNT);
        }
    },
    BABY() {
        @Override
        boolean isContainAge(int age) {
            return age < CHILD_AGE;
        }

        @Override
        public int ageDisCount(int fare) {
            return fare;
        }
    };


    private static final int ADUlT_AGE = 19;
    private static final int TEENAGER_AGE = 13;
    private static final int CHILD_AGE = 6;
    private static final int DEDUCTIBLE_AMOUNT = 350;
    private static final double TEENAGER_DISCOUNT = 0.2;
    private static final double CHILDREN_DISCOUNT = 0.5;

    AgeSet() {

    }

    public static AgeSet of(int age) {
        return Arrays.stream(AgeSet.values())
            .filter(item -> item.isContainAge(age))
            .findAny().orElseThrow(() -> new SubwayException(
                PathExceptionSet.AGE_NOT_FOUND));
    }

    abstract boolean isContainAge(int age);

    public abstract int ageDisCount(int fare);
}
