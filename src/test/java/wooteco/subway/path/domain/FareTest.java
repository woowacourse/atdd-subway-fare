package wooteco.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;

class FareTest {

    @ParameterizedTest
    @DisplayName("거리별 기본 요금 계산")
    @MethodSource("matchedFare")
    void defaultRate(int distance, int predictedFare, LoginMember member, int extraFare) {
        //when
        Fare fare = new Fare(distance, member, extraFare);

        //then
        assertThat(fare.value()).isEqualTo(predictedFare);
    }

    private static Stream<Arguments> matchedFare() {
        return Stream.of(
            Arguments.of(9, 1250, new LoginMember(1L, "email", 20), 0),
            Arguments.of(12, 1350, new LoginMember(1L, "email", 20), 0),
            Arguments.of(66, 2250, new LoginMember(1L, "email", 20), 0)
        );
    }

    @ParameterizedTest
    @DisplayName("나이별 요금 계산")
    @MethodSource("ageFare")
    void ageRate(int distance, int predictedFare, LoginMember member, int extraFare) {
        //when
        Fare fare = new Fare(distance, member, extraFare);

        //then
        assertThat(fare.value()).isEqualTo(predictedFare);
    }

    private static Stream<Arguments> ageFare() {
        return Stream.of(
            Arguments.of(9, 1250, new LoginMember(1L, "email", 20), 0),
            Arguments.of(9, 720, new LoginMember(1L, "email", 15), 0),
            Arguments.of(9, 450, new LoginMember(1L, "email", 8), 0),
            Arguments.of(9, 0, new LoginMember(1L, "email", 3), 0)
        );
    }

    @ParameterizedTest
    @DisplayName("노선별 추가 요금 계산")
    @MethodSource("extraFareByLine")
    void distanceRate(int distance, int predictedFare, LoginMember member, int extraFare) {
        //when
        Fare fare = new Fare(distance, member, extraFare);

        //then
        assertThat(fare.value()).isEqualTo(predictedFare);
    }

    private static Stream<Arguments> extraFareByLine() {
        return Stream.of(
            Arguments.of(9, 1350, new LoginMember(1L, "email", 20), 100),
            Arguments.of(9, 1450, new LoginMember(1L, "email", 20), 200),
            Arguments.of(9, 1550, new LoginMember(1L, "email", 20), 300)
        );
    }
}