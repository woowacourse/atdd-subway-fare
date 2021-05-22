package wooteco.subway.path.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class DistanceExtraFarePolicyTest {

    @ParameterizedTest(name = "거리 추가 요금 계산 테스트")
    @CsvSource(value = {"10:0", "50:800", "90:1000"}, delimiter = ':')
    void apply(int distance, int expected) {

        // when
        final int extraFare = DistanceExtraFarePolicy.apply(distance);

        // then
        assertThat(extraFare).isEqualTo(expected);
    }
}
