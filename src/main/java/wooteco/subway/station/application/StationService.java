package wooteco.subway.station.application;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import wooteco.subway.exception.SubwayCustomException;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;
import wooteco.subway.station.exception.StationException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {
    private StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        try {
            Station station = stationDao.insert(stationRequest.toStation());
            return StationResponse.of(station);
        } catch (DuplicateKeyException e) {
            throw new SubwayCustomException(StationException.DUPLICATED_STATION_NAME_EXCEPTION);
        }
    }

    public Station findStationById(Long id) {
        try {
            return stationDao.findById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new SubwayCustomException(StationException.NOT_FOUND_STATION_EXCEPTION);
        }
    }

    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();

        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteStationById(Long id) {
        int updateRow = stationDao.deleteById(id);

        validateUpdateRow(updateRow);
    }

    public void updateStationById(Long id, StationRequest stationRequest) {
        Station station = stationRequest.toStation();
        try {
            int updateRow = stationDao.updateById(id, station);
            validateUpdateRow(updateRow);
        } catch (DuplicateKeyException e) {
            throw new SubwayCustomException(StationException.DUPLICATED_STATION_NAME_EXCEPTION);
        } catch (EmptyResultDataAccessException e) {
            throw new SubwayCustomException(StationException.NOT_FOUND_STATION_EXCEPTION);
        }
    }

    private void validateUpdateRow(int updateRow) {
        if(updateRow != 1) {
            throw new SubwayCustomException(StationException.NOT_FOUND_STATION_EXCEPTION);
        }
    }
}
