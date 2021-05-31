package wooteco.subway.service;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class AgeDiscountFareCalculator implements FareCalculator {

    private static final int DEFAULT_EXEMPTION_FARE = 350;

    private final FareCalculator fareCalculator;
    private final int age;

    public AgeDiscountFareCalculator(FareCalculator fareCalculator, int age) {
        this.fareCalculator = fareCalculator;
        this.age = age;
    }

    @Override
    public int calculateFare(int distance, int extraFare) {
        final int fare = fareCalculator.calculateFare(distance, extraFare);
        return FarePolicyByAge.decidePolicy(age).calculate(fare);
    }

    private enum FarePolicyByAge {
        BABY(age -> AgeUtils.checkAge(age).isBetween(0, 5),
            fare -> 0),
        CHILDREN(age -> AgeUtils.checkAge(age).isBetween(6, 12),
            fare ->
                FareUtils.originalFare(fare - DEFAULT_EXEMPTION_FARE)
                    .discountPercentage(0.5)
                    .calculate()
        ),
        TEENAGER(age -> AgeUtils.checkAge(age).isBetween(13, 18),
            fare ->
                FareUtils.originalFare(fare - DEFAULT_EXEMPTION_FARE)
                    .discountPercentage(0.2)
                    .calculate()
        ),
        ADULT(age -> AgeUtils.checkAge(age).isGreaterThan(18),
            fare -> fare);

        private final Predicate<Integer> predicate;
        private final UnaryOperator<Integer> operator;

        FarePolicyByAge(Predicate<Integer> predicate, UnaryOperator<Integer> operator) {
            this.predicate = predicate;
            this.operator = operator;
        }

        private static FarePolicyByAge decidePolicy(int age) {
            return Arrays.stream(values())
                .filter(val -> val.predicate.test(age))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
        }

        private int calculate(int fare) {
            return operator.apply(fare);
        }
    }

    private static class AgeUtils {
        private final int age;

        private AgeUtils(int age) {
            this.age = age;
        }

        private static AgeUtils checkAge(int age) {
            return new AgeUtils(age);
        }

        private boolean isBetween(int min, int max) {
            return age >= min && age <= max;
        }

        private boolean isGreaterThan(int number) {
            return age > number;
        }
    }

    private static class FareUtils {

        private final int fare;

        private FareUtils(int fare) {
            this.fare = fare;
        }

        private static FareUtils originalFare(int originalFare) {
            return new FareUtils(originalFare);
        }

        private FareUtils discountPercentage(double percentage) {
            return new FareUtils(fare - (int) (fare * percentage));
        }

        private int calculate() {
            return fare;
        }
    }
}
