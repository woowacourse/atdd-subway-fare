package wooteco.subway.path.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.member.domain.NonLoginMember;
import wooteco.subway.path.domain.SectionEdge;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @Mock
    private LineService lineService;
    @Mock
    private StationService stationService;
    @Mock
    private PathFinder pathFinder;

    @InjectMocks
    private PathService pathService;

    @DisplayName("경로 조회")
    @Test
    void findPath() {
        // given
        Station station1 = new Station(1L, "1역");
        Station station2 = new Station(2L, "2역");
        Section section = new Section(station1, station2, 2);
        Line line = new Line(1L, "1호선", "red", 0, new Sections(Arrays.asList(section)));
        List<Line> lines = Arrays.asList(line);
        SubwayPath subwayPath = new SubwayPath(
                Arrays.asList(new SectionEdge(section, line)),
                Arrays.asList(station1, station2));

        given(lineService.findLines()).willReturn(lines);
        given(stationService.findStationById(1L)).willReturn(station1);
        given(stationService.findStationById(2L)).willReturn(station2);
        given(pathFinder.findPath(lines, station1, station2)).willReturn(subwayPath);

        // when
        PathResponse path = pathService.findPath(1L, 2L, new NonLoginMember());

        // then
        PathResponse expectedPathResponse = new PathResponse(
                Arrays.asList(
                        new StationResponse(1L, "1역"),
                        new StationResponse(2L, "2역")),
                2, 1250);
        assertThat(path).usingRecursiveComparison().isEqualTo(expectedPathResponse);
    }
}