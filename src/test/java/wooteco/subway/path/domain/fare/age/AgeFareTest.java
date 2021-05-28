package wooteco.subway.path.domain.fare.age;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.path.domain.fare.Fare;

class AgeFareTest {
    @DisplayName("나이에 따른 요금 할인 정책을 적용한다.")
    @ParameterizedTest(name = "{3}")
    @CsvSource({
        "18, 720, 청소년",
        "12, 450, 어린이",
        "19, 1250, 성인"
    })
    void getFareWithAge(int age, int expectedFare, String testCaseName) {
        // given
        Fare currentFare = new Fare(1_250);

        // when
        AgeFare ageFare = AgeFarePolicy.getAgeFareByAge(age);
        Fare fareDiscountedByAge = ageFare.getFare(currentFare);

        // then
        assertThat(fareDiscountedByAge).isEqualTo(new Fare(expectedFare));
    }
}