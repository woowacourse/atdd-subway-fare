package wooteco.subway.path.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    private Station 서초, 교대, 강남, 고터;
    private Section 서초_교대, 교대_강남, 교대_고터, 고터_강남;
    private Line 이호선, 삼호선;

    @Mock
    private LineService lineService;

    @Mock
    private StationService stationService;

    private PathService pathService;

    @BeforeEach
    void setUp() {
        initializeSubwayData();
        pathService = new PathService(lineService, stationService, new PathFinder());
    }

    @DisplayName("하나의 호선을 지나가는 경로 계산")
    @MethodSource("findPath_singleLineValues")
    @ParameterizedTest
    void findPath_singleLine(Long source, Long target, LoginMember member, PathResponse expectedPathResponse) {
        // given
        given(lineService.findLines()).willReturn(Arrays.asList(이호선, 삼호선));
        given(stationService.findStationById(source)).willReturn(서초);
        given(stationService.findStationById(target)).willReturn(교대);

        // when
        PathResponse pathResponse = pathService.findPath(source, target, member);

        // then
        then(lineService).should(times(1)).findLines();
        then(stationService).should(times(2)).findStationById(anyLong());

        assertThat(pathResponse).usingRecursiveComparison()
            .isEqualTo(expectedPathResponse);
    }

    private static Stream<Arguments> findPath_singleLineValues() {
        List<StationResponse> stationResponses = Arrays.asList(
            new StationResponse(1L, "서초역"), new StationResponse(2L, "교대역")
        );

        return Stream.of(
            Arguments.of(
                1L, 2L, new LoginMember(1L, "e@e.com", 20),
                new PathResponse(stationResponses, 9, 1450)
            ),
            Arguments.of(
                1L, 2L, new LoginMember(1L, "e@e.com", 13),
                new PathResponse(stationResponses, 9, 880)
            ),
            Arguments.of(
                1L, 2L, new LoginMember(1L, "e@e.com", 12),
                new PathResponse(stationResponses, 9, 550)
            ),
            Arguments.of(
                1L, 2L, new LoginMember(1L, "e@e.com", 5),
                new PathResponse(stationResponses, 9, 0)
            )
        );
    }

    @DisplayName("환승하는 경로 계산")
    @MethodSource("findPath_transferLineValues")
    @ParameterizedTest
    void findPath_transferLine(Long source, Long target, LoginMember member, PathResponse expectedPathResponse) {
        // given
        given(lineService.findLines()).willReturn(Arrays.asList(이호선, 삼호선));
        given(stationService.findStationById(source)).willReturn(서초);
        given(stationService.findStationById(target)).willReturn(강남);

        // when
        PathResponse pathResponse = pathService.findPath(source, target, member);

        // then
        then(lineService).should(times(1)).findLines();
        then(stationService).should(times(2)).findStationById(anyLong());

        assertThat(pathResponse).usingRecursiveComparison()
            .isEqualTo(expectedPathResponse);
    }

    private static Stream<Arguments> findPath_transferLineValues() {
        List<StationResponse> stationResponses = Arrays.asList(
            new StationResponse(1L, "서초역"), new StationResponse(2L, "교대역"),
            new StationResponse(4L, "고속터미널역"), new StationResponse(3L, "강남역")
        );

        return Stream.of(
            Arguments.of(
                1L, 4L, new LoginMember(1L, "e@e.com", 20),
                new PathResponse(stationResponses, 11, 1650)
            ),
            Arguments.of(
                1L, 4L, new LoginMember(1L, "e@e.com", 13),
                new PathResponse(stationResponses, 11, 1040)
            ),
            Arguments.of(
                1L, 4L, new LoginMember(1L, "e@e.com", 12),
                new PathResponse(stationResponses, 11, 650)
            ),
            Arguments.of(
                1L, 4L, new LoginMember(1L, "e@e.com", 5),
                new PathResponse(stationResponses, 11, 0)
            )
        );
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
        서초_교대 = new Section(1L, 서초, 교대, 9);
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
