package wooteco.subway.path.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("AgeFarePolicy 클래스")
class AgeFarePolicyTest {

    @Nested
    @DisplayName("agePolicyAppliedFare 메서드는")
    class Describe_agePolicyAppliedFare {

        @Nested
        @DisplayName("사용자가 13세 이상 19세 미만 청소년이라면")
        class Context_with_teenager {

            @ParameterizedTest
            @ValueSource(ints = {13, 14, 15, 16, 17, 18})
            @DisplayName("기존 요금에서 350원을 공제한 요금의 20% 할인된 요금을 리턴한다.")
            void it_returns_discounted_fare(int age) {
                Assertions.assertEquals(AgeFarePolicy.agePolicyAppliedFare(1350, age), 800);
            }
        }

        @Nested
        @DisplayName("사용자가 6세 이상 13세 미만 어린이라면")
        class Context_with_children {

            @ParameterizedTest
            @ValueSource(ints = {6, 7, 8, 9, 10, 11, 12})
            @DisplayName("기존 요금에서 350원을 공제한 요금의 50% 할인된 요금을 리턴한다.")
            void it_returns_discounted_fare() {
                Assertions.assertEquals(AgeFarePolicy.agePolicyAppliedFare(1350, 6), 500);
            }
        }
    }

}