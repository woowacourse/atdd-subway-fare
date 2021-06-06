package wooteco.subway.fare.domain.farestrategy;

import wooteco.subway.exception.InternalLogicException;
import wooteco.subway.fare.domain.Money;
import wooteco.subway.line.domain.Distance;

public class DistanceFare implements FareStrategy {
    private static final int UNIT_PRICE = 100;
    private static final int FIRST_RANGE_UNIT_DISTANCE = 5;
    private static final int SECOND_RANGE_UNIT_DISTANCE = 8;
    private static final int DEFAULT_FARE_RANGE = 10;
    private static final int FIRST_FARE_RANGE_DISTANCE = 50;
    private static final int DEFAULT_TO_FIRST_RANGE_DISTANCE = FIRST_FARE_RANGE_DISTANCE - DEFAULT_FARE_RANGE;

    private final Distance totalDistance;

    public DistanceFare(int totalDistance) {
        this.totalDistance = new Distance(totalDistance);
    }


    @Override
    public Money calculate(Money value) {
        if (totalDistance.isLessOrEqualThan(DEFAULT_FARE_RANGE)) {
            return value;
        }
        if (isFirstRange()) {
            return value.add(calculateExtraFare(firstDistanceRange(), FIRST_RANGE_UNIT_DISTANCE));
        }
        return value
                .add(calculateExtraFare(DEFAULT_TO_FIRST_RANGE_DISTANCE, FIRST_RANGE_UNIT_DISTANCE))
                .add(calculateExtraFare(secondDistanceRange(), SECOND_RANGE_UNIT_DISTANCE));
    }

    private int calculateExtraFare(int distance, int unitDistance) {
        return (int) Math.ceil(((double) (distance)) / unitDistance) * UNIT_PRICE;
    }


    public boolean isFirstRange() {
        return totalDistance.isMoreThan(DEFAULT_FARE_RANGE)
                && totalDistance.isLessOrEqualThan(FIRST_FARE_RANGE_DISTANCE);
    }

    public int firstDistanceRange() {
        return totalDistance.subtract(DEFAULT_FARE_RANGE);
    }

    public int secondDistanceRange() {
        if (totalDistance.isLessThan(FIRST_FARE_RANGE_DISTANCE)) {
            throw new InternalLogicException();
        }
        return totalDistance.subtract(FIRST_FARE_RANGE_DISTANCE);
    }

}
