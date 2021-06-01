package wooteco.subway.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.params.ParameterizedTest.ARGUMENTS_PLACEHOLDER;

public class AgeTest {

    @DisplayName("나이는 음수일 수 없다.")
    @Test
    void checkPositiveNumber() {
        assertThatCode(() -> new Age(-1))
                .isInstanceOf(Exception.class);
    }

    @DisplayName("6세 미만은 불가.")
    @Test
    void checkMinimumAge() {
        assertThatCode(() -> new Age(5))
                .isInstanceOf(Exception.class);
    }

    @ParameterizedTest(name = "나이에 따라 요금이 할인된다. " + ARGUMENTS_PLACEHOLDER)
    @MethodSource("getDiscountedFareSource")
    void getDiscountedFare(int ageValue, int expectedFare) {
        // given
        Age age = new Age(ageValue);

        // when
        int discountedFare = age.getDiscountedFare(1250);

        // then
        assertThat(discountedFare).isEqualTo(expectedFare);
    }

    private static Stream<Arguments> getDiscountedFareSource() {
        return Stream.of(
                Arguments.of(6, 450),
                Arguments.of(12, 450),
                Arguments.of(13, 720),
                Arguments.of(18, 720),
                Arguments.of(19, 1250)
        );
    }
}
