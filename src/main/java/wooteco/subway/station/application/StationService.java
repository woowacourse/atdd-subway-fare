package wooteco.subway.station.application;

import org.springframework.stereotype.Service;
import wooteco.subway.exception.deletion.StationCannotDeleteException;
import wooteco.subway.exception.duplication.StationNameDuplicatedException;
import wooteco.subway.exception.notfound.StationNotFoundException;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;

@Service
public class StationService {

    private final StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        validateDuplicatedName(stationRequest.getName());
        Station station = stationDao.insert(stationRequest.toStation());
        return StationResponse.of(station);
    }

    private void validateDuplicatedName(String name) {
        if (stationDao.findByName(name).isPresent()) {
            throw new StationNameDuplicatedException();
        }
    }

    public Station findStationById(Long id) {
        return stationDao.findById(id)
                .orElseThrow(StationNotFoundException::new);
    }

    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();
        return StationResponse.listOf(stations);
    }

    public void deleteStationById(Long id) {
        findStationById(id);
        validateDeletableStatus(id);
        stationDao.deleteById(id);
    }

    private void validateDeletableStatus(Long id) {
        if (stationDao.countRegisteredStations(id) > 0) {
            throw new StationCannotDeleteException();
        }
    }

    public void updateStationById(Long id, StationRequest stationRequest) {
        findStationById(id);
        validateDuplicatedName(stationRequest.getName());
        stationDao.update(stationRequest.getName(), id);
    }
}
