package wooteco.subway.path.strategy;

public class TeenagerFareStrategy implements FareStrategy{
    @Override
    public int discount(int fare) {
        return (int) ((fare - 350) * 0.8);
    }
}
