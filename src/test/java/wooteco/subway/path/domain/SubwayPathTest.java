package wooteco.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.exception.application.ValidationFailureException;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.path.domain.fare.Fare;
import wooteco.subway.path.domain.fare.creationstrategy.DistanceBasedCreationStrategy;
import wooteco.subway.station.domain.Station;

class SubwayPathTest {

    private Station 서초, 교대, 강남, 고터;
    private Section 서초_교대, 교대_강남, 교대_고터, 고터_강남;
    private Line 이호선, 삼호선;
    private List<SectionEdge> sectionEdges;
    private List<Station> stations;

    @BeforeEach
    void setUp() {
        initializeSubwayData();
        sectionEdges = Arrays.asList(
            new SectionEdge(서초_교대, 이호선), new SectionEdge(교대_고터, 삼호선), new SectionEdge(고터_강남, 삼호선)
        );
        stations = Arrays.asList(서초, 교대, 고터, 강남);
    }

    @DisplayName("경로의 구간의 수가 역의 수보다 하나 작지 않으면 생성에 실패한다.")
    @Test
    void create_fail_invalidSize() {
        // given
        List<Station> stations = Arrays.asList(서초, 교대, 고터);

        // when, then
        assertThatThrownBy(() -> new SubwayPath(sectionEdges, stations))
            .isInstanceOf(ValidationFailureException.class)
            .hasMessageContaining("경로의 구간의 수는 역의 수보다 하나 작아야 합니다");
    }

    @DisplayName("요금을 계산한다.")
    @Test
    void calculateFare() {
        // given
        SubwayPath subwayPath = new SubwayPath(sectionEdges, stations);

        // when
        Fare fare = subwayPath.calculateFare(new DistanceBasedCreationStrategy());

        // then
        assertThat(fare).isEqualTo(new Fare(1550));
    }

    @DisplayName("경로의 거리를 계산한다.")
    @Test
    void calculateDistance() {
        // given
        SubwayPath subwayPath = new SubwayPath(sectionEdges, stations);

        // when, then
        assertThat(subwayPath.calculateDistance())
            .isEqualTo(7);
    }

    private void initializeSubwayData() {
        서초 = new Station(1L, "서초역");
        교대 = new Station(2L, "교대역");
        강남 = new Station(3L, "강남역");
        고터 = new Station(4L, "고속터미널역");

        이호선_생성();
        삼호선_생성();
    }

    private void 이호선_생성() {
        서초_교대 = new Section(1L, 서초, 교대, 5);
        교대_강남 = new Section(2L, 교대, 강남, 3);

        이호선 = new Line(
            1L, "2호선", "green lighten-1", 200, new Sections(Arrays.asList(서초_교대, 교대_강남))
        );
    }

    private void 삼호선_생성() {
        교대_고터 = new Section(3L, 교대, 고터, 1);
        고터_강남 = new Section(4L, 고터, 강남, 1);

        삼호선 = new Line(
            2L, "3호선", "orange lighten-1", 300, new Sections(Arrays.asList(교대_고터, 고터_강남))
        );
    }
}
