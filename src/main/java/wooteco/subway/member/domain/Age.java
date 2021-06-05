package wooteco.subway.member.domain;

import wooteco.subway.config.exception.BadRequestException;

public class Age {

    private static final int ADULT_LIMIT = 19;
    private static final int TEENAGER_LIMIT = 13;
    private static final int CHILDREN_LIMIT = 6;

    private final int age;

    public Age(int age) {
        checkPositiveNumber(age);
        checkMinimumAge(age);

        this.age = age;
    }

    private void checkPositiveNumber(int age) {
        if (age < 0) {
            throw new BadRequestException("나이는 음수일 수 없습니다.");
        }
    }

    private void checkMinimumAge(int age) {
        if (age < CHILDREN_LIMIT) {
            throw new BadRequestException(CHILDREN_LIMIT + "세 미만의 어린이는 가입할 수 없습니다.");
        }
    }

    public int getDiscountedFare(int fare) {
        if (age >= ADULT_LIMIT) {
            return fare;
        }

        if (age >= TEENAGER_LIMIT) {
            return (int) ((fare - 350) * 0.8);
        }

        if (age >= CHILDREN_LIMIT) {
            return (int) ((fare - 350) * 0.5);
        }

        throw new IllegalStateException("비정상적인 age입니다. age:" + age);
    }

    public int toInt() {
        return age;
    }
}
