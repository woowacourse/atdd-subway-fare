package wooteco.subway.path.domain.fare;

import wooteco.subway.exception.application.ValidationFailureException;

public class Fare {

    private static final int BASIC_FARE = 1250;

    private final int value;

    public Fare(int distance, int age, int extraFare) {
        validateRange(distance, age, extraFare);
        this.value = calculateFare(distance, age, extraFare);
    }

    private void validateRange(int distance, int age, int extraFare) {
        validatePositiveOrZero(distance);
        validatePositiveOrZero(age);
        validatePositiveOrZero(extraFare);
    }

    private void validatePositiveOrZero(int value) {
        if (value < 0) {
            throw new ValidationFailureException("음수가 존재하여 요금 객체 생성에 실패했습니다.");
        }
    }

    private int calculateFare(int distance, int age, int additionalLineFare) {
        int fare = applyDistance(BASIC_FARE, distance) + additionalLineFare;
        return applyAge(fare, age);
    }

    private int applyDistance(int fare, int distance) {
        return DistanceAppliedRule.applyRule(fare, distance);
    }

    private int applyAge(int fare, int age) {
        return AgeAppliedRule.applyRule(fare, age);
    }

    public int value() {
        return this.value;
    }
}
