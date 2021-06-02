package wooteco.subway.path.domain;

import wooteco.subway.exception.SubwayCustomException;
import wooteco.subway.path.exception.PathException;

import java.util.Arrays;

public enum Age {

    ADULT(0){
        @Override
        boolean isRangeAge(int age) {
            return age >= ADULT_AGE;
        }

        @Override
        int calculateFareByAge(int fare) {
            return 0;
        }
    },
    TEEN(0.2) {
        @Override
        boolean isRangeAge(int age) {
            return age < ADULT_AGE && age >= TEEN_AGE;
        }

        @Override
        int calculateFareByAge(int fare) {
            return (int) ((fare - DISCOUNT_MONEY) * TEEN.discountRate);
        }
    },
    CHILD(0.5) {
        @Override
        boolean isRangeAge(int age) {
            return age < TEEN_AGE && age >= CHILD_AGE;
        }

        @Override
        int calculateFareByAge(int fare) {
            return (int) ((fare - DISCOUNT_MONEY) * CHILD.discountRate);
        }
    },
    BABY(1) {
        @Override
        boolean isRangeAge(int age) {
            return age < CHILD_AGE;
        }

        @Override
        int calculateFareByAge(int fare) {
            return fare;
        }
    };

    private static final int ADULT_AGE = 19;
    private static final int TEEN_AGE = 13;
    private static final int CHILD_AGE = 6;
    private static final int DISCOUNT_MONEY = 350;

    private final double discountRate;

    Age(double discountRate) {
        this.discountRate = discountRate;
    }

    public static Age of(int age) {
        return Arrays.stream(Age.values())
                .filter(item -> item.isRangeAge(age))
                .findAny()
                .orElseThrow(() -> new SubwayCustomException(PathException.INVALID_AGE_EXCEPTION));
    }

    abstract boolean isRangeAge(int age);

    abstract int calculateFareByAge(int fare);
}
