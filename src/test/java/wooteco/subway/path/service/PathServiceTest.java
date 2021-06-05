package wooteco.subway.path.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.member.domain.AnonymousUser;
import wooteco.subway.member.domain.LoginUser;
import wooteco.subway.member.domain.User;
import wooteco.subway.path.application.PathFinder;
import wooteco.subway.path.application.PathService;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @InjectMocks
    private PathService pathService;

    @Mock
    private LineService lineService;

    @Mock
    private StationService stationService;

    @Mock
    private PathFinder pathFinder;

    private static Stream<Arguments> getUserAndExpectedValue() {
        return Stream.of(Arguments.of(new AnonymousUser(), 1550),
                Arguments.of(new LoginUser(1L, "dd", 13), 960));
    }

    /**
     * 잠실역   --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   송파
     */

    @DisplayName("경로 조회시 유저 분류에 따라 요금이 달라지며, 요금 등의 정보 또한 함께 확인할 수 있다.")
    @MethodSource("getUserAndExpectedValue")
    @ParameterizedTest
    void findPath(User user, int expectedFare) {
        long source = 2L;
        long target = 3L;

        Station 강남역 = new Station(1L, "강남역");
        Station 잠실역 = new Station(2L, "잠실역");
        Station 송파역 = new Station(3L, "송파역");
        Station 남부터미널역 = new Station(4L, "남부터미널역");

        Section section1 = new Section(1L, 잠실역, 강남역, 5);
        Section section2 = new Section(2L, 강남역, 송파역, 10);
        Section section3 = new Section(4L, 잠실역, 남부터미널역, 3);
        Section section4 = new Section(3L, 남부터미널역, 송파역, 5);

        Sections sections1 = new Sections(Arrays.asList(section1));
        Sections sections2 = new Sections(Arrays.asList(section2));
        Sections sections3 = new Sections(Arrays.asList(section3, section4));

        Line 이호선 = new Line(1L, "2호선", "black", 9999, sections1);
        Line 삼호선 = new Line(2L, "3호선", "white", 9999, sections2);
        Line 신분당선 = new Line(3L, "신분당선", "red", 300, sections3);
        List<Line> lines = Arrays.asList(이호선, 삼호선, 신분당선);
        SubwayPath path = new PathFinder().findPath(lines, 잠실역, 송파역);

        given(lineService.findLines()).willReturn(lines);
        given(stationService.findStationById(source)).willReturn(잠실역);
        given(stationService.findStationById(target)).willReturn(송파역);
        given(pathFinder.findPath(lines, 잠실역, 송파역)).willReturn(path);

        PathResponse pathResponse = pathService.findPath(source, target, user);

        assertThat(pathResponse.getDistance()).isEqualTo(8);
        assertThat(pathResponse.getFare()).isEqualTo(expectedFare);

        verify(lineService, times(1)).findLines();
        verify(stationService, times(2)).findStationById(any());
        verify(pathFinder, times(1)).findPath(lines, 잠실역, 송파역);
    }
}
