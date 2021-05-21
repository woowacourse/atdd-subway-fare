package wooteco.subway.path.application;

import wooteco.subway.path.domain.Fare;

public class FareCalculator {
    public Fare getExtraFareByDistance(Fare currentFare, int distance) {
        if (distance <= 10) {
            return currentFare;
        }
        if (distance <= 50) {
            int extraDistance = distance - 10;
            int extraFare = (int) (Math.ceil(extraDistance / 5.0) * 100);
            return getExtraFareByDistance(currentFare, 10).add(new Fare(extraFare));
        }
        int extraDistance = distance - 50;
        int extraFare = (int) (Math.ceil(extraDistance / 8.0) * 100);
        return getExtraFareByDistance(currentFare, 50).add(new Fare(extraFare));
    }


}
