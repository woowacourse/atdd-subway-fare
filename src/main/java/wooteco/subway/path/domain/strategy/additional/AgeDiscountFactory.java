package wooteco.subway.path.domain.strategy.additional;

public class AgeDiscountFactory {
    public static AgeDiscountPolicy create(int age, int discountFare) {
        if (age < 6) {
            return new UnderFiveDiscount(discountFare);
        }
        if (age < 13) {
            return new SixToTwelveDiscount(discountFare);
        }
        if (age < 19) {
            return new ThirteenToNineTeenDiscount(discountFare);
        }
        return new NoDiscount(discountFare);
    }
}
