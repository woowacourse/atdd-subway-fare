package wooteco.subway.path.strategy;

public class AdultFareStrategy extends FareDistanceStrategy {


    @Override
    public boolean isAppropriate(int age) {
        return age > 19;
    }

    @Override
    public int discount(int distance, int extraLineFare) {
        return DEFAULT_FARE + extraDistanceFare(distance) + extraLineFare;
    }
}
