package wooteco.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class SubwayPathTest {
    private static Station 강남역;
    private static Station 판교역;
    private static Station 역삼역;
    private static Station 잠실역;

    private static Line 신분당선;
    private static Line 이호선;

    private static Section 강남에서판교;
    private static Section 강남에서역삼;
    private static Section 역삼에서잠실;

    @DisplayName("거리별 + 추가요금노선 별 요금을 계산한다")
    @ParameterizedTest
    @MethodSource("calculateFareWithDistanceAndExtraFare")
    void calculateFareWithDistanceAndExtraFare(List<SectionEdge> sectionEdges, List<Station> stations, int expectedFare) {
        //given
        SubwayPath subwayPath = new SubwayPath(sectionEdges, stations);

        //when
        int fare = subwayPath.calculateFare();

        //then
        assertThat(fare).isEqualTo(expectedFare);
    }

    private static Stream<Arguments> calculateFareWithDistanceAndExtraFare() {
        setStations();
        setLines();
        setSections();

        return Stream.of(
            Arguments.of(Arrays.asList(new SectionEdge(강남에서역삼, 이호선)),
                Arrays.asList(강남역, 역삼역), 1250),
            Arguments.of(Arrays.asList(new SectionEdge(강남에서역삼, 이호선), new SectionEdge(역삼에서잠실, 이호선)),
                Arrays.asList(강남역, 역삼역, 잠실역), 1350),
            Arguments.of(Arrays.asList(new SectionEdge(강남에서역삼, 이호선), new SectionEdge(강남에서판교, 신분당선)),
                Arrays.asList(강남역, 역삼역, 판교역), 2050)
        );
    }

    private static void setStations() {
        강남역 = new Station(1L, "강남역");
        판교역 = new Station(2L, "판교역");
        역삼역 = new Station(4L, "역삼역");
        잠실역 = new Station(5L, "잠실역");
    }

    private static void setLines() {
        int extraFare = 800;
        int zeroExtraFare = 0;

        신분당선 = new Line(1L, "신분당선", "red lighten-1", extraFare);
        이호선 = new Line(2L, "2호선", "green lighten-1", zeroExtraFare);
    }

    private static void setSections() {
        강남에서판교 = new Section(강남역, 판교역, 5);
        신분당선.addSection(강남에서판교);

        강남에서역삼 = new Section(강남역, 역삼역, 5);
        역삼에서잠실 = new Section(역삼역, 잠실역, 10);
        이호선.addSection(강남에서역삼);
        이호선.addSection(역삼에서잠실);
    }
}
