package wooteco.subway.fare.domain.farestrategy;

import wooteco.subway.fare.domain.Money;
import wooteco.subway.line.domain.Distance;

import static wooteco.subway.line.domain.Distance.DEFAULT_TO_FIRST_RANGE_DISTANCE;

public class DistanceFare implements FareStrategy {
    private static final int UNIT_PRICE = 100;
    private static final int FIRST_RANGE_UNIT_DISTANCE = 5;
    private static final int SECOND_RANGE_UNIT_DISTANCE = 8;

    private final Distance totalDistance;

    public DistanceFare(int totalDistance) {
        this.totalDistance = new Distance(totalDistance);
    }


    @Override
    public Money calculate(Money value) {
        if (totalDistance.isDefaultRange()) {
            return value;
        }
        if (totalDistance.isFirstRange()) {
            return value.add(calculateExtraFare(totalDistance.firstDistanceRange(), FIRST_RANGE_UNIT_DISTANCE));
        }
        return value
                .add(calculateExtraFare(DEFAULT_TO_FIRST_RANGE_DISTANCE, FIRST_RANGE_UNIT_DISTANCE))
                .add(calculateExtraFare(totalDistance.secondDistanceRange(), SECOND_RANGE_UNIT_DISTANCE));
    }

    private int calculateExtraFare(int distance, int unitDistance) {
        return (int) Math.ceil(((double) (distance)) / unitDistance) * UNIT_PRICE;
    }

}
