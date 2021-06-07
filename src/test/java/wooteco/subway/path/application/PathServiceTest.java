package wooteco.subway.path.application;

import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

@DisplayName("PathService 클래스")
class PathServiceTest {

    @Nested
    @DisplayName("findPath 메서드는")
    class Describe_findPath {

        @Mock
        private LineService lineService;
        @Mock
        private StationService stationService;
        private Station station1;
        private Station station2;
        private Station station3;
        private Station station4;

        @BeforeEach
        void setUp() {
            stationSetUp();
            mockSetUp();
        }

        private void stationSetUp() {
            station1 = new Station(1L, "노원역");
            station2 = new Station(2L, "창동역");
            station3 = new Station(3L, "쌍문역");
            station4 = new Station(4L, "수유역");
        }

        private void mockSetUp() {
            MockitoAnnotations.initMocks(this);
            when(lineService.findLines()).thenReturn(Arrays.asList(line()));
            when(stationService.findStationById(1L)).thenReturn(station1);
            when(stationService.findStationById(4L)).thenReturn(station4);
        }

        private Line line() {
            Section section1 = new Section(1L, station1, station2, 25);
            Section section2 = new Section(2L, station2, station3, 25);
            Section section3 = new Section(3L, station3, station4, 30);
            List<Section> sections = Arrays.asList(section1, section2, section3);

            return new Line(1L, "4호선", "blue", 1000, new Sections(sections));
        }

        @Nested
        @DisplayName("출발역과 도착역 아이디와 성인 승객이 주어지면")
        class Context_with_departure_and_arrival_station_adult_user {

            @Test
            @DisplayName("경로 정보와 요금 정보를 가지고 있는 pathResponse DTO를 반환한다.")
            void it_returns_path_response_dto() {
                PathService pathService = new PathService(lineService, stationService,
                    new PathFinder());
                LoginMember loginMember = new LoginMember(1L, "bbwwpark@naver.com", 30);
                PathResponse pathResponse = pathService.findPath(1L, 4L, loginMember);

                Assertions.assertEquals(pathResponse.getDistance(), 80);
                Assertions.assertEquals(pathResponse.getFare(), 3450);
                Assertions.assertEquals(pathResponse.getStations(),
                    Arrays.asList(
                        StationResponse.of(station1),
                        StationResponse.of(station2),
                        StationResponse.of(station3),
                        StationResponse.of(station4)
                    )
                );
            }
        }

        @Nested
        @DisplayName("출발역과 도착역 아이디와 청소년 승객이 주어지면")
        class Context_with_departure_and_arrival_station_teenager_user {

            @Test
            @DisplayName("경로 정보와 요금 정보를 가지고 있는 pathResponse DTO를 반환한다.")
            void it_returns_path_response_dto() {
                PathService pathService = new PathService(lineService, stationService,
                    new PathFinder());
                LoginMember loginMember = new LoginMember(1L, "bbwwpark@naver.com", 16);
                PathResponse pathResponse = pathService.findPath(1L, 4L, loginMember);

                Assertions.assertEquals(pathResponse.getDistance(), 80);
                Assertions.assertEquals(pathResponse.getFare(), 2480);
                Assertions.assertEquals(pathResponse.getStations(),
                    Arrays.asList(
                        StationResponse.of(station1),
                        StationResponse.of(station2),
                        StationResponse.of(station3),
                        StationResponse.of(station4)
                    )
                );
            }
        }

        @Nested
        @DisplayName("출발역과 도착역 아이디와 어린이 승객이 주어지면")
        class Context_with_departure_and_arrival_station_children_user {

            @Test
            @DisplayName("경로 정보와 요금 정보를 가지고 있는 pathResponse DTO를 반환한다.")
            void it_returns_path_response_dto() {
                PathService pathService = new PathService(lineService, stationService,
                    new PathFinder());
                LoginMember loginMember = new LoginMember(1L, "bbwwpark@naver.com", 7);
                PathResponse pathResponse = pathService.findPath(1L, 4L, loginMember);

                Assertions.assertEquals(pathResponse.getDistance(), 80);
                Assertions.assertEquals(pathResponse.getFare(), 1550);
                Assertions.assertEquals(pathResponse.getStations(),
                    Arrays.asList(
                        StationResponse.of(station1),
                        StationResponse.of(station2),
                        StationResponse.of(station3),
                        StationResponse.of(station4)
                    )
                );
            }
        }
    }

}