package wooteco.subway.station;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.exception.DuplicatedException;
import wooteco.subway.exception.NotFoundException;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;

@ExtendWith(MockitoExtension.class)
public class StationServiceTest {

    @InjectMocks
    private StationService stationService;

    @Mock
    private StationDao stationDao;

    @Test
    @DisplayName("지하철역을 저장한다.")
    void saveStation() {
        final String name = "강남역";
        final StationRequest stationRequest = new StationRequest(name);
        final Station station = new Station(name);
        given(stationDao.existsByName(name)).willReturn(false);
        given(stationDao.insert(station)).willReturn(new Station(1L, name));

        assertThat(stationService.saveStation(stationRequest)).extracting("name").isEqualTo(name);
        verify(stationDao, times(1)).existsByName(name);
        verify(stationDao, times(1)).insert(station);
    }

    @Test
    @DisplayName("이미 존재하는 이름의 지하철역은 등록할 수 없다.")
    void saveStationFail() {
        final String name = "강남역";
        final StationRequest stationRequest = new StationRequest(name);
        given(stationDao.existsByName(name)).willReturn(true);

        assertThatThrownBy(() -> stationService.saveStation(stationRequest))
            .isInstanceOf(DuplicatedException.class)
            .hasMessage(String.format("이미 존재하는 지하철역 이름입니다. (입력한 값: %s)", name));
        verify(stationDao, times(1)).existsByName(name);
    }

    @Test
    @DisplayName("존재하지 않는 ID로 지하철역을 찾을 수 없다.")
    void findStationByIdFail() {
        given(stationDao.existsById(anyLong())).willReturn(false);
        assertThatThrownBy(() -> stationService.findStationById(anyLong()))
            .isInstanceOf(NotFoundException.class)
            .hasMessage("해당하는 지하철역이 없습니다.");
        verify(stationDao, times(1)).existsById(anyLong());
    }

    @Test
    @DisplayName("존재하지 않는 ID로 지하철역을 지울 수 없다.")
    void deleteStationByIdFail() {
        given(stationDao.existsById(anyLong())).willReturn(false);
        assertThatThrownBy(() -> stationService.deleteStationById(anyLong()))
            .isInstanceOf(NotFoundException.class)
            .hasMessage("해당하는 지하철역이 없습니다.");
        verify(stationDao, times(1)).existsById(anyLong());
    }
}
