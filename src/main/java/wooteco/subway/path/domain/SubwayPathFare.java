package wooteco.subway.path.domain;

public class SubwayPathFare {
    private static final int EXTRA_FARE_SECOND_BOUNDARY = 50;
    private static final int EXTRA_FARE_FIRST_BOUNDARY = 10;
    private static final int EXTRA_FARE_RATE_AFTER_SECOND_BOUNDARY = 8;
    private static final int EXTRA_FARE_RATE_AFTER_FIRST_BOUNDARY = 5;
    private static final int BASE_FARE = 1250;
    private static final int FARE_BETWEEN_10_TO_50 = 800;
    private static final int FARE_RATE = 100;
    private static final int YOUTH_AGE = 6;
    private static final int MIN_ADOLESCENT_AGE = 13;
    private static final int MAX_ADOLESCENT_AGE = 19;
    private static final int BASE_DISCOUNT_RATE = 350;
    private static final double YOUTH_DISCOUNT_RATE = 0.5;
    private static final double ADOLESCENT_DISCOUNT_RATE = 0.8;

    private int age;
    private int distance;
    private int lineFare;

    public SubwayPathFare(int age, int distance, int lineFare) {
        this.age = age;
        this.distance = distance;
        this.lineFare = lineFare;
    }

    public int getFare() {
        if (age != -1 && age < YOUTH_AGE) {
            return 0;
        }
        int fare = calculateFareByDistance() + lineFare;
        if (YOUTH_AGE <= age && age < MIN_ADOLESCENT_AGE) {
            return (int) ((fare - BASE_DISCOUNT_RATE) * YOUTH_DISCOUNT_RATE);
        }
        if (MIN_ADOLESCENT_AGE <= age && age < MAX_ADOLESCENT_AGE) {
            return (int) ((fare - BASE_DISCOUNT_RATE) * ADOLESCENT_DISCOUNT_RATE);
        }
        return fare;
    }

    public int calculateFareByDistance() {
        int totalDistance = distance;
        if (totalDistance > EXTRA_FARE_SECOND_BOUNDARY) {
            return BASE_FARE + FARE_BETWEEN_10_TO_50 + (int)
                    ((Math.ceil((totalDistance - EXTRA_FARE_SECOND_BOUNDARY - 1) /
                            EXTRA_FARE_RATE_AFTER_SECOND_BOUNDARY)) + 1) * FARE_RATE;
        }
        if (EXTRA_FARE_FIRST_BOUNDARY < totalDistance && totalDistance <= EXTRA_FARE_SECOND_BOUNDARY) {
            return BASE_FARE + (int)
                    ((Math.ceil((totalDistance - EXTRA_FARE_FIRST_BOUNDARY - 1) /
                            EXTRA_FARE_RATE_AFTER_FIRST_BOUNDARY)) + 1) * FARE_RATE;
        }
        return BASE_FARE;
    }
}
