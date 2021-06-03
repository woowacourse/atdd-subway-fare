package wooteco.subway.fare.domain;

import wooteco.subway.path.domain.SubwayPath;

public class Fare {

    private final SubwayPath subwayPath;
    private final FareStrategy fareStrategy;
    private final DiscountStrategy discountStrategy;

    public Fare(SubwayPath subwayPath, FareStrategy fareStrategy, DiscountStrategy discountStrategy) {
        this.subwayPath = subwayPath;
        this.fareStrategy = fareStrategy;
        this.discountStrategy = discountStrategy;
    }

    public int calculateFare() {
        int distance = subwayPath.calculateDistance();
        int lineExtraFare = subwayPath.calculateLineMaxFare();
        int basicFare = fareStrategy.calculateFare(distance) + lineExtraFare;
        return discountStrategy.applyDiscount(basicFare);
    }
}
