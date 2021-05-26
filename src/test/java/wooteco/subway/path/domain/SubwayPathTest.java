package wooteco.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.domain.policy.FarePolicyFactory;
import wooteco.subway.station.domain.Station;

class SubwayPathTest {

    @DisplayName("요금 계산")
    @ParameterizedTest
    @CsvSource(value = {"10, 2150, 5", "11, 1300, 6", "15, 1870, 13", "16, 1950, 14", "50, 2430, 18", "58, 3050, 19"})
    void fare(int distance, int expectedFare, int age) {
        SectionEdge sectionEdge = new SectionEdge(
            new Section(1L, neozalStation(), rootStation(), distance),
            new Line(1L, "백기선", "red", 900)
        );

        LoginMember loginMember = new LoginMember(1L, "email@email.com", age);

        SubwayPath subwayPath = new SubwayPath(
            Collections.singletonList(sectionEdge),
            Arrays.asList(neozalStation(), rootStation()),
            new Fare(
                FarePolicyFactory.createExtraFarePolicy(),
                FarePolicyFactory.createDiscountPolicy()
            ));

        assertThat(subwayPath.fare(loginMember).intValue()).isEqualTo(expectedFare);
    }

    private Station neozalStation() {
        return new Station(1L, "너잘");
    }

    private Station rootStation() {
        return new Station(2L, "루트");
    }

}