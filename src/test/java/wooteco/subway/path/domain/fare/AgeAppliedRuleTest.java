package wooteco.subway.path.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import wooteco.subway.path.domain.fare.AgeAppliedRule;

public class AgeAppliedRuleTest {

    private static final int BASIC_FARE = 1250;

    @ParameterizedTest
    @DisplayName("나이별 요금 계산")
    @MethodSource("ageFare")
    void ageRate(int age, int expectedFare) {
        //when
        int fare = AgeAppliedRule.applyRule(BASIC_FARE, age);

        //then
        assertThat(fare).isEqualTo(expectedFare);
    }

    private static Stream<Arguments> ageFare() {
        return Stream.of(
            Arguments.of(20, 1250),
            Arguments.of(15, 720),
            Arguments.of(8, 450),
            Arguments.of(3, 0)
        );
    }
}
