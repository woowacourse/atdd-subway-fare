package wooteco.subway.fare.domain;

public class Fare {
    private static final int ZERO = 0;
    private static final int DEFAULT_FARE = 1250;
    private static final int UNIT_PRICE = 100;
    private static final int DEFAULT_FARE_RANGE_DISTANCE = 10;
    private static final int FIRST_FARE_RANGE_DISTANCE = 50;
    private static final int FIRST_RANGE_UNIT_DISTANCE = 5;
    private static final int SECOND_RANGE_UNIT_DISTANCE = 8;
    private static final int CHILD_START_AGE = 6;
    private static final int ADULT_START_AGE = 19;
    private static final int TEENAGER_START_AGE = 13;
    private static final int DISCOUNT_PRICE = 350;
    private static final double DISCOUNT_50_RATE = 0.5;
    private static final double DISCOUNT_20_RATE = 0.8;

    private final int totalDistance;
    private final int lineExtraFare;

    public Fare(int totalDistance, int lineExtraFare) {
        this.totalDistance = totalDistance;
        this.lineExtraFare = lineExtraFare;
    }

    public int calculateBasicFare() {
        if (totalDistance <= DEFAULT_FARE_RANGE_DISTANCE) {
            return DEFAULT_FARE + lineExtraFare;
        }
        if (totalDistance <= FIRST_FARE_RANGE_DISTANCE) {
            return DEFAULT_FARE
                    + calculateExtraFare(totalDistance - DEFAULT_FARE_RANGE_DISTANCE, FIRST_RANGE_UNIT_DISTANCE)
                    + lineExtraFare;
        }
        return DEFAULT_FARE
                + calculateExtraFare(FIRST_FARE_RANGE_DISTANCE - DEFAULT_FARE_RANGE_DISTANCE, FIRST_RANGE_UNIT_DISTANCE)
                + calculateExtraFare(totalDistance - FIRST_FARE_RANGE_DISTANCE, SECOND_RANGE_UNIT_DISTANCE)
                + lineExtraFare;
    }

    private int calculateExtraFare(int distance, int unitDistance) {
        return (int) Math.ceil(((double) (distance)) / unitDistance) * UNIT_PRICE;
    }

    public int calculateDiscountFare(int age) {
        if (age < CHILD_START_AGE) {
            return ZERO;
        }
        if (age >= ADULT_START_AGE) {
            return calculateBasicFare();
        }
        if (age < TEENAGER_START_AGE) {
            return (int) ((calculateBasicFare() - DISCOUNT_PRICE) * DISCOUNT_50_RATE);
        }
        return (int) ((calculateBasicFare() - DISCOUNT_PRICE) * DISCOUNT_20_RATE);
    }
}
