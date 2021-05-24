package wooteco.subway.path.domain;

public class Discount {

    private final DiscountRate discountRate;

    private Discount(DiscountRate discountRate) {
        this.discountRate = discountRate;
    }

    public static Discount of(int age) {
        return new Discount(DiscountRate.compareAgeBaseline(age));
    }

    public int getDiscountedFare(int fare) {
        return discountRate.calculateFare(fare);
    }
}
