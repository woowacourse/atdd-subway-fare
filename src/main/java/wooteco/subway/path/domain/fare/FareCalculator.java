package wooteco.subway.path.domain.fare;

import wooteco.subway.path.domain.SubwayPath;

public class FareCalculator {

    public static int calculateFare(final SubwayPath subwayPath, final Integer age) {
        int fareByDistance = getExtraFareByDistance(subwayPath.getDistance(), subwayPath.getMaximumExtraFareByLine());
        return getDiscountedFareByAge(fareByDistance, age);
    }

    private static int getExtraFareByDistance(final int distance, final int fare) {
        return DistancePolicy.getFareApplyPolicy(distance, fare);
    }

    private static int getDiscountedFareByAge(final int fare, final int age) {
        return AgePolicy.getFareApplyPolicy(fare, age);
    }
}
