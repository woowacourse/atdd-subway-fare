package wooteco.subway.station.application;

import org.springframework.stereotype.Service;
import wooteco.subway.station.infrastructure.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.ui.dto.StationRequest;
import wooteco.subway.station.ui.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {
    private StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        Station station = stationDao.insert(stationRequest.toStation());
        return StationResponse.of(station);
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
        validateStationRequest(id, stationRequest);
        Station station = findStationById(id);
        Station newStation = new Station(station.getId(), stationRequest.getName());
        stationDao.updateStation(newStation);
    }

    private void validateStationRequest(Long id, StationRequest stationRequest) {
        if (stationDao.existsByNameExceptId(id, stationRequest.getName())) {
            throw new IllegalArgumentException("이미 존재하는 이름입니다.");
        }
    }
}
