package wooteco.subway.path.strategy;

public class AdultFareStrategy implements FareStrategy{
    @Override
    public int discount(int fare) {
        return fare;
    }
}
