package wooteco.subway.path.domain.fare;

import static wooteco.subway.path.domain.fare.FarePolicyFinder.FARE_PER_KM;

public interface FarePolicy {
    double calculateFare(int distance, long extraFare);

    default double calculateOverFare (double baseFare, int distance, int perDistance){
        return baseFare + (Math.ceil(distance / perDistance) * FARE_PER_KM);
    }
}
