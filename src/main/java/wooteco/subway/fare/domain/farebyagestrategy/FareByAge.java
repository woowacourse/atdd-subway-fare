package wooteco.subway.fare.domain.farebyagestrategy;

import java.util.Arrays;
import java.util.List;
import wooteco.subway.exception.notfound.NotExistException;

public class FareByAge {

    private static final List<DiscountFareByAgeStrategy> DISCOUNT_FARE_BY_AGE_STRATEGIES
        = Arrays.asList(
        new DiscountFareWhenBabyStrategy(),
        new DiscountFareWhenChildStrategy(),
        new DiscountFareWhenTeenagerStrategy()
    );

    private final int age;

    public FareByAge(int age) {
        this.age = age;
    }

    public int calculateDiscountFareByAge(int fare) {
        return distinguishAgeMember().discountFareByAge(fare);
    }

    private DiscountFareByAgeStrategy distinguishAgeMember() {
        return DISCOUNT_FARE_BY_AGE_STRATEGIES.stream()
            .filter(discountFareByAgeStrategy -> discountFareByAgeStrategy.isInAgeRange(age))
            .findAny()
            .orElseThrow(NotExistException::new);
    }

}
