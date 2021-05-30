package wooteco.subway.path.strategy;

public class PreSchoolFareStrategy implements FareStrategy {
    @Override
    public boolean isAppropriate(int age) {
        return age < 6;
    }

    @Override
    public int discount(int fare, int extraLineFare) {
        return 0;
    }
}
