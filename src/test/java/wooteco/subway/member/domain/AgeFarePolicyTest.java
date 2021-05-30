package wooteco.subway.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.path.domain.Fare;

import static org.assertj.core.api.Assertions.assertThat;

class AgeFarePolicyTest {
    @DisplayName("연령별 요금 계산")
    @ParameterizedTest
    @CsvSource({"6, 450", "13, 720", "18, 720", "19, 1250"})
    void agePolicyTest(int age, int expectedFare) {
        //given
        AgeFarePolicy agePolicy = AgeFarePolicy.of(age);
        //when
        Fare fare = agePolicy.calculateFare(new Fare(1250));
        //then
        assertThat(fare).isEqualTo(new Fare(expectedFare));
    }
}