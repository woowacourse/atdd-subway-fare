package wooteco.subway.service;

import wooteco.subway.domain.FareCalculator;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public class AgeDiscountFareCalculator implements FareCalculator {
    private final FareCalculator fareCalculator;
    private final int age;

    public AgeDiscountFareCalculator(FareCalculator fareCalculator, int age) {
        this.fareCalculator = fareCalculator;
        this.age = age;
    }

    @Override
    public int calculateFare(int distance, int extraFare) {
        final int fare = fareCalculator.calculateFare(distance, extraFare);
        return FarePolicyByAge.decidePolicy(age)
                .calculate(fare);
    }

    private enum FarePolicyByAge {
        BABY(age -> age < 6,
                fare -> 0),
        CHILDREN(age -> age >= 6 && age < 13,
                fare -> (int) ((fare - 350) * 0.5)),
        TEENAGER(age -> age >= 13 && age < 19,
                fare -> (int) ((fare - 350) * 0.8)),
        ADULT(age -> age >= 19,
                fare -> fare);
        private final Predicate<Integer> predicate;
        private final Function<Integer, Integer> function;

        FarePolicyByAge(Predicate<Integer> predicate, Function<Integer, Integer> function) {
            this.predicate = predicate;
            this.function = function;
        }

        private static FarePolicyByAge decidePolicy(int age) {
            return Arrays.stream(values())
                    .filter(val -> val.predicate.test(age))
                    .findAny()
                    .orElseThrow(IllegalArgumentException::new);
        }

        private int calculate(int fare) {
            return function.apply(fare);
        }
    }
}