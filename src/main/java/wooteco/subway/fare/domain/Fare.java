package wooteco.subway.fare.domain;

import java.util.Objects;

public class Fare {

    private final int distance;
    private final int lineExtraFare;
    private final FareStrategy fareStrategy;
    private DiscountStrategy discountStrategy;

    public Fare(int distance, int lineExtraFare, FareStrategy fareStrategy) {
        this(distance, lineExtraFare, fareStrategy, null);
    }

    public Fare(int distance, int lineExtraFare, FareStrategy fareStrategy, DiscountStrategy discountStrategy) {
        this.distance = distance;
        this.lineExtraFare = lineExtraFare;
        this.fareStrategy = fareStrategy;
        this.discountStrategy = discountStrategy;
    }

    public int calculateFare() {
        int basicFare = fareStrategy.calculateFare(distance) + lineExtraFare;
        if (Objects.isNull(discountStrategy)) {
            return basicFare;
        }
        return discountStrategy.applyDiscount(basicFare);
    }
}
