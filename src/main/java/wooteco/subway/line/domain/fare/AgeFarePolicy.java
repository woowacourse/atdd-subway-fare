package wooteco.subway.line.domain.fare;

import wooteco.subway.exception.notfound.InvalidAgeException;
import wooteco.subway.member.domain.User;

import java.util.Arrays;
import java.util.function.UnaryOperator;

public enum AgeFarePolicy {
    ADULT(19, (fare) -> fare),
    TEENAGER(13, (fare) -> calculate(fare, 0.8)),
    CHILD(6, (fare) -> calculate(fare, 0.5)),
    INFANT(0, (fare) -> calculate(fare, 0));

    private final int minimumAge;
    private final UnaryOperator<Integer> discountRate;

    AgeFarePolicy(int minimumAge, UnaryOperator<Integer> discountRate) {
        this.minimumAge = minimumAge;
        this.discountRate = discountRate;
    }

    public static AgeFarePolicy of(User user) {
        return Arrays.stream(values())
                .filter(agePolicy -> user.isOverThan(agePolicy.minimumAge))
                .findAny()
                .orElseThrow(InvalidAgeException::new);
    }

    public int discount(int fare) {
        return discountRate.apply(fare);
    }

    private static int calculate(int fare, double discountRate) {
        return (int) Math.ceil((fare - 350) * discountRate);
    }
}