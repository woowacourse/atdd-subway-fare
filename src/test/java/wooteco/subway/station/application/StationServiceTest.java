package wooteco.subway.station.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureJdbc;
import org.springframework.boot.test.context.SpringBootTest;
import wooteco.subway.ServiceTest;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@AutoConfigureJdbc
@SpringBootTest(classes = {StationService.class, StationDao.class, LineDao.class})
class StationServiceTest extends ServiceTest {
    @Autowired
    private StationService stationService;

    @Test
    @DisplayName("역 정상 생성")
    void saveStation() {
        String stationName = "name";
        StationResponse stationResponse = 역_생성_요청(stationName);

        assertThat(stationResponse).extracting("name")
                .isEqualTo(stationName);
    }

    @Test
    @DisplayName("역 중복 이름 생성시 에러")
    void saveStationDuplicatedName() {
        String stationName = "name";
        역_생성_요청(stationName);

        assertThatThrownBy(() -> 역_생성_요청(stationName)).isInstanceOf(StationException.class);
    }

    @Test
    @DisplayName("역 id 조회")
    void findStationById() {
        String stationName = "name";
        StationResponse response = 역_생성_요청(stationName);
        Long createdId = response.getId();

        Station expected = new Station(createdId, stationName);
        Station actual = 역_조회_요청(createdId);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("존재하지 않는 역 id 조회시 에러 발생")
    void findStationByNotExistId() {
        Long notExistedId = 100L;

        assertThatThrownBy(() -> 역_조회_요청(notExistedId)).isInstanceOf(StationException.class);
    }

    @Test
    @DisplayName("역 정상 삭제")
    void deleteStation() {
        String stationName = "name";
        StationResponse response = 역_생성_요청(stationName);
        Long createdId = response.getId();

        assertThat(역_조회_요청(createdId)).isNotNull();

        역_삭제_요청(createdId);
        assertThatThrownBy(() -> 역_조회_요청(createdId)).isInstanceOf(StationException.class);
    }

    @Test
    @DisplayName("존재하지 않는 역 삭제시 에러 발생")
    void deleteNotExistStation() {
        Long notExistedId = 100L;

        assertThatThrownBy(() -> 역_삭제_요청(notExistedId)).isInstanceOf(StationException.class);
    }

    @Test
    @DisplayName("역 정상 수정")
    void updateStation() {
        String name = "name";
        String updateName = "newName";
        StationResponse response = 역_생성_요청(name);
        Long createdId = response.getId();

        역_수정_요청(createdId, updateName);

        Station expected = new Station(createdId, updateName);
        Station actual = 역_조회_요청(createdId);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("존재하지 않는 역 수정시 에러 발생")
    void updateNotExistStation() {
        Long notExistedId = 100L;
        String newName = "newName";

        assertThatThrownBy(() -> 역_수정_요청(notExistedId, newName)).isInstanceOf(StationException.class);
    }

    private StationResponse 역_생성_요청(String name) {
        return stationService.saveStation(new StationRequest(name));
    }

    private Station 역_조회_요청(Long id) {
        return stationService.findStationById(id);
    }

    private void 역_삭제_요청(Long id) {
        stationService.deleteStationById(id);
    }

    private void 역_수정_요청(Long id, String updateName) {
        stationService.updateStationById(id, new StationRequest(updateName));
    }
}
