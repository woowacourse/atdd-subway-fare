package wooteco.subway.path.domain.fare.decorator;

import wooteco.subway.line.application.LineException;
import wooteco.subway.path.domain.fare.FarePolicy;

import java.util.Arrays;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

public class AgeFarePolicyDecorator extends FarePolicyDecorator {
    private int age;

    public AgeFarePolicyDecorator(int age, FarePolicy farePolicy) {
        super(farePolicy);
        this.age = age;
    }

    @Override
    public int calculate() {
        return FarePolicyByAge.of(age).calculate.applyAsInt(super.calculate());
    }

    enum FarePolicyByAge {
        BABY((age) -> age < 6, FarePolicyByAge::baby),
        CHILD((age) -> age >= 6 && age < 13, FarePolicyByAge::child),
        TEENAGER((age) -> age >= 13 && age < 19, FarePolicyByAge::teenager),
        ADULT((age) -> age >= 19, FarePolicyByAge::adult);

        private static final int BABY_FARE = 0;
        private static final int DISCOUNT_DIFF = 350;
        private static final double CHILD_AFTER_DISCOUNT_RATIO = 0.5;
        private static final double TEENAGER_AFTER_DISCOUNT_RATIO = 0.8;

        IntPredicate checkAgeRange;
        IntUnaryOperator calculate;

        FarePolicyByAge(IntPredicate checkAgeRange, IntUnaryOperator calculate) {
            this.checkAgeRange = checkAgeRange;
            this.calculate = calculate;
        }

        private static FarePolicyByAge of(int age) {
            return Arrays.stream(values())
                    .filter(farePolicyByAge -> farePolicyByAge.checkAgeRange.test(age))
                    .findFirst()
                    .orElseThrow(() -> new LineException("존재하지 않는 나이 멤버입니다."));
        }

        public static int baby(int fare) {
            return BABY_FARE;
        }

        public static int child(int fare) {
            return (int) ((fare - DISCOUNT_DIFF) * CHILD_AFTER_DISCOUNT_RATIO);
        }

        public static int teenager(int fare) {
            return (int) ((fare - DISCOUNT_DIFF) * TEENAGER_AFTER_DISCOUNT_RATIO);
        }

        public static int adult(int fare) {
            return fare;
        }
    }
}
