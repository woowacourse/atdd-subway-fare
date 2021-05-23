package wooteco.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class AgeAppliedRuleTest {

    @ParameterizedTest
    @DisplayName("나이 매칭 ")
    @MethodSource("matchedAge")
    void create(int age, AgeAppliedRule ageAppliedRule) {
        assertThat(AgeAppliedRule.matchRule(age))
            .isEqualTo(ageAppliedRule);
    }

    private static Stream<Arguments> matchedAge() {
        return Stream.of(
            Arguments.of(3, AgeAppliedRule.INFANT),
            Arguments.of(9, AgeAppliedRule.CHILD),
            Arguments.of(15, AgeAppliedRule.JUVENILLE),
            Arguments.of(20, AgeAppliedRule.OTHER)
        );
    }
}
