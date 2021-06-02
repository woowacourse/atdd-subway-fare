package wooteco.subway.station.application;

import java.util.List;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.application.DuplicatedFieldException;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

@Service
public class StationService {

    private final StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        Station station;
        try {
            station = stationDao.insert(stationRequest.toStation());
        } catch (DuplicateKeyException e) {
            throw new DuplicatedFieldException("중복 필드가 있어 역 생성에 실패했습니다.");
        }
        return StationResponse.of(station);
    }

    public Station findStationById(Long id) {
        return stationDao.findById(id);
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAllStationResponses() {
        return StationResponse.listOf(
            stationDao.findAll()
        );
    }

    public void deleteStationById(Long id) {
        stationDao.deleteById(id);
    }
}
