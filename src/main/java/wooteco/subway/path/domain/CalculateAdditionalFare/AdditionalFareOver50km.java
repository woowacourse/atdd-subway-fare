package wooteco.subway.path.domain.CalculateAdditionalFare;

public class AdditionalFareOver50km implements AdditionalFareStrategy {

    private static final int OVER_LIMIT_DISTANCE = 50;
    private static final int DEFAULT_DISTANCE = 10;

    @Override
    public boolean isInDistanceRange(int distance) {
        return OVER_LIMIT_DISTANCE < distance;
    }

    @Override
    public int calculateAdditionalFare(int distance) {
        return calculateAdditionalFareOver10km(OVER_LIMIT_DISTANCE - DEFAULT_DISTANCE)
            + calculateAdditionalFareOver50km(distance - OVER_LIMIT_DISTANCE);
    }

    private int calculateAdditionalFareOver10km(int distance) {
        return CalculateAdditionalFare.calculateAdditionalFareOver10km(distance);
    }

    private int calculateAdditionalFareOver50km(int distance) {
        return CalculateAdditionalFare.calculateAdditionalFareOver50km(distance);
    }
}
