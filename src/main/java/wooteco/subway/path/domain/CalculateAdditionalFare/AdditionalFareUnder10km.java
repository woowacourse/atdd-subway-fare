package wooteco.subway.path.domain.CalculateAdditionalFare;

public class AdditionalFareUnder10km implements AdditionalFareStrategy {

    private static final int DEFAULT_DISTANCE = 10;
    private static final int NO_ADDITIONAL_FARE = 0;

    @Override
    public boolean isInDistanceRange(int distance) {
        return distance <= DEFAULT_DISTANCE;
    }

    @Override
    public int calculateAdditionalFare(int distance) {
        return NO_ADDITIONAL_FARE;
    }
}
