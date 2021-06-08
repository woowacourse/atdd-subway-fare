package wooteco.subway.station.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;
import wooteco.subway.station.exception.DuplicatedStationException;
import wooteco.subway.station.exception.InvalidDeletionException;
import wooteco.subway.station.exception.NotFoundException;

@MockitoSettings
class StationServiceTest {

    private static final Long STATION_ID = 1L;

    @InjectMocks
    private StationService stationService;
    @Mock
    private StationDao stationDao;
    @Mock
    private SectionDao sectionDao;

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
        // given
        StationRequest stationRequest = new StationRequest("선정릉역");

        when(stationDao.exists(stationRequest.getName())).thenReturn(true);

        // when // then
        assertThatThrownBy(() -> stationService.saveStation(stationRequest))
                .isExactlyInstanceOf(DuplicatedStationException.class);
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

        // when // then
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
        assertThat(responses).extracting("name")
                .containsExactlyElementsOf(Arrays.asList("로키형네역", "피케이네역"));
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

        // when // then
        assertThatThrownBy(() -> stationService.deleteStationById(STATION_ID))
                .isInstanceOf(InvalidDeletionException.class);
    }

}