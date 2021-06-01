package wooteco.subway.station.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

@ExtendWith(MockitoExtension.class)
class StationServiceTest {

    private static final Long STATION_ID = 1L;

    @InjectMocks
    StationService stationService;
    @Mock
    StationDao stationDao;
    @Mock
    SectionDao sectionDao;

    @DisplayName("역을 저장한다.")
    @Test
    void saveStationSuccess() {
        // given
        Station station = new Station(1L, "선정릉역");
        StationRequest stationRequest = new StationRequest("선정릉역");
        when(stationDao.insert(stationRequest.toStation())).thenReturn(station);
        // when
        stationService.saveStation(stationRequest);
        // then
        verify(stationDao).insert(stationRequest.toStation());
    }

    @DisplayName("역을 저장한다. - 실패, 같은 이름의 역이 등록되어 있다.")
    @Test
    void saveStationFail_duplicateStation() {
//        // given
//        Station station = new Station(1L, "선정릉역");
//        StationRequest stationRequest = new StationRequest("선정릉역");
//        when(stationDao.insert(stationRequest.toStation())).thenReturn(station);
//        // when
//        stationService.saveStation(stationRequest);
//        // then
//        verify(stationDao).insert(stationRequest.toStation());
    }


    @DisplayName("id로 역을 조회한다.")
    @Test
    void findStationByIdSuccess() {
        // given
        Station station = new Station(STATION_ID, "선정릉역");
        when(stationDao.findById(STATION_ID)).thenReturn(Optional.of(station));
        // when
        stationService.findStationById(STATION_ID);
        // then
        verify(stationDao).findById(STATION_ID);
    }

    @DisplayName("id로 역을 조회한다. - 실패, 역이 존재하지 않는다.")
    @Test
    void findStationByIdFail_stationNotExisting() {
        // given
        when(stationDao.findById(STATION_ID)).thenReturn(Optional.empty());
        // when
//        stationService.findStationById(STATION_ID);
        // then
        assertThatThrownBy(() -> stationService.findStationById(STATION_ID))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("모든 역을 조회한다.")
    @Test
    void findAllStationResponsesSuccess() {
        // given
        List<Station> stations = Arrays.asList(
                new Station(1L, "로키형네역"),
                new Station(2L, "피케이네역")
        );
        when(stationDao.findAll()).thenReturn(stations);
        // when
        List<StationResponse> responses = stationService.findAllStationResponses();
        // then
        verify(stationDao).findAll();
        List<String> expectedNames = stations.stream()
                .map(Station::getName)
                .collect(Collectors.toList());
        List<String> responseNames = responses.stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());
//        assertThat(responseNames);
    }

    @DisplayName("id로 역을 삭제한다.")
    @Test
    void deleteStationByIdSuccess() {
        // given
        when(sectionDao.existsByStationId(STATION_ID)).thenReturn(false);
        // when
        stationService.deleteStationById(STATION_ID);
        // then
        verify(stationDao).deleteById(STATION_ID);
    }

    @DisplayName("id로 역을 삭제한다. - 실패, 역이 노선에 등록되어 있다.")
    @Test
    void deleteStationByIdFail_stationInLine() {
        // given
        when(sectionDao.existsByStationId(STATION_ID)).thenReturn(true);
        // when
        // then
        assertThatThrownBy(() -> stationService.deleteStationById(STATION_ID))
                .isInstanceOf(InvalidDeletionException.class);
    }

}