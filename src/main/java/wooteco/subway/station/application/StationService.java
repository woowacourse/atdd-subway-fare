package wooteco.subway.station.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Service
@Transactional
public class StationService {
    private StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
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
        stationDao.update(new Station(id, stationRequest.getName()));
        return StationResponse.of(stationDao.findById(id));
    }

    public void deleteStationById(Long id) {
        stationDao.deleteById(id);
    }
}
