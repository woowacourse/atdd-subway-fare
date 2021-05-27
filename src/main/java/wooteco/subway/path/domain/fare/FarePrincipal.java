package wooteco.subway.path.domain.fare;

import static wooteco.subway.path.application.FarePrincipalFinder.FARE_PER_KM;

public interface FarePrincipal {
    int DEDUCTED_AMOUNT = 350;

    double calculateFare(int distance, long extraFare);

    default double calculateOverFare (double baseFare, int distance, int perDistance){
        return baseFare + (Math.ceil(distance / perDistance) * FARE_PER_KM);
    }
}
