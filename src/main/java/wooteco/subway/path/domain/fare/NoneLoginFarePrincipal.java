package wooteco.subway.path.domain.fare;

import static wooteco.subway.path.domain.fare.FarePrincipalFinder.*;

public class NoneLoginFarePrincipal implements FarePrincipal {
    @Override
    public double calculateFare(final int distance, final long extraFare) {
        double baseFare = BASIC_FARE + extraFare;

        if (distance <= FIRST_OVER_FARE_DISTANCE) {
            return baseFare;
        }

        if (distance <= SECOND_OVER_FARE_DISTANCE) {
            return calculateOverFare(baseFare, distance - FIRST_OVER_FARE_DISTANCE, 5);
        }

        baseFare = OVER_FARE + extraFare;
        return calculateOverFare(baseFare, distance - SECOND_OVER_FARE_DISTANCE, 8);
    }
}
