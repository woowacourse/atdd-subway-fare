package wooteco.subway.fare.domain.farebydistancestrategy;

public interface AdditionalFareStrategy {

    boolean isInDistanceRange(int distance);

    int calculateAdditionalFare(int distance);
}
