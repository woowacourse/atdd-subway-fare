package wooteco.subway.station.application;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;
import wooteco.subway.station.exception.DuplicateStationNameException;
import wooteco.subway.station.exception.RegisteredStationDeleteException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {
    private StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Transactional
    public StationResponse saveStation(StationRequest stationRequest) {
        validateDuplicateStationName(stationRequest.getName());

        try {
            Station station = stationDao.insert(stationRequest.toStation());
            return StationResponse.of(station);
        } catch (DuplicateKeyException e) {
            throw new DuplicateStationNameException();
        }
    }

    private void validateDuplicateStationName(String name) {
        stationDao.findByName(name).ifPresent(station -> {
            throw new DuplicateStationNameException();
        });
    }

    public Station findStationById(Long id) {
        return stationDao.findById(id);
    }

    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();

        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStationById(Long id) {
        try {
            stationDao.deleteById(id);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new RegisteredStationDeleteException("노선에 등록된 역은 삭제할 수 없습니다.");
        }
    }
}
