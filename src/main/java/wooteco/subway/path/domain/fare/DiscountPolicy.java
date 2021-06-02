package wooteco.subway.path.domain.fare;

import wooteco.subway.exception.NotFoundException;

import java.util.Arrays;
import java.util.function.Predicate;

public enum DiscountPolicy {
    ADULT("성인", 0, 1.0, (age) -> age >= 19),
    TEENAGER("청소년", 350, 0.8, (age) -> 13 <= age && age <= 18),
    CHILD("어린이", 350, 0.5, (age) -> 6 <= age && age < 13),
    BABY("유아", 0, 0.0, (age) -> age < 6);

    private final String korean;
    private final int staticDiscount;
    private final double discountRate;
    private final Predicate<Integer> findAge;

    DiscountPolicy(String korean, int staticDiscount, double discountRate, Predicate<Integer> findAge) {
        this.korean = korean;
        this.staticDiscount = staticDiscount;
        this.discountRate = discountRate;
        this.findAge = findAge;
    }

    public int staticDiscount() {
        return staticDiscount;
    }

    public double discountRate() {
        return discountRate;
    }

    public String getKorean() {
        return korean;
    }

    public static DiscountPolicy findByAge(int age) {
        return Arrays.stream(values())
                .filter(policy -> policy.findAge.test(age))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("나이에 해당 하는 요금 정책이 없습니다."));
    }

    public int apply(Fare fare) {
        return (int) ((fare.toInt() - staticDiscount) * discountRate);
    }
}
