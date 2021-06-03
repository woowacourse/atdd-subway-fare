package wooteco.subway.path.domain.strategy.additional;

public class AdolescenceDiscount extends AgeDiscountPolicy {
    public AdolescenceDiscount(int discountFare) {
        super(discountFare);
    }

    @Override
    public int calculateFare(int fare) {
        return (int) ((fare - discountFare()) * 0.8);
    }

    @Override
    public boolean match(int age) {
        return age >= 13 && age <= 19;
    }
}
