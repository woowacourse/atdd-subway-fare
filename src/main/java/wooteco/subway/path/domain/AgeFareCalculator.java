package wooteco.subway.path.domain;

import java.util.Arrays;
import java.util.function.BinaryOperator;

public enum AgeFareCalculator {
    PRE_SCHOOL(0, 6, (fare, lineFare) -> 0 ),
    CHILD(6, 13, (fare, lineFare) -> (int)(((fare+lineFare) - 350 ) * 0.5)),
    TEENAGER(13, 19, (fare, lineFare) -> (int)(((fare+lineFare) - 350 ) * 0.8)),
    ADULT(19, 200, Integer::sum);

    private final int minAge;
    private final int maxAge;
    private final BinaryOperator<Integer> adjuster;

    AgeFareCalculator(int minAge, int maxAge, BinaryOperator<Integer> adjuster) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.adjuster = adjuster;
    }

    public static int of(int age, int fare, int lineFare) {
        return Arrays.stream(values())
                .filter(it -> it.minAge <= age && age < it.maxAge)
                .mapToInt(it -> it.adjuster.apply(fare, lineFare))
                .findAny()
                .orElseThrow(IllegalAccessError::new);
    }
}
