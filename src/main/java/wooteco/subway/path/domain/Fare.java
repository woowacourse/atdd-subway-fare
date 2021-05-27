package wooteco.subway.path.domain;

import wooteco.subway.path.ui.farepolicy.FarePolicy;

public class Fare {
    public static final int DEFAULT_FARE_TEN_KM = 1250;
    public static final int DEFAULT_FARE_FIFTY_KM = 2050;

    public static final int TEN_KM = 10;
    public static final int FIFTY_KM = 50;

    public static final int PER_EIGHT_KM = 8;
    public static final int PER_FIVE_KM = 5;

    private final FarePolicy farePolicy;

    public Fare(FarePolicy farePolicy) {
        this.farePolicy = farePolicy;
    }

    public int calculateTotalFare(int distance, int maxExtraLineFare) {
        int fare = calculateTotalFareByDistance(distance);
        return this.farePolicy.discount(fare + maxExtraLineFare);
    }

    private int calculateTotalFareByDistance(int distance) {
        if (distance <= TEN_KM) {
            return DEFAULT_FARE_TEN_KM;
        }
        if (distance <= FIFTY_KM) {
            distance = distance - TEN_KM;
            return additionalFareByPerKilo(DEFAULT_FARE_TEN_KM, PER_FIVE_KM, distance);
        }

        distance = distance - FIFTY_KM;
        return additionalFareByPerKilo(DEFAULT_FARE_FIFTY_KM, PER_EIGHT_KM, distance);
    }

    private int additionalFareByPerKilo(int defaultFare, int perKm, int distance) {
        return defaultFare + (((distance - 1) / perKm + 1) * 100);
    }
}
