package wooteco.subway.station.application;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import wooteco.subway.line.application.LineException;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

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
            throw new StationException("중복된 역 이름을 등록할 수 없습니다.");
        }
    }

    public Station findStationById(Long id) {
        return stationDao.findById(id).orElseThrow(()->new StationException("존재하지 않는 역입니다."));
    }

    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();

        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteStationById(Long id) {
        stationDao.findById(id)
                .orElseThrow(()->new StationException("존재하지 않는 역입니다."));

        stationDao.deleteById(id);
    }
}
