package wooteco.subway.fare.domain;

public interface DiscountStrategy {

    int applyDiscount(int fare);
}
