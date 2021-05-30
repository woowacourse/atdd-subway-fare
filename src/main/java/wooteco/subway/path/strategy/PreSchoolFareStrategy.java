package wooteco.subway.path.strategy;

public class PreSchoolFareStrategy implements FareStrategy {
    @Override
    public int discount(int fare, int extraLineFare) {
        return 0;
    }
}
