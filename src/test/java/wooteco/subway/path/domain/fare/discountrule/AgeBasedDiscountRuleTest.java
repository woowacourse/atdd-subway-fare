package wooteco.subway.path.domain.fare.discountrule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import wooteco.subway.exception.application.ValidationFailureException;
import wooteco.subway.path.domain.fare.Fare;

class AgeBasedDiscountRuleTest {

    @DisplayName("나이 기반 할인 규칙 생성")
    @MethodSource("distanceRuleValues")
    @ParameterizedTest
    void createDiscountRule(int age, AgeBasedDiscountRule expectedAgeBasedDiscountRule) {
        // when, then
        assertThat(AgeBasedDiscountRule.from(age))
            .isEqualTo(expectedAgeBasedDiscountRule);
    }

    private static Stream<Arguments> distanceRuleValues() {
        return Stream.of(
            Arguments.of(0, AgeBasedDiscountRule.INFANT),
            Arguments.of(3, AgeBasedDiscountRule.INFANT),
            Arguments.of(8, AgeBasedDiscountRule.CHILD),
            Arguments.of(15, AgeBasedDiscountRule.JUVENILE),
            Arguments.of(20, AgeBasedDiscountRule.ADULT)
        );
    }

    @DisplayName("값이 음수이면 나이 기반 할인 규칙 생성 실패")
    @Test
    void createDiscountRule_fail_negativeAge() {
        // when, then
        assertThatThrownBy(() -> AgeBasedDiscountRule.from(-1))
            .isInstanceOf(ValidationFailureException.class)
            .hasMessageContaining("나이는 음수일 수 없습니다");
    }

    @DisplayName("나이 기반 할인 규칙 적용 - 유아")
    @Test
    void apply_infant() {
        // when, then
        Fare fare = AgeBasedDiscountRule.INFANT.apply(Fare.DEFAULT_FARE);
        assertThat(fare).isEqualTo(new Fare(0));
    }

    @DisplayName("나이 기반 할인 규칙 적용 - 어린이")
    @Test
    void apply_child() {
        // when, then
        Fare fare = AgeBasedDiscountRule.CHILD.apply(Fare.DEFAULT_FARE);
        assertThat(fare).isEqualTo(new Fare(450));
    }

    @DisplayName("나이 기반 할인 규칙 적용 - 청소년")
    @Test
    void apply_juvenile() {
        // when, then
        Fare fare = AgeBasedDiscountRule.JUVENILE.apply(Fare.DEFAULT_FARE);
        assertThat(fare).isEqualTo(new Fare(720));
    }

    @DisplayName("나이 기반 할인 규칙 적용 - 어른")
    @Test
    void apply_adult() {
        // when, then
        Fare fare = AgeBasedDiscountRule.ADULT.apply(Fare.DEFAULT_FARE);
        assertThat(fare).isEqualTo(new Fare(1250));
    }
}
