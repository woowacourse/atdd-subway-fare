package wooteco.subway.path.domain;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import wooteco.subway.member.domain.LoginMember;

public class FareTest {

    @DisplayName("6세 미만의 유아 요금을 계산한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 5})
    void BabyFareTest(int age) {
        LoginMember loginMember = new LoginMember(age);
        Fare fare = new Fare(1250);
        fare.calculateFareByAge(loginMember);
        assertThat(fare.getFare()).isEqualTo(0);
    }

    @DisplayName("6 ~ 13세의 어린이 요금을 계산한다.")
    @ParameterizedTest
    @ValueSource(ints = {6, 12})
    void ChildrenFareTest(int age) {
        LoginMember loginMember = new LoginMember(age);
        Fare fare = new Fare(1250);
        fare.calculateFareByAge(loginMember);
        assertThat(fare.getFare()).isEqualTo(450);
    }

    @DisplayName("13 ~ 18의 청소년 요금을 계산한다.")
    @ParameterizedTest
    @ValueSource(ints = {13, 18})
    void TeenagerFareTest(int age) {
        LoginMember loginMember = new LoginMember(age);
        Fare fare = new Fare(1250);
        fare.calculateFareByAge(loginMember);
        assertThat(fare.getFare()).isEqualTo(720);
    }

    @DisplayName("19세 이상의 일반 요금을 계산한다.")
    @ParameterizedTest
    @ValueSource(ints = {19, 100})
    void AdultFareTest(int age) {
        LoginMember loginMember = new LoginMember(age);
        Fare fare = new Fare(1250);
        fare.calculateFareByAge(loginMember);
        assertThat(fare.getFare()).isEqualTo(1250);
    }
}
