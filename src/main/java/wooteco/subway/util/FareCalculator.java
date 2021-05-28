package wooteco.subway.util;

import wooteco.subway.path.domain.SectionEdge;
import wooteco.subway.path.domain.SubwayPath;

import java.util.List;

public class FareCalculator {
    private static final int ZERO = 0;
    private static final int DISTANCE_PIVOT = 10;
    private static final int ADDITIONAL_FARE_PIVOT = 5;
    private static final int LONG_DISTANCE_PIVOT = 50;
    private static final int LONG_ADDITIONAL_FARE_PIVOT = 8;
    private static final double CHILD_DISCOUNTED_RATE = 0.5;
    private static final double TEENAGE_DISCOUNTED_RATE = 0.8;

    public static int calculateFare(final SubwayPath subwayPath, final Integer age) {
        int extraFareByDistance = FarePolicy.DEFAULT_FARE.getFare() + getMaximumExtraFareByLine(subwayPath.getSectionEdges()) + getExtraFareByDistance(subwayPath.getDistance());
        return getDiscountedFareByAge(extraFareByDistance, age);
    }

    private static int getMaximumExtraFareByLine(List<SectionEdge> sectionEdges) {
        return sectionEdges.stream()
                .mapToInt(sectionEdge -> sectionEdge.getLine().getExtraFare())
                .max()
                .orElse(ZERO);
    }

    private static int getExtraFareByDistance(final int distance) {
        if (distance <= DISTANCE_PIVOT) {
            return ZERO;
        }
        if (distance > LONG_DISTANCE_PIVOT) {
            return calculateAdditionalFareByDistance(distance - (distance % LONG_DISTANCE_PIVOT) - DISTANCE_PIVOT, ADDITIONAL_FARE_PIVOT)
                    + calculateAdditionalFareByDistance(distance - LONG_DISTANCE_PIVOT, LONG_ADDITIONAL_FARE_PIVOT);
        }
        return calculateAdditionalFareByDistance(distance - DISTANCE_PIVOT, ADDITIONAL_FARE_PIVOT);
    }

    private static int calculateAdditionalFareByDistance(int distance, int condition) {
        return (int) ((Math.ceil((distance - 1) / condition) + 1) * FarePolicy.ADDITIONAL_FARE.getFare());
    }

    private static int getDiscountedFareByAge(int fare, int age) {
        if (AgePolicy.isChildren(age)) {
            return (int) ((fare - FarePolicy.MINOR_DISCOUNT_FARE.getFare()) * CHILD_DISCOUNTED_RATE);
        }
        if (AgePolicy.isTeenage(age)) {
            return (int) ((fare - FarePolicy.MINOR_DISCOUNT_FARE.getFare()) * TEENAGE_DISCOUNTED_RATE);
        }
        return fare;
    }
}
