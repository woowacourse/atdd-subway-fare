package wooteco.subway.path.domain.fare.decorator;

import wooteco.subway.path.domain.fare.FarePolicy;

import java.util.Arrays;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

public class DistanceFarePolicyDecorator extends FarePolicyDecorator {
    private static final int FARE_PER_UNIT_DISTANCE = 100;
    private static final int FIRST_ADDITIONAL_FARE_SECTION = 10;
    private static final int SECOND_ADDITIONAL_FARE_SECTION = 50;
    private static final int FIRST_ADDITIONAL_FARE_UNIT_DISTANCE = 5;
    private static final int SECOND_ADDITIONAL_FARE_UNIT_DISTANCE = 8;

    int distance;

    public DistanceFarePolicyDecorator(int distance, FarePolicy farePolicy) {
        super(farePolicy);
        this.distance = distance;
    }

    @Override
    public int calculate() {
        return super.calculate() + FarePolicyByDistance.of(distance).calculate.applyAsInt(distance);
    }

    enum FarePolicyByDistance {
        NO_EXTRA_CHARGE((distance) -> distance <= FIRST_ADDITIONAL_FARE_SECTION, (distance) -> 0),
        FIRST_SECTION((distance) -> FIRST_ADDITIONAL_FARE_SECTION < distance && distance <= SECOND_ADDITIONAL_FARE_SECTION, FarePolicyByDistance::fareInFirstSection),
        SECOND_SECTION((distance) -> SECOND_ADDITIONAL_FARE_SECTION < distance, FarePolicyByDistance::fareInSecondSection);

        IntPredicate checkDistanceRange;
        IntUnaryOperator calculate;

        FarePolicyByDistance(IntPredicate checkDistanceRange, IntUnaryOperator calculate) {
            this.checkDistanceRange = checkDistanceRange;
            this.calculate = calculate;
        }

        private static FarePolicyByDistance of(int distance) {
            return Arrays.stream(values())
                    .filter(farePolicyByDistance -> farePolicyByDistance.checkDistanceRange.test(distance))
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        }

        private static int fareInFirstSection(int distance) {
            int firstSectionDistance = Math.min(distance, SECOND_ADDITIONAL_FARE_SECTION) - FIRST_ADDITIONAL_FARE_SECTION;
            return calculateAdditionalFare(firstSectionDistance, FIRST_ADDITIONAL_FARE_UNIT_DISTANCE);
        }

        private static int fareInSecondSection(int distance) {
            int secondSectionDistance = distance - SECOND_ADDITIONAL_FARE_SECTION;
            int secondSectionFare = calculateAdditionalFare(secondSectionDistance, SECOND_ADDITIONAL_FARE_UNIT_DISTANCE);
            return fareInFirstSection(distance) + secondSectionFare;
        }

        private static int calculateAdditionalFare(int additionalDistance, int unitDistance) {
            return (int) ((Math.ceil((additionalDistance - 1) / unitDistance) + 1) * FARE_PER_UNIT_DISTANCE);
        }
    }
}
