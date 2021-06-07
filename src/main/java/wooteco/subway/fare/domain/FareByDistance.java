package wooteco.subway.fare.domain;

import java.util.Arrays;
import java.util.function.Function;

public enum FareByDistance {
    DEFAULT_FARE(0, 10, distance -> {
        return Constants.DEFAULT_FARE;
    }),
    OVER_TEN_FARE(10, 50, distance -> {
        return Constants.DEFAULT_FARE +
                extraFareOverTen(distance - Constants.MAX_DISTANCE_FOR_DEFAULT_FARE);
    }),
    OVER_FIFTY_FARE(50, Constants.MAX_DISTANCE_OF_SUBWAY_PATH, distance -> {
        return Constants.DEFAULT_FARE +
                extraFareOverTen(Constants.MAX_DISTANCE_FOR_OVER_TEN_FARE) +
                extraFareOverFifty(distance - Constants.MAX_DISTANCE_FOR_DEFAULT_FARE - Constants.MAX_DISTANCE_FOR_OVER_TEN_FARE);
    });

    private final int from;
    private final int to;
    private final Function<Integer, Integer> farePolicy;

    FareByDistance(int from, int to, Function<Integer, Integer> farePolicy) {
        this.from = from;
        this.to = to;
        this.farePolicy = farePolicy;
    }

    private static int extraFareOverTen(Integer distance) {
        return calculateExtraFare(distance, Constants.OVER_TEN_FARE_SECTION_STANDARD_DISTANCE);
    }

    private static int extraFareOverFifty(Integer distance) {
        return calculateExtraFare(distance, Constants.OVER_FIFTY_FARE_SECTION_STANDARD_DISTANCE);
    }

    private static int calculateExtraFare(Integer totalDistanceToPay, int sectionStandard) {
        final double sectionsToPay = Math.ceil(((totalDistanceToPay - 1) / sectionStandard) + 1);
        return (int) sectionsToPay * Constants.EXTRA_FARE_PER_SECTION;
    }

    public static int calculate(int distance) {
        final FareByDistance chosenFareByDistance = Arrays.stream(FareByDistance.values())
                .filter(fareByDistance -> fareByDistance.from < distance && distance <= fareByDistance.to)
                .findAny()
                .orElseThrow(IllegalStateException::new);

        return chosenFareByDistance.farePolicy.apply(distance);
    }

    private static class Constants {
        private static final int DEFAULT_FARE = 1250;
        private static final int EXTRA_FARE_PER_SECTION = 100;
        private static final int OVER_TEN_FARE_SECTION_STANDARD_DISTANCE = 5;
        private static final int OVER_FIFTY_FARE_SECTION_STANDARD_DISTANCE = 8;
        private static final int MAX_DISTANCE_FOR_DEFAULT_FARE = 10;
        private static final int MAX_DISTANCE_FOR_OVER_TEN_FARE = 40;
        private static final int MAX_DISTANCE_OF_SUBWAY_PATH = 1000;
    }
}
