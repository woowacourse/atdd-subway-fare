package wooteco.subway.path.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.path.domain.SectionEdge;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.path.domain.fare.AdultFarePrincipal;
import wooteco.subway.path.domain.fare.KidFarePrincipal;
import wooteco.subway.path.domain.fare.TeenagerFarePrincipal;
import wooteco.subway.path.domain.fare.ToddlerFarePrincipal;
import wooteco.subway.station.domain.Station;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PathFinderTest {
    private Station 잠실역;
    private Station 강남역;
    private Station 석촌역;
    private Station 교대역;
    private Station 수서역;
    private Line 이호선;
    private Line 삼호선;
    private Section 잠실역_강남역;
    private Section 강남역_석촌역;
    private Section 석촌역_교대역;
    private Section 강남역_수서역;
    private Section 수서역_교대역;
    private List<Line> lines;

    @InjectMocks
    private PathFinder pathFinder;

    @BeforeEach
    @Test
    void setUp() {
        잠실역 = new Station("잠실역");
        강남역 = new Station("강남역");
        석촌역 = new Station("석촌역");
        교대역 = new Station("교대역");
        수서역 = new Station("수서역");
        이호선 = new Line("2호선", "bg-red-600");
        삼호선 = new Line("3호선", "bg-blue-600", 800L);
        잠실역_강남역 = new Section(잠실역, 강남역, 10);
        강남역_석촌역 = new Section(강남역, 석촌역, 10);
        석촌역_교대역 = new Section(석촌역, 교대역, 10);
        강남역_수서역 = new Section(강남역, 수서역, 5);
        수서역_교대역 = new Section(수서역, 교대역, 3);
        이호선.addSection(잠실역_강남역);
        이호선.addSection(강남역_석촌역);
        이호선.addSection(석촌역_교대역);
        삼호선.addSection(강남역_수서역);
        삼호선.addSection(수서역_교대역);
        lines = Arrays.asList(이호선, 삼호선);
        pathFinder = new PathFinder();
    }

    @DisplayName("경로 등록을 확인한다.")
    @Test
    void findPath() {
        SubwayPath path = pathFinder.findPath(lines, 잠실역, 석촌역);

        assertThat(path.getSectionEdges().get(0).getSection())
                .usingRecursiveComparison()
                .isEqualTo(잠실역_강남역);
        assertThat(path.getSectionEdges().get(1).getSection())
                .usingRecursiveComparison()
                .isEqualTo(강남역_석촌역);
    }

    @DisplayName("환승 여부를 확인한다.")
    @Test
    void findPathByTransfer() {
        SubwayPath path = pathFinder.findPath(lines, 잠실역, 수서역);

        assertThat(path.getSectionEdges().get(0).getSection())
                .usingRecursiveComparison()
                .isEqualTo(잠실역_강남역);
        assertThat(path.getSectionEdges().get(1).getSection())
                .usingRecursiveComparison()
                .isEqualTo(강남역_수서역);
    }
}
