package wooteco.subway.path.domain.CalculateAdditionalFare;

public class AdditionalFareOver10Km implements AdditionalFareStrategy {

    private static final int DEFAULT_DISTANCE = 10;
    private static final int OVER_LIMIT_DISTANCE = 50;

    @Override
    public boolean isInDistanceRange(int distance) {
        return DEFAULT_DISTANCE < distance && distance <= OVER_LIMIT_DISTANCE;
    }

    @Override
    public int calculateAdditionalFare(int distance) {
        return calculateAdditionalFareOver10km(distance - DEFAULT_DISTANCE);
    }

    private int calculateAdditionalFareOver10km(int distance) {
        return CalculateAdditionalFare.calculateAdditionalFareOver10km(distance);
    }
}
