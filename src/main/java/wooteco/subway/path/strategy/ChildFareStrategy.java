package wooteco.subway.path.strategy;

public class ChildFareStrategy extends FareDistanceStrategy {

    @Override
    public boolean isAppropriate(int age) {
        return age >= 6 && age < 13 ;
    }

    @Override
    public int discount(int distance, int extraLineFare) {
        int fare = DEFAULT_FARE + extraDistanceFare(distance) + extraLineFare;
        return (int) ((fare - 350) * 0.5);
    }
}
