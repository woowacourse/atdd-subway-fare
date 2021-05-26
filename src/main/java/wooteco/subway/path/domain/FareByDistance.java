package wooteco.subway.path.domain;

import java.util.Arrays;
import java.util.function.Function;

public enum FareByDistance {
    DEFAULT_FARE(0, 10, distance -> {
        return Constants.DEFAULT_FARE;
    }),
    OVER_TEN_KILOMETERS_FARE(10, 50, distance -> {
        return Constants.DEFAULT_FARE +
                extraFareOverTenKilometers(distance - Constants.MAX_DISTANCE_FOR_DEFAULT_FARE);
    }),
    OVER_FIFTY_KILOMETERS_FARE(50, distance -> {
        return Constants.DEFAULT_FARE +
                extraFareOverTenKilometers(Constants.MAX_DISTANCE_FOR_OVER_TEN_KILOMETERS_FARE) +
                extraFareOverFiftyKilometers(distance - Constants.MAX_DISTANCE_FOR_DEFAULT_FARE - Constants.MAX_DISTANCE_FOR_OVER_TEN_KILOMETERS_FARE);
    });

    private static class Constants {
        public static final int DEFAULT_FARE = 1250;
        public static final int MAX_DISTANCE_FOR_DEFAULT_FARE = 10;
        public static final int MAX_DISTANCE_FOR_OVER_TEN_KILOMETERS_FARE = 40;
        public static final int EXTRA_FARE_PER_SECTION = 100;
        public static final int OVER_TEN_KILOMETERS_FARE_SECTION_STANDARD_DISTANCE = 5;
        public static final int OVER_FIFTY_KILOMETERS_FARE_SECTION_STANDARD_DISTANCE = 8;
        public static final int MAX_DISTANCE_OF_TOTAL_SUBWAY_PATH = 10000;
    }

    private static int extraFareOverTenKilometers(Integer distance) {
        return calculateExtraFare(distance, Constants.OVER_TEN_KILOMETERS_FARE_SECTION_STANDARD_DISTANCE);
    }

    private static int extraFareOverFiftyKilometers(Integer distance) {
        return calculateExtraFare(distance, Constants.OVER_FIFTY_KILOMETERS_FARE_SECTION_STANDARD_DISTANCE);
    }

    private static int calculateExtraFare(Integer totalDistanceToPay, int sectionStandard) {
        final double sectionsToPay = Math.ceil(((totalDistanceToPay - 1) / sectionStandard) + 1);
        return (int) sectionsToPay * Constants.EXTRA_FARE_PER_SECTION;
    }

    private final int distanceFrom;
    private final int distanceTo;
    private final Function<Integer, Integer> farePolicy;

    FareByDistance(int distanceFrom, int distanceTo, Function<Integer, Integer> farePolicy) {
        this.distanceFrom = distanceFrom;
        this.distanceTo = distanceTo;
        this.farePolicy = farePolicy;
    }

    FareByDistance(int distanceFrom, Function<Integer, Integer> farePolicy) {
        this.distanceFrom = distanceFrom;
        this.distanceTo = Constants.MAX_DISTANCE_OF_TOTAL_SUBWAY_PATH;
        this.farePolicy = farePolicy;
    }

    public static int calculate(int distance) {
        final FareByDistance chosenFareByDistance = Arrays.stream(FareByDistance.values())
                .filter(fareByDistance -> fareByDistance.distanceFrom < distance && distance <= fareByDistance.distanceTo)
                .findAny()
                .orElseThrow(IllegalStateException::new);

        return chosenFareByDistance.farePolicy.apply(distance);
    }
}
