package wooteco.subway.fare.domain.farebyagestrategy;

public interface DiscountFareByAgeStrategy {

    boolean isInAgeRange(int age);

    int discountFareByAge(int fare);
}
