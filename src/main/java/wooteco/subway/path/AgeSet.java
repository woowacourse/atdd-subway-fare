package wooteco.subway.path;

import java.util.Arrays;
import wooteco.subway.exception.SubwayException;
import wooteco.subway.path.exception.PathExceptionSet;

public enum AgeSet {

    ADULT(19) {
        @Override
        boolean isContainAge(int age) {
            return age >= getReferenceAge();
        }

        @Override
        public int ageDisCount(int fare) {
            return 0;
        }
    },
    TEENAGER(13) {
        @Override
        boolean isContainAge(int age) {
            return age < ADULT.getReferenceAge() && age >= getReferenceAge();
        }

        @Override
        public int ageDisCount(int fare) {
            return (int) ((fare - DEDUCTIBLE_AMOUNT) * TEENAGER_DISCOUNT);
        }
    },
    CHILD(6) {
        @Override
        boolean isContainAge(int age) {
            return age < TEENAGER.getReferenceAge() && age >= getReferenceAge();
        }

        @Override
        public int ageDisCount(int fare) {
            return (int) ((fare - DEDUCTIBLE_AMOUNT) * CHILDREN_DISCOUNT);
        }
    },
    BABY(0) {
        @Override
        boolean isContainAge(int age) {
            return age < CHILD.getReferenceAge() && age >= getReferenceAge();
        }

        @Override
        public int ageDisCount(int fare) {
            return fare;
        }
    };

    private static final int DEDUCTIBLE_AMOUNT = 350;
    private static final double TEENAGER_DISCOUNT = 0.2;
    private static final double CHILDREN_DISCOUNT = 0.5;

    private final int referenceAge;

    AgeSet(int referenceAge) {
        this.referenceAge = referenceAge;
    }

    public static AgeSet of(int age) {
        return Arrays.stream(AgeSet.values())
            .filter(item -> item.isContainAge(age))
            .findAny().orElseThrow(() -> new SubwayException(PathExceptionSet.AGE_NOT_FOUND));
    }

    abstract boolean isContainAge(int age);

    public abstract int ageDisCount(int fare);

    public int getReferenceAge() {
        return referenceAge;
    }
}
