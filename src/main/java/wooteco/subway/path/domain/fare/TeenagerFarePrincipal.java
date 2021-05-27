package wooteco.subway.path.domain.fare;

import wooteco.subway.path.domain.fare.FarePrincipal;

import static wooteco.subway.path.application.FarePrincipalFinder.*;
import static wooteco.subway.path.application.FarePrincipalFinder.SECOND_OVER_FARE_DISTANCE;

public class TeenagerFarePrincipal implements FarePrincipal {
    private double discountRate = 0.2;

    @Override
    public double calculateFare(final int distance, final long extraFare) {
        double baseFare = BASIC_FARE + extraFare;
        double fare = baseFare;

        if (distance <= SECOND_OVER_FARE_DISTANCE) {
            fare =  calculateOverFare(baseFare, distance - FIRST_OVER_FARE_DISTANCE, 5);
        }

        if (distance > SECOND_OVER_FARE_DISTANCE) {
            baseFare = OVER_FARE + extraFare;
            fare = calculateOverFare(baseFare, distance - SECOND_OVER_FARE_DISTANCE, 8);
        }

        return fare - ((fare - DEDUCTED_AMOUNT) * discountRate);
    }
}
