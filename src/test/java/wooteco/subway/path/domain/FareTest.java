package wooteco.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import wooteco.subway.path.domain.strategy.DistanceAdditionPolicy;
import wooteco.subway.path.domain.strategy.additional.AgeDiscountFactory;
import wooteco.subway.path.domain.strategy.additional.AgeDiscountPolicy;
import wooteco.subway.path.domain.strategy.additional.NoDiscount;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("요금 계산 도메인")
class FareTest {
    private static final int DEFAULT_FARE = 1250;

    private static Stream<Arguments> calculateByDistance() {
        return Stream.of(
                Arguments.of(9, 0, DEFAULT_FARE),
                Arguments.of(11, 0, DEFAULT_FARE + 100),
                Arguments.of(51, 0, DEFAULT_FARE + 900),
                Arguments.of(61, 300, DEFAULT_FARE + 1000 + 300)
        );
    }

    @DisplayName("거리에 따른 요금이 계산된다.")
    @ParameterizedTest
    @MethodSource
    void calculateByDistance(int distance, int extraFare, int result) {
        //given
        DistanceAdditionPolicy distanceAdditionPolicy = DistanceAdditionFactory.create(distance, DEFAULT_FARE);
        //when
        Fare fare = new Fare(distanceAdditionPolicy, new NoDiscount(), extraFare);
        int calculateFare = fare.calculateFare();

        //then
        assertThat(calculateFare).isEqualTo(result);
    }

    private static Stream<Arguments> calculateByDistanceWithAge() {
        return Stream.of(
                Arguments.of(9, 0, 5, 0),
                Arguments.of(11, 0, 12, (int) ((DEFAULT_FARE + 100) * 0.5)),
                Arguments.of(51, 0, 18, (int) ((DEFAULT_FARE + 900) * 0.8)),
                Arguments.of(61, 300, 60, DEFAULT_FARE + 1000 + 300)
        );
    }

    @DisplayName("6세 미만이면 운행요금 없음" +
            "청소년 (6세 이상 ~ 13세 미만)인 경우 50%," +
            "청소년 (13세 이상 ~ 19세 미만)인 경우 20% 할인된 금액이 적용된다 " +
            "할인에 포함되지 않으면 할이되지 않는다.")
    @ParameterizedTest
    @MethodSource
    void calculateByDistanceWithAge(int distance, int extraFare, int age, int result) {
        //given
        DistanceAdditionPolicy distanceAdditionPolicy = DistanceAdditionFactory.create(distance, DEFAULT_FARE);
        AgeDiscountPolicy ageDiscountPolicy = AgeDiscountFactory.create(age);
        //when
        Fare fare = new Fare(distanceAdditionPolicy, ageDiscountPolicy, extraFare);
        int calculateFare = fare.calculateFare();

        //then
        assertThat(calculateFare).isEqualTo(result);
    }
}
