package wooteco.subway.station.application;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import wooteco.subway.exception.SubwayCustomException;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.station.exception.SubwayStationException;

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
            throw new SubwayCustomException(SubwayStationException.DUPLICATE_STATION_EXCEPTION);
        }
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

    public void deleteStationById(Long id) {
        stationDao.deleteById(id);
    }

    public void updateStation(Long id, StationRequest stationRequest) {
        Station station = stationRequest.toStation();
        try {
            stationDao.update(id, station);
        } catch (DuplicateKeyException exception) {
            throw new SubwayCustomException(SubwayStationException.DUPLICATE_STATION_EXCEPTION);
        } catch (Exception e) {
            System.out.println(e.getClass());
            throw e;
        }
    }
}
