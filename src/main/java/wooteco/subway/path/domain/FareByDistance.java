package wooteco.subway.path.domain;

public class FareByDistance {

    private static final int DEFAULT_FARE = 1250;
    private static final int DEFAULT_DISTANCE = 10;
    private static final int OVER_LIMIT_DISTANCE = 50;

    private final int distance;

    public FareByDistance(int distance) {
        this.distance = distance;
    }

    public int calculate() {
        if (distance <= DEFAULT_DISTANCE) {
            return DEFAULT_FARE;
        }

        if (distance <= OVER_LIMIT_DISTANCE) {
            return fareOver10(distance);
        }

        return fareOver50(distance);
    }

    private int fareOver10(int distance) {
        int additionalFare = calculateAdditionalFareOver10km(distance);
        return DEFAULT_FARE + additionalFare;
    }

    private int fareOver50(int distance) {
        int additionalFareOver10km = calculateAdditionalFareOver10km(OVER_LIMIT_DISTANCE);
        int additionalFareOver50km = calculateAdditionalFareOver50km(distance);

        return DEFAULT_FARE
                + additionalFareOver10km
                + additionalFareOver50km;
    }

    private int calculateAdditionalFareOver10km(int distance) {
        int additionalDistance = distance - DEFAULT_DISTANCE - 1;
        return (additionalDistance / 5 + 1) * 100;
    }

    private int calculateAdditionalFareOver50km(int distance) {
        int additionalDistance = distance - OVER_LIMIT_DISTANCE - 1;
        return (additionalDistance / 8 + 1) * 100;
    }
}
