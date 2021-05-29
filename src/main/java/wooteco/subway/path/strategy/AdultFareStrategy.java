package wooteco.subway.path.strategy;

public class AdultFareStrategy extends FareDistanceStrategy{
    @Override
    public int discount(int distance, int extraLineFare) {
        return DEFAULT_FARE + extraDistanceFare(distance) + extraLineFare;
    }


}
