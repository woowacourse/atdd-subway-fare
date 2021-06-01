package wooteco.subway.path.domain.CalculateAdditionalFare;

public interface AdditionalFareStrategy {

    boolean isInDistanceRange(int distance);

    int calculateAdditionalFare(int distance);
}
