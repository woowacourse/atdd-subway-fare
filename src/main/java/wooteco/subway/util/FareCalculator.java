package wooteco.subway.util;

import wooteco.subway.AgePolicy;
import wooteco.subway.path.domain.SectionEdge;
import wooteco.subway.path.domain.SubwayPath;

import java.util.List;

public class FareCalculator {
    public static final int DEFAULT_FARE = 1250;
    public static final int DEFAULT_DISTANCE_PIVOT = 10;
    public static final int DEFAULT_CONDITION_PIVOT = 5;
    public static final int LONG_DISTANCE_PIVOT = 50;
    public static final int LONG_CONDITION_PIVOT = 8;
    public static final int ADDITIONAL_FARE = 100;

    public static int calculateFare(final SubwayPath subwayPath, final Integer age) {
        int extraFareByDistance = DEFAULT_FARE + getMaximumExtraFareByLine(subwayPath.getSectionEdges()) + getExtraFareByDistance(subwayPath.getDistance());
        return getDiscountedFareByAge(extraFareByDistance, age);
    }

    private static int getMaximumExtraFareByLine(List<SectionEdge> sectionEdges) {
        return sectionEdges.stream()
                .mapToInt(sectionEdge -> sectionEdge.getLine().getExtraFare())
                .max()
                .orElse(0);
    }

    private static int getExtraFareByDistance(final int distance) {
        if (distance <= DEFAULT_DISTANCE_PIVOT) {
            return 0;
        }
        if (distance > LONG_DISTANCE_PIVOT) {
            return 800 + calculateAdditionalFareByDistance(distance - LONG_DISTANCE_PIVOT, LONG_CONDITION_PIVOT);
        }
        return calculateAdditionalFareByDistance(distance - DEFAULT_DISTANCE_PIVOT, DEFAULT_CONDITION_PIVOT);
    }

    private static int calculateAdditionalFareByDistance(int distance, int condition) {
        return (int) ((Math.ceil((distance - 1) / condition) + 1) * ADDITIONAL_FARE);
    }

    private static int getDiscountedFareByAge(int fare, int age) {
        if (AgePolicy.isChildren(age)) {
            return (int) ((fare - 350) * 0.5);
        }
        if (AgePolicy.isTeenage(age)) {
            return (int) ((fare - 350) * 0.8);
        }
        return fare;
    }
}
