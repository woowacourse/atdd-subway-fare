package wooteco.subway.station;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class StationDaoTest {

    @Autowired
    private StationDao stationDao;

    @DisplayName("전체 역 반환 테스트")
    @Test
    public void checkAllStation() {
        // given
        Station station1 = new Station("상봉역");
        stationDao.insert(station1);
        Station station2 = new Station("구로디지털단지역");
        stationDao.insert(station2);

        // when
        List<Station> stations = stationDao.findAll();

        // then
        assertThat(stations.get(0).getName()).isEqualTo(station1.getName());
        assertThat(stations.get(1).getName()).isEqualTo(station2.getName());
    }

    @DisplayName("저장 테스트")
    @Test
    public void checkSave() {
        // given
        Station station = new Station("상봉역");

        // when
        Station insertedStation = stationDao.insert(station);

        // then
        assertThat(insertedStation.getName()).isEqualTo("상봉역");
    }

    @DisplayName("삭제 테스트")
    @Test
    public void delete() {
        // given
        Station station = new Station("상봉역");
        Station insertedStation = stationDao.insert(station);

        // when
        int deleteCount = stationDao.delete(insertedStation.getId());

        // then
        assertThat(deleteCount).isEqualTo(1);
    }

    @DisplayName("삭제 실패 테스트")
    @Test
    public void deleteVoidStation() {
        // given

        // when
        int deleteCount = stationDao.delete(999L);

        // then
        assertThat(deleteCount).isEqualTo(0);
    }
}