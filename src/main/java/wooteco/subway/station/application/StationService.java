package wooteco.subway.station.application;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import wooteco.subway.exception.NotFoundException;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationNameRequest;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

import java.util.Comparator;
import java.util.List;

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
            throw new IllegalArgumentException("이미 존재하는 지하철 역입니다");
        }
    }

    public Station findStationById(Long id) {
        try{
            return stationDao.findById(id);
        } catch (Exception e) {
            throw new NotFoundException("존재하지 않는 지하철 역입니다");
        }
    }

    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();
        stations.stream()
                .sorted(Comparator.comparing(Station::getId));
        return StationResponse.listOf(stations);
    }

    public void deleteStationById(Long id) {
        if (stationDao.isOnSection(id)) {
            throw new IllegalArgumentException("이미 노선에 등록된 지하철 역입니다");
        }
        stationDao.deleteById(id);
    }

    public StationResponse updateName(Long id, StationNameRequest req) {
        if (stationDao.findByName(req.getName()) != null) {
            throw new IllegalArgumentException("이미 존재하는 지하철 역입니다");
        }
        Station station = stationDao.updateName(id, req.getName());
        return StationResponse.of(station);
    }
}
