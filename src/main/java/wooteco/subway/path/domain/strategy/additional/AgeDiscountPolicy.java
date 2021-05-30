package wooteco.subway.path.domain.strategy.additional;

public abstract class AgeDiscountPolicy {
    public final int calculateFare(int fare) {
        return (int)(fare * discountRate());
    }

    protected abstract double discountRate();
}
