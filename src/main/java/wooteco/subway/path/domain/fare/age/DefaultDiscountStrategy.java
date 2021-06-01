package wooteco.subway.path.domain.fare.age;

public class DefaultDiscountStrategy implements AgeStrategy {
    @Override
    public int calculate(int fare) {
        return fare;
    }
}
