package wooteco.subway.path.domain.fare.age;

public class BabyDiscountStrategy implements AgeStrategy {
    @Override
    public int calculate(int fare) {
        return 0;
    }
}
