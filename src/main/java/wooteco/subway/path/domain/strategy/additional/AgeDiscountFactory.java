package wooteco.subway.path.domain.strategy.additional;

public class AgeDiscountFactory {
    public static AgeDiscountPolicy create(int age) {
        if (age < 6) {
            return new UnderFiveDiscount();
        }
        if (age < 13) {
            return new SixToTwelveDiscount();
        }
        if (age < 19) {
            return new ThirteenToNineTeenDiscount();
        }
        return new NoDiscount();
    }
}
