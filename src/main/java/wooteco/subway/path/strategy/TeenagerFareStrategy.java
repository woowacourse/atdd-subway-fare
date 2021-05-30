package wooteco.subway.path.strategy;

public class TeenagerFareStrategy extends FareDistanceStrategy {
    @Override
    public boolean isAppropriate(int age) {
        return age >= 13 && age < 19;
    }

    @Override
    public int discount(int distance, int extraLineFare) {
        int fare = DEFAULT_FARE + extraDistanceFare(distance) + extraLineFare;
        return (int) ((fare - 350) * 0.8);
    }
}
