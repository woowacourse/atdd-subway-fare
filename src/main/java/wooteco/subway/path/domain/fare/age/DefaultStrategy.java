package wooteco.subway.path.domain.fare.age;

public class DefaultStrategy implements AgeStrategy {
    @Override
    public int calculate(int fare) {
        return fare;
    }
}
