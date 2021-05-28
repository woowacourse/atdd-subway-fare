package wooteco.subway.path.domain.fare.age;

import java.util.Arrays;
import java.util.function.IntPredicate;
import java.util.function.Supplier;

public enum AgeFarePolicy {
    BABY(
        age -> age <= 5,
        AgeFarePolicy::getBabyFare
    ),
    CHILD(
        age -> 5 < age && age <= 12,
        AgeFarePolicy::getChildFare
    ),
    TEENAGER(
        age -> 12 < age && age <= 18,
        AgeFarePolicy::getTeenagerFare
    ),
    ADULT(
        age -> 18 < age,
        AgeFarePolicy::getAdultFare
    );

    private final IntPredicate agePredicate;
    private final Supplier<AgeFare> ageFareSupplier;

    AgeFarePolicy(IntPredicate agePredicate, Supplier<AgeFare> ageFareSupplier) {
        this.agePredicate = agePredicate;
        this.ageFareSupplier = ageFareSupplier;
    }

    public static AgeFare getAgeFareByAge(int age) {
        AgeFarePolicy ageFarePolicy = getFarePolicyByAge(age);
        return ageFarePolicy.getAgeFare();
    }

    private static AgeFarePolicy getFarePolicyByAge(int age) {
        return Arrays.stream(AgeFarePolicy.values())
            .filter(ageFarePolicy -> ageFarePolicy.agePredicate.test(age))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("해당 나이에 해당하는 요금 정책이 없습니다."));
    }

    private static AgeFare getBabyFare() {
        return new BabyFare();
    }

    private static AgeFare getChildFare() {
        return new ChildFare();
    }

    private static AgeFare getTeenagerFare() {
        return new TeenagerFare();
    }

    private static AgeFare getAdultFare() {
        return new AdultFare();
    }

    private AgeFare getAgeFare() {
        return ageFareSupplier.get();
    }
}