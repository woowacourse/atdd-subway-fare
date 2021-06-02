package wooteco.subway.fare.domain.farestrategy;

import wooteco.subway.fare.domain.Money;

public class DistanceFare implements FareStrategy {
    private static final int UNIT_PRICE = 100;
    private static final int DEFAULT_FARE_RANGE_DISTANCE = 10;
    private static final int FIRST_FARE_RANGE_DISTANCE = 50;
    private static final int FIRST_RANGE_UNIT_DISTANCE = 5;
    private static final int SECOND_RANGE_UNIT_DISTANCE = 8;
    private final int totalDistance;

    public DistanceFare(int totalDistance) {
        this.totalDistance = totalDistance;
    }


    @Override
    public Money calculate(Money value) {
        if (totalDistance <= DEFAULT_FARE_RANGE_DISTANCE) {
            return value;
        }
        if (totalDistance <= FIRST_FARE_RANGE_DISTANCE) {
            return value.add(calculateExtraFare(totalDistance - DEFAULT_FARE_RANGE_DISTANCE, FIRST_RANGE_UNIT_DISTANCE));
        }
        return value
                .add(calculateExtraFare(FIRST_FARE_RANGE_DISTANCE - DEFAULT_FARE_RANGE_DISTANCE, FIRST_RANGE_UNIT_DISTANCE))
                .add(calculateExtraFare(totalDistance - FIRST_FARE_RANGE_DISTANCE, SECOND_RANGE_UNIT_DISTANCE));
    }

    private int calculateExtraFare(int distance, int unitDistance) {
        return (int) Math.ceil(((double) (distance)) / unitDistance) * UNIT_PRICE;
    }

}
