package wooteco.subway.path.domain;

import java.util.Arrays;
import java.util.function.Predicate;

public enum FareByAge {
    BABY(0, 0, (age) -> age < 6),
    CHILDREN(0.5, 350, (age) -> age >= 6 && age < 13),  //TODO: 전략으로 분리
    TEENAGER(0.8, 350, (age) -> age >= 13 && age < 19),
    ADULT(1, 0, (age) -> age >= 19);

    private final double rate;
    private final int deductionFare;
    private final Predicate<Integer> match;

    FareByAge(double rate, int deductionFare, Predicate<Integer> match) {
        this.rate = rate;
        this.deductionFare = deductionFare;
        this.match = match;
    }

    public static int calculate(int price, Integer age) {
        return Arrays.stream(values())
            .filter(fareByAge -> fareByAge.match.test(age))
            .map(fareByAge -> fareByAge.calculateFare(price))
            .findAny()
            .orElseThrow(RuntimeException::new);
    }

    private int calculateFare(int price) {
        return (int)((price - deductionFare) * rate);
    }
}
