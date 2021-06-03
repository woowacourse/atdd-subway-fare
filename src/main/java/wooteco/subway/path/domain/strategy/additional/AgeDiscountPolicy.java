package wooteco.subway.path.domain.strategy.additional;

public abstract class AgeDiscountPolicy {
    private final int discountFare;

    protected AgeDiscountPolicy(int discountFare) {
        this.discountFare = discountFare;
    }

    public int discountFare() {
        return discountFare;
    }

    public abstract int calculateFare(int fare);
    public abstract boolean match(int age);
}
