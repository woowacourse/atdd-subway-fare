package wooteco.subway.domain;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class AgeDiscountFareCalculator implements FareCalculator {
    private static final String POLICY_ERROR_MESSAGE = "존재하지 않는 요금 정책입니다.";
    private static final int SIX = 6;
    private static final int THIRTEEN = 13;
    private static final int NINETEEN = 19;
    private static final int DEFAULT_EXEMPTION_FARE = 350;
    private static final double CHILDREN_DISCOUNT_RATE = 0.5;
    private static final double TEENAGER_DISCOUNT_RATE = 0.2;

    private final FareCalculator fareCalculator;
    private final int age;

    public AgeDiscountFareCalculator(FareCalculator fareCalculator, int age) {
        this.fareCalculator = fareCalculator;
        this.age = age;
    }

    @Override
    public int calculateFare(int distance, int extraFare) {
        int defaultFare = fareCalculator.calculateFare(distance, extraFare);
        return FarePolicyByAge.decidePolicy(age).calculate(defaultFare);
    }

    private enum FarePolicyByAge {
        BABY(age -> age < SIX,
                fare -> 0),
        CHILDREN(age -> age >= SIX && age < THIRTEEN,
                fare -> (fare - DEFAULT_EXEMPTION_FARE) - (int)((fare - DEFAULT_EXEMPTION_FARE) * CHILDREN_DISCOUNT_RATE)),
        TEENAGER(age -> age >= THIRTEEN && age < NINETEEN,
                fare -> (fare - DEFAULT_EXEMPTION_FARE) - (int)((fare - DEFAULT_EXEMPTION_FARE) * TEENAGER_DISCOUNT_RATE)),
        ADULT(age -> age >= NINETEEN,
                fare -> fare);

        private final Predicate<Integer> predicate;
        private final UnaryOperator<Integer> function;

        FarePolicyByAge(Predicate<Integer> predicate, UnaryOperator<Integer> function) {
            this.predicate = predicate;
            this.function = function;
        }

        private static FarePolicyByAge decidePolicy(int age) {
            return Arrays.stream(values())
                    .filter(val -> val.predicate.test(age))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException(POLICY_ERROR_MESSAGE));
        }

        private int calculate(int fare) {
            return function.apply(fare);
        }
    }
}
