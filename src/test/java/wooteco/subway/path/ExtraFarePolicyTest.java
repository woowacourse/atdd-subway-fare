package wooteco.subway.path;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.path.domain.policy.extrafarepolicy.DefaultExtraFarePolicy;
import wooteco.subway.path.domain.policy.extrafarepolicy.FiftyOverPolicyExtraFare;
import wooteco.subway.path.domain.policy.extrafarepolicy.TenFiftyPolicyExtraFare;

@DisplayName("요금 정책 테스트")
public class ExtraFarePolicyTest {

    @DisplayName("기본 요금 정책")
    @ParameterizedTest
    @CsvSource(value = {"1, 1250", "2, 1250"})
    void defaultFarePolicy(int distance, int expectedFare) {
        DefaultExtraFarePolicy defaultExtraFarePolicy = new DefaultExtraFarePolicy();

        assertThat(defaultExtraFarePolicy.calculate(distance))
            .isEqualTo(BigDecimal.valueOf(expectedFare));
    }

    @DisplayName("기본 요금 정책 조건")
    @ParameterizedTest
    @CsvSource(value = {"0, false", "1, true"})
    void defaultFarePolicyCondition(int distance, boolean expected) {
        DefaultExtraFarePolicy defaultExtraFarePolicy = new DefaultExtraFarePolicy();

        assertThat(defaultExtraFarePolicy.isSatisfied(distance))
            .isEqualTo(expected);
    }

    @DisplayName("10~50km 요금 정책")
    @ParameterizedTest
    @CsvSource(value = {"11, 100", "16, 200", "15, 100", "50, 800"})
    void tenFiftyPolicy(int distance, int expectedFare) {
        TenFiftyPolicyExtraFare tenFiftyPolicyExtraFare = new TenFiftyPolicyExtraFare();

        assertThat(tenFiftyPolicyExtraFare.calculate(distance))
            .isEqualTo(BigDecimal.valueOf(expectedFare));
    }

    @DisplayName("10~50km 요금 정책 조건")
    @ParameterizedTest
    @CsvSource(value = {"10, false", "11, true", "50, true", "51, true"})
    void tenFiftyPolicyCondition(int distance, boolean expected) {
        TenFiftyPolicyExtraFare tenFiftyPolicyExtraFare = new TenFiftyPolicyExtraFare();

        assertThat(tenFiftyPolicyExtraFare.isSatisfied(distance)).isEqualTo(expected);
    }

    @DisplayName("50km~ 요금 정책")
    @ParameterizedTest
    @CsvSource(value = {"51, 100", "58, 100", "59, 200"})
    void fiftyOverPolicy(int distance, int expectedFare) {
        FiftyOverPolicyExtraFare fiftyOverPolicyExtraFare = new FiftyOverPolicyExtraFare();

        assertThat(fiftyOverPolicyExtraFare.calculate(distance))
            .isEqualTo(BigDecimal.valueOf(expectedFare));
    }

    @DisplayName("50km~ 요금 정책 조건")
    @ParameterizedTest
    @CsvSource(value = {"50, false", "51, true"})
    void fiftyOverPolicyCondition(int distance, boolean expected) {
        FiftyOverPolicyExtraFare fiftyOverPolicyExtraFare = new FiftyOverPolicyExtraFare();

        assertThat(fiftyOverPolicyExtraFare.isSatisfied(distance))
            .isEqualTo(expected);
    }
}
