package wooteco.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.member.domain.Age;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.station.domain.Station;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class FareTest {
    @DisplayName("요금이 0 미만 일 때 에러를 반환한다")
    @Test
    void fareBelowZero_throwException() {
        assertThatThrownBy(() -> new Fare(-1)).isInstanceOf(IllegalFareException.class);
    }

    @DisplayName("구간 정보와 거리 정보가 주어졌을 때 총 요금을 계산한다")
    @Test
    void calculateTotalFare() {
        List<SectionEdge> sectionEdges = Arrays.asList(
                new SectionEdge(
                        new Section(
                                new Station(1L, "삭정역"),
                                new Station(2L, "김고래역"),
                                new Distance(6)
                        ),
                        new Line(1L, "삭정선", "red", new Fare(600))
                ),
                new SectionEdge(
                        new Section(
                                new Station(2L, "김고래역"),
                                new Station(3L, "포비역"),
                                new Distance(5)
                        ),
                        new Line(2L, "김고래선", "yellow", new Fare(300))
                )
        );
        Fare totalFare = Fare.calculateTotalFare(sectionEdges, new Distance(11));
        assertThat(totalFare.getFare()).isEqualTo(1250 + 600 + 100);
    }

    @DisplayName("로그인 여부에 따라 할인 정책을 적용한 fare를 계산한다")
    @Test
    void calculateFareAfterDiscount() {
        Fare totalFare = new Fare(3200);
        LoginMember childrenLoginMember = new LoginMember(1L, "email@email.com", new Age(6));
        LoginMember teenagerLoginMember = new LoginMember(2L, "email2@email.com", new Age(13));
        LoginMember normalLoginMember = new LoginMember(3L, "email3@email.com", new Age(19));

        Fare childrenFare = totalFare.calculateFareAfterDiscount(childrenLoginMember);
        assertThat(childrenFare.getFare()).isEqualTo(3200 - (int) ((3200 - 350) * 0.5));

        Fare teenagerFare = totalFare.calculateFareAfterDiscount(teenagerLoginMember);
        assertThat(teenagerFare.getFare()).isEqualTo(3200 - (int) ((3200 - 350) * 0.2));

        Fare normalFare = totalFare.calculateFareAfterDiscount(normalLoginMember);
        assertThat(normalFare.getFare()).isEqualTo(3200);
    }
}