package wooteco.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.member.domain.MemberType;

import static org.assertj.core.api.Assertions.assertThat;

class FarePolicyTest {

    @DisplayName("거리가 7일 때 기본 요금 반환")
    @Test
    public void calculateTest1() {
        int distance = 7;
        int expectedFare = 1250;
        assertFare(distance, 0, MemberType.NONE, expectedFare);
    }

    @DisplayName("거리가 17일 때 구간1 반환")
    @Test
    public void calculateTest2() {
        int distance = 17;
        int expectedFare = 1450;
        assertFare(distance, 0, MemberType.NONE, expectedFare);
    }

    @DisplayName("거리가 77일 때 기본 요금 반환")
    @Test
    public void calculateTest3() {
        int distance = 77;
        int expectedFare = 2450;
        assertFare(distance, 0, MemberType.NONE, expectedFare);
    }

    @DisplayName("추가 요금 구간 적용")
    @Test
    public void calculateTest4() {
        int distance = 7;
        int expectedFare = 1250 + 1000;
        assertFare(distance, 1000, MemberType.NONE, expectedFare);
    }


    @DisplayName("거리가 7일 때, 어린이 요금 적용 반환")
    @Test
    public void calculateTest5() {
        int distance = 7;
        int expectedFare = (int) (1250 * 0.5);
        assertFare(distance, 0, MemberType.CHILD, expectedFare);
    }

    @DisplayName("거리가 7일 때, 청소년 요금 적용 반환")
    @Test
    public void calculateTest6() {
        int distance = 7;
        int expectedFare = (int) (1250 * 0.8);
        assertFare(distance, 0, MemberType.ADOLESCENT, expectedFare);
    }

    private void assertFare(int distance, int extraFare, MemberType memberType, int expectedFare) {
        assertThat(FarePolicy.calculate(distance, extraFare, memberType)).isEqualTo(expectedFare);
    }
}