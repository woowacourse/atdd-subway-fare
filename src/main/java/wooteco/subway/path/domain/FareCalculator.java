package wooteco.subway.path.domain;

import wooteco.subway.line.domain.Line;

import java.util.List;

public class FareCalculator {

    private static final int BASIC_FARE = 1250;

    public static int calculateFare(int distance) {
        return ExtraFare.calculateFare(distance) + BASIC_FARE;
    }

    public static int calculateFareWithLine(int basicFare, List<Line> lines) {
        return basicFare + lines.stream()
                .mapToInt(Line::getExtraFare)
                .max()
                .orElseThrow(() -> new IllegalArgumentException("노선이 존재하지 않습니다."));
    }

    public static int discountFareByAge(int fare, int age) {
        final Discount discount = Discount.of(age);
        return discount.getDiscountedFare(fare);
    }
}
