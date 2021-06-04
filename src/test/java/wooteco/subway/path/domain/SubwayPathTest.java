package wooteco.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

class SubwayPathTest {

    private static final Line FIRST_LINE = new Line(1L, "1호선", "파란색", 0);
    private static final Line SECOND_LINE = new Line(2L, "2호선", "초록색", 100);
    private static final Line THIRD_LINE = new Line(3L, "3호선", "셋째역", 100);

    private static final Station FIRST_STATION = new Station(1L, "첫역");
    private static final Station SECOND_STATION = new Station(2L, "둘째역");
    private static final Station THIRD_STATION = new Station(3L, "셋째역");
    private static final Station FOURTH_STATION = new Station(4L, "넷째역");

    @ParameterizedTest
    @DisplayName("경로 위 노선 최대 추가 요금 계산")
    @MethodSource("matchedMaxFare")
    void calculatePath(List<SectionEdge> sectionEdges, int maxFare) {
        //when
        List<Station> stations = new ArrayList<>();
        SubwayPath subwayPath = new SubwayPath(sectionEdges, stations);

        //then
        int calculatedMaximumFare = subwayPath.maximumExtraFare();

        //then
        assertThat(calculatedMaximumFare).isEqualTo(maxFare);
    }

    private static Stream<Arguments> matchedMaxFare() {
        return Stream.of(
            Arguments.of(Arrays.asList(
                new SectionEdge(new Section(1L, FIRST_STATION, SECOND_STATION, 10), FIRST_LINE),
                new SectionEdge(new Section(2L, SECOND_STATION, THIRD_STATION, 10), SECOND_LINE),
                new SectionEdge(new Section(3L, THIRD_STATION, FOURTH_STATION, 10), THIRD_LINE)
            ), 100),
            Arguments.of(Arrays.asList(
                new SectionEdge(new Section(1L, FIRST_STATION, SECOND_STATION, 10), FIRST_LINE),
                new SectionEdge(new Section(2L, SECOND_STATION, THIRD_STATION, 10), FIRST_LINE),
                new SectionEdge(new Section(3L, THIRD_STATION, FOURTH_STATION, 10), FIRST_LINE)
            ), 0)
        );
    }

    @ParameterizedTest
    @DisplayName("경로 위 거리 계산")
    @MethodSource("sumDistance")
    void calculateDistance(List<SectionEdge> sectionEdges, int sumDistance) {
        //given
        List<Station> stations = new ArrayList<>();
        SubwayPath subwayPath = new SubwayPath(sectionEdges, stations);

        //when
        int calculatedDistance = subwayPath.calculateDistance();

        //then
        assertThat(calculatedDistance).isEqualTo(sumDistance);
    }


    private static Stream<Arguments> sumDistance() {
        return Stream.of(
            Arguments.of(Arrays.asList(
                new SectionEdge(new Section(1L, FIRST_STATION, SECOND_STATION, 10), FIRST_LINE),
                new SectionEdge(new Section(2L, SECOND_STATION, THIRD_STATION, 20), SECOND_LINE),
                new SectionEdge(new Section(3L, THIRD_STATION, FOURTH_STATION, 30), THIRD_LINE)
            ), 60),
            Arguments.of(Arrays.asList(
                new SectionEdge(new Section(1L, FIRST_STATION, SECOND_STATION, 10), FIRST_LINE),
                new SectionEdge(new Section(2L, SECOND_STATION, THIRD_STATION, 10), FIRST_LINE),
                new SectionEdge(new Section(3L, THIRD_STATION, FOURTH_STATION, 10), FIRST_LINE)
            ), 30)
        );
    }

}