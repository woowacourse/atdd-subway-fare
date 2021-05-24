package wooteco.subway.path.domain;

import wooteco.subway.line.domain.Line;

import java.util.List;

public class FareCalculator {

    public static final int BASIC_DISTANCE = 10;
    public static final int MIDDLE_DISTANCE = 50;

    public static final int BASIC_FARE = 1250;
    public static final int OVER_FARE = 100;

    public static int calculateFare(int distance) {
        if (distance <= BASIC_DISTANCE) {
            return BASIC_FARE;
        }
        if (distance <= MIDDLE_DISTANCE) {
            return calculateExtraFareOfFirstRange(distance) + BASIC_FARE;
        }
        return calculateExtraFareOfSecondRange(distance) + BASIC_FARE;
    }

    private static int calculateExtraFareOfFirstRange(int distance) {
        return calculateOverFare(distance - BASIC_DISTANCE, 5, OVER_FARE);
    }

    private static int calculateExtraFareOfSecondRange(int distance) {
        int beforeFare = calculateExtraFareOfFirstRange(MIDDLE_DISTANCE);
        return beforeFare + calculateOverFare(
                distance - MIDDLE_DISTANCE,
                8,
                OVER_FARE
        );
    }

    private static int calculateOverFare(int distance, int overDistance, int overFare) {
        return (int) ((Math.ceil((distance - 1) / overDistance) + 1) * overFare);
    }

    public static int calculateFareWithLine(int basicFare, List<Line> lines) {
        return basicFare + lines.stream()
                .mapToInt(Line::getFare)
                .max()
                .orElseThrow(() -> new IllegalArgumentException("노선이 존재하지 않습니다."));
    }

    public static int discountFareByAge(int fare, int age) {
        final Discount discount = Discount.of(age);
        return discount.getDiscountedFare(fare);
    }
}
