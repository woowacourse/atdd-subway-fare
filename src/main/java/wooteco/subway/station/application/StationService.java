package wooteco.subway.station.application;

import org.springframework.stereotype.Service;
import wooteco.subway.exception.DuplicateNameException;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class StationService {
    private StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        Station station;
        try {
            station = stationDao.insert(stationRequest.toStation());
        } catch (Exception e) {
            throw new DuplicateNameException("이미 존재하는 역입니다.");
        }
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
        int affectedRowNumber = stationDao.deleteById(id);
        if (affectedRowNumber == 0) {
            throw new NoSuchElementException("존재하지 않는 역입니다.");
        }
    }
}
