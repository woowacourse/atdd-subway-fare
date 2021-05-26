package wooteco.subway.station.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.station.DuplicateStationException;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class StationService {
    private final StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        existsStation(stationRequest);
        Station station = stationDao.insert(stationRequest.toStation());
        return StationResponse.of(station);
    }

    @Transactional(readOnly = true)
    public Station findStationById(Long id) {
        return stationDao.findById(id);
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();
        return stations.stream()
                .map(StationResponse::of)
                .collect(toList());
    }

    public StationResponse updateStationById(Long id, StationRequest stationRequest) {
        existsStation(stationRequest);
        stationDao.update(new Station(id, stationRequest.getName()));
        return StationResponse.of(stationDao.findById(id));
    }

    private void existsStation(StationRequest stationRequest) {
        if (stationDao.exists(stationRequest.getName())) {
            throw new DuplicateStationException();
        }
    }

    public void deleteStationById(Long id) {
        stationDao.deleteById(id);
    }
}
