package wooteco.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class AgeFareCalculatorTest {

    private static Stream<Arguments> fareByAge() {
        return Stream.of(
                Arguments.of(20, 1250, 0, 1250),
                Arguments.of(15, 1250, 0, 720),
                Arguments.of(9, 1250, 0, 450),
                Arguments.of(3, 1250, 0, 0)
        );
    }

    @DisplayName("연령별 요금 계산 결과 확인")
    @ParameterizedTest
    @MethodSource("fareByAge")
    void ageCalculator(int age, int fare, int lineFare, int totalFare) {
        assertThat(totalFare).isEqualTo(AgeFareCalculator.of(age, fare, lineFare));
    }
}
