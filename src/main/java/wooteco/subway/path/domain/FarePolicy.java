package wooteco.subway.path.domain;

public class FarePolicy {

    private static final int DEFAULT_FARE = 1250;
    private static final int FARE_PER_UNIT_DISTANCE = 100;

    private static final int FIRST_ADDITIONAL_FARE_SECTION = 10;
    private static final int SECOND_ADDITIONAL_FARE_SECTION = 50;

    private static final int FIRST_ADDITIONAL_FARE_UNIT_DISTANCE = 5;
    private static final int SECOND_ADDITIONAL_FARE_UNIT_DISTANCE = 8;

    public static int calculate(int distance) {
        return DEFAULT_FARE + fareInFirstSection(distance) + fareInSecondSection(distance);
    }

    private static int fareInFirstSection(int distance) {
        if (distance < FIRST_ADDITIONAL_FARE_SECTION) {
            return 0;
        }
        int firstSectionDistance = Math.min(distance, SECOND_ADDITIONAL_FARE_SECTION) - FIRST_ADDITIONAL_FARE_SECTION;
        return calculateAdditionalFare(firstSectionDistance, FIRST_ADDITIONAL_FARE_UNIT_DISTANCE);
    }

    private static int fareInSecondSection(int distance) {
        if (distance < SECOND_ADDITIONAL_FARE_SECTION) {
            return 0;
        }
        int secondSectionDistance = distance - SECOND_ADDITIONAL_FARE_SECTION;
        return calculateAdditionalFare(secondSectionDistance, SECOND_ADDITIONAL_FARE_UNIT_DISTANCE);
    }

    private static int calculateAdditionalFare(int additionalDistance, int unitDistance) {
        return (int) ((Math.ceil((additionalDistance - 1) / unitDistance) + 1) * FARE_PER_UNIT_DISTANCE);
    }
}
