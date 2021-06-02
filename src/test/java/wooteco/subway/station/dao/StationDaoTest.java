package wooteco.subway.station.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import wooteco.subway.exception.notfound.StationNotFoundException;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationTransferLinesDto;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("StationDao 관련")
@DataJdbcTest
@ActiveProfiles("test")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class StationDaoTest {
    private Station station1;
    private Station station2;
    private StationDao stationDao;

    public StationDaoTest(DataSource dataSource) {
        stationDao = new StationDao(new JdbcTemplate(dataSource), dataSource);
    }

    @BeforeEach
    void setUp() {
        station1 = new Station("강남역");
        station2 = new Station("역삼역");
    }

    @DisplayName("지하철 역 저장한다.")
    @Test
    void insert() {
        //when
        Station insertStation = stationDao.insert(station1);

        //then
        assertThat(insertStation.getName()).isEqualTo(station1.getName());
    }

    @DisplayName("중복되는 이름으로 지하철 역 저장한다.")
    @Test
    void insertByDuplicateName() {
        //given
        stationDao.insert(station1);

        //when then
        assertThatThrownBy(() -> stationDao.insert(station1))
                .isInstanceOf(DuplicateKeyException.class);
    }

    @DisplayName("모든 지하철 역을 조회한다.")
    @Test
    void findAll() {
        //given
        List<Station> beforeInsertStations = stationDao.findAll();
        stationDao.insert(station1);
        stationDao.insert(station2);

        //when
        List<Station> afterInsertStations = stationDao.findAll();

        //then
        assertThat(beforeInsertStations).hasSize(0);
        assertThat(afterInsertStations).hasSize(2);
    }

    @DisplayName("지하철 역 ID로 지하철 역을 삭제한다.")
    @Test
    void deleteById() {
        //given
        Station insertStation = stationDao.insert(station1);
        Long savedStationId = insertStation.getId();

        //when
        stationDao.deleteById(savedStationId);
        Optional<Station> findStation = stationDao.findById(savedStationId);

        //then
        assertThat(findStation).isEqualTo(Optional.empty());
    }

    @DisplayName("존재하지 않는 지하철 역 ID로 지하철 역을 삭제하면 예외가 발생한다..")
    @Test
    void deleteByNotExistId() {
        //when then
        assertThatThrownBy(() -> stationDao.deleteById(Long.MAX_VALUE))
                .isInstanceOf(StationNotFoundException.class);
    }

    @DisplayName("지하철 역 ID로 지하철 역 조회한다.")
    @Test
    void findById() {
        //given
        Station insertStation = stationDao.insert(station1);
        Long savedStationId = insertStation.getId();

        //when
        Optional<Station> findOptionalStation = stationDao.findById(savedStationId);

        //then
        assertThat(findOptionalStation.isPresent()).isTrue();
        Station findStation = findOptionalStation.get();
        assertThat(findStation.getId()).isEqualTo(insertStation.getId());
        assertThat(findStation.getName()).isEqualTo(station1.getName());
    }

    @DisplayName("존재하지 않는 지하철 역 ID로 지하철 역 조회한다.")
    @Test
    void findByNotExistId() {
        //when
        Optional<Station> findOptionalStation = stationDao.findById(Long.MAX_VALUE);

        //then
        assertThat(findOptionalStation).isEqualTo(Optional.empty());
    }

    @DisplayName("지하철 역 정보 수정한다.")
    @Test
    void update() {
        //given
        Station insertStation = stationDao.insert(station1);
        Long savedStationId = insertStation.getId();

        //when
        stationDao.update(new Station(savedStationId, station2.getName()));
        Station updatedStation = stationDao.findById(savedStationId).get();

        //then
        assertThat(updatedStation.getId()).isEqualTo(savedStationId);
        assertThat(updatedStation.getName()).isEqualTo(station2.getName());
    }

    @DisplayName("이미 존재하는 지하철 역 이름으로 지하철 역 정보 수정한다.")
    @Test
    void updateByDuplicateName() {
        //given
        stationDao.insert(station1);
        Station insertStation = stationDao.insert(station2);
        Long savedStationId = insertStation.getId();

        //when then
        assertThatThrownBy(() -> stationDao.update(new Station(savedStationId, station1.getName())))
                .isInstanceOf(DuplicateKeyException.class);
    }

    @DisplayName("존재하지 않는 지하철 역 정보를 수정하면 예외가 발생한다.")
    @Test
    void updateByNotExistStation() {
        //when then
        assertThatThrownBy(() -> stationDao.update(new Station(Long.MAX_VALUE, station1.getName())))
                .isInstanceOf(StationNotFoundException.class);
    }

    @DisplayName("해당 역에서 환승가능한 모든 노선(Line) 정보들을 가진 역 목록을 조회한다.")
    @Test
    void findAllWithTransferLines() {
        //given
        Station insertStation1 = stationDao.insert(station1);
        Station insertStation2 = stationDao.insert(station2);

        //when
        List<StationTransferLinesDto> allWithTransferLines = stationDao.findAllWithTransferLines();

        //then
        assertThat(allWithTransferLines).hasSize(2);

        StationTransferLinesDto stationTransferLinesDto1 = allWithTransferLines.get(0);
        assertThat(stationTransferLinesDto1.getId()).isEqualTo(insertStation1.getId());
        assertThat(stationTransferLinesDto1.getName()).isEqualTo(insertStation1.getName());
        assertThat(stationTransferLinesDto1.getTransferLines()).hasSize(0);

        StationTransferLinesDto stationTransferLinesDto2 = allWithTransferLines.get(1);

        assertThat(stationTransferLinesDto2.getId()).isEqualTo(insertStation2.getId());
        assertThat(stationTransferLinesDto2.getName()).isEqualTo(insertStation2.getName());
        assertThat(stationTransferLinesDto2.getTransferLines()).hasSize(0);
    }
}