package wooteco.subway.path.domain.strategy.additional;

public class ChildrenDiscount extends AgeDiscountPolicy {
    public ChildrenDiscount(int discountFare) {
        super(discountFare);
    }

    @Override
    public int calculateFare(int fare) {
        return (int) ((fare - discountFare()) * 0.5);
    }

    @Override
    public boolean match(int age) {
        return age >=6 && age <=12;
    }
}
