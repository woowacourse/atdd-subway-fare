package wooteco.subway.station.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.notfound.EntityExceptionStatus;
import wooteco.subway.exception.notfound.EntityNotFoundException;
import wooteco.subway.exception.value.InvalidValueException;
import wooteco.subway.exception.value.InvalidValueExceptionStatus;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class StationService {

    private final StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Transactional
    public StationResponse saveStation(StationRequest stationRequest) {
        validateNameDuplication(stationRequest.getName());
        Station station = stationDao.insert(stationRequest.toStation());
        return StationResponse.of(station);
    }

    private void validateNameDuplication(String name) {
        stationDao.findByName(name)
                .ifPresent(station -> {
                    throw new InvalidValueException(InvalidValueExceptionStatus.DUPLICATED_STATION_NAME);
                });
    }

    public Station findStationById(Long id) {
        return stationDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityExceptionStatus.STATION_NOT_FOUND));
    }

    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();
        return StationResponse.listOf(stations);
    }

    @Transactional
    public void updateStationById(Long id, StationRequest stationRequest) {
        findStationById(id);
        validateNameDuplication(stationRequest.getName());
        stationDao.update(stationRequest.getName(), id);
    }

    @Transactional
    public void deleteStationById(Long id) {
        findStationById(id);
        validateDeletableStatus(id);
        stationDao.deleteById(id);
    }

    private void validateDeletableStatus(Long id) {
        if (stationDao.calculateRegisteredCountsById(id) != 0) {
            throw new InvalidValueException(InvalidValueExceptionStatus.STATION_NOT_DELETABLE);
        }
    }
}
