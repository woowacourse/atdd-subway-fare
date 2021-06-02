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
    },
    TEENAGER() {
        @Override
        boolean isContainAge(int age) {
            return age < ADUlT_AGE && age >= TEENAGER_AGE;
        }
    },
    CHILD() {
        @Override
        boolean isContainAge(int age) {
            return age < TEENAGER_AGE && age >= CHILD_AGE;
        }
    },
    BABY() {
        @Override
        boolean isContainAge(int age) {
            return age < CHILD_AGE;
        }
    };


    private static final int ADUlT_AGE = 19;
    private static final int TEENAGER_AGE = 13;
    private static final int CHILD_AGE = 6;

    AgeSet() {

    }

    public static AgeSet of(int age) {
        return Arrays.stream(AgeSet.values())
            .filter(item -> item.isContainAge(age))
            .findAny().orElseThrow(() -> new SubwayException(
                PathExceptionSet.AGE_NOT_FOUND));
    }

    abstract boolean isContainAge(int age);
}
