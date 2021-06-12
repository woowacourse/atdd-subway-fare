package wooteco.subway.path.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class DiscountPolicyTest {

    @ParameterizedTest(name = "거리 추가 요금 계산 테스트")
    @CsvSource(value = {"5:1350:1350", "6:1350:850", "13:1350:550", "19:1350:0"}, delimiter = ':')
    void apply(int age, int fare, int expected) {

        // when
        final int discountFare = DiscountPolicy.apply(age, fare);

        // then
        assertThat(discountFare).isEqualTo(expected);
    }

}
