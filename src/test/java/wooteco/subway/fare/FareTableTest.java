package wooteco.subway.fare;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.domain.User;
import wooteco.subway.path.domain.fare.Fare;
import wooteco.subway.path.domain.fare.FareTable;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class FareTableTest {

    private User 유저;

    @BeforeEach
    void setUp() {
        유저 = new Member("email", "password", 20);
    }

    @Test
    @DisplayName("10키로 이하 구간 - 유저의 나이에 맞는 요금을 반환한다. (게스트의 경우 성인요금을 반환)")
    void testCreateFareTable() {
        int distance = 10;
        int extraFare = 0;
        Fare fare = Fare.of(distance, extraFare);

        FareTable fareTable = FareTable.of(fare, 유저);
        Map<String, Integer> tableMap = fareTable.getFareTable();

        assertThat(fareTable.getDefaultFare()).isEqualTo(1250);
        assertThat(tableMap.get("성인")).isEqualTo(1250);
        assertThat(tableMap.get("청소년")).isEqualTo(720);
        assertThat(tableMap.get("어린이")).isEqualTo(450);
        assertThat(tableMap.get("유아")).isEqualTo(0);
    }

    @Test
    @DisplayName("10 초과 50이하 구간 - 유저의 나이에 맞는 요금을 반환한다. (게스트의 경우 성인요금을 반환)")
    void testCalculateFare() {
        int distance = 25; // 10 + 15
        int extraFare = 0;
        Fare fare = Fare.of(distance, extraFare);

        FareTable fareTable = FareTable.of(fare, 유저);
        Map<String, Integer> tableMap = fareTable.getFareTable();

        assertThat(fareTable.getDefaultFare()).isEqualTo(1250 + 300 + extraFare);
        assertThat(tableMap.get("성인")).isEqualTo(1250 + 300 + extraFare);
        assertThat(tableMap.get("청소년")).isEqualTo(720 + 240 + extraFare);
        assertThat(tableMap.get("어린이")).isEqualTo(450 + 150 + extraFare);
        assertThat(tableMap.get("유아")).isEqualTo(0);
    }

    @Test
    @DisplayName("50km 이상 구간 - 유저의 나이에 맞는 요금을 반환한다. (게스트의 경우 성인요금을 반환)")
    void testCalculateFareWhenDistanceIsOver50() {
        int distance = 58; // 10 + 40 + 8
        int extraFare = 0;
        Fare fare = Fare.of(distance, extraFare);

        FareTable fareTable = FareTable.of(fare, 유저);
        Map<String, Integer> tableMap = fareTable.getFareTable();

        assertThat(fareTable.getDefaultFare()).isEqualTo(1250 + 800 + 100 + extraFare);
        assertThat(tableMap.get("성인")).isEqualTo(1250 + 800 + 100 + extraFare);
        assertThat(tableMap.get("청소년")).isEqualTo(720 + 640 + 80 + extraFare);
        assertThat(tableMap.get("어린이")).isEqualTo(450 + 400 + 50 + extraFare);
        assertThat(tableMap.get("유아")).isEqualTo(0);
    }
}
