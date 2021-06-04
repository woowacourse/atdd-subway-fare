package wooteco.subway.station.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.exception.DuplicatedException;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;
import wooteco.subway.station.exception.StationNotFoundException;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class StationServiceTest {

    private static final String NAME = "조앤역";

    @InjectMocks
    private StationService stationService;

    @Mock
    private StationDao stationDao;

    @Mock
    private SectionDao sectionDao;

    @Test
    @DisplayName("역을 저장한다.")
    void saveStation() {
        final StationRequest stationRequest = new StationRequest(NAME);
        given(stationDao.isExistByName(stationRequest.getName())).willReturn(false);
        given(stationDao.insert(ArgumentMatchers.any(Station.class))).willReturn(new Station(1L, NAME));

        final StationResponse stationResponse = stationService.saveStation(stationRequest);
        assertThat(stationResponse.getId()).isEqualTo(1L);
        assertThat(stationResponse.getName()).isEqualTo(NAME);
    }

    @Test
    @DisplayName("중복된 이름을 역을 저장 요청 시 중복 예외를 던진다.")
    void saveStationWithExistedName() {
        final StationRequest stationRequest = new StationRequest(NAME);
        given(stationDao.isExistByName(stationRequest.getName())).willReturn(true);

        assertThatThrownBy(() -> stationService.saveStation(stationRequest)).
                isInstanceOf(DuplicatedException.class);
    }

    @Test
    @DisplayName("Id로 역을 조회한다.")
    void findStationById() {
        given(stationDao.isNotExistById(1L)).willReturn(false);
        given(stationDao.findById(1L)).willReturn(new Station(1L, NAME));

        final Station station = stationService.findStationById(1L);
        assertThat(station.getId()).isEqualTo(1L);
        assertThat(station.getName()).isEqualTo(NAME);
    }

    @Test
    @DisplayName("존재하지 않는 Id로 역 조회 시 예외를 던진다.")
    void findStationByInvalidId() {
        given(stationDao.isNotExistById(100L)).willReturn(true);

        assertThatThrownBy(() -> stationService.findStationById(100L)).
                isInstanceOf(StationNotFoundException.class);
    }

    @Test
    @DisplayName("모든 역을 조회한다.")
    void findAllStationResponses() {
        given(stationDao.findAll()).willReturn(Arrays.asList(new Station(1L, NAME)));

        final List<StationResponse> stationResponses = stationService.findAllStationResponses();
        assertThat(stationResponses.size()).isEqualTo(1);
        assertThat(stationResponses.get(0).getId()).isEqualTo(1L);
        assertThat(stationResponses.get(0).getName()).isEqualTo(NAME);
    }
}