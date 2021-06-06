package wooteco.subway.station.application;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import wooteco.subway.exception.SubwayException;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;
import wooteco.subway.station.exception.StationExceptionSet;

@Service
public class StationService {

    private final StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        try {
            Station station = stationDao.insert(stationRequest.toStation());
            return StationResponse.of(station);
        } catch (DuplicateKeyException exception) {
            throw new SubwayException(StationExceptionSet.DUPLICATE_STATION_EXCEPTION);
        }
    }

    public Station findStationById(Long id) {
        try {
            return stationDao.findById(id);
        } catch (EmptyResultDataAccessException exception) {
            throw new SubwayException(StationExceptionSet.NOT_EXIST_STATION_EXCEPTION);
        }
    }

    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();

        return stations.stream()
            .map(StationResponse::of)
            .sorted(Comparator.comparing(StationResponse::getId))
            .collect(Collectors.toList());
    }

    public void deleteStationById(Long id) {
        try {
            int updateRow = stationDao.deleteById(id);
            validateUpdate(updateRow);
        } catch (DataIntegrityViolationException exception) {
            throw new SubwayException(StationExceptionSet.DELETE_USE_STATION_EXCEPTION);
        }
    }

    public void updateStation(Long id, StationRequest stationRequest) {
        Station station = new Station(id, stationRequest.getName());
        try {
            int updateRow = stationDao.update(station);
            validateUpdate(updateRow);
        } catch (DuplicateKeyException exception) {
            throw new SubwayException(StationExceptionSet.DUPLICATE_STATION_EXCEPTION);
        }
    }

    private void validateUpdate(int updateRow) {
        if (updateRow != 1) {
            throw new SubwayException(StationExceptionSet.NOT_EXIST_STATION_EXCEPTION);
        }
    }
}
