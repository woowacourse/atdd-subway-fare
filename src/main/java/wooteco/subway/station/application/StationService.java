package wooteco.subway.station.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;
import wooteco.subway.station.exception.DuplicatedStationNameException;
import wooteco.subway.station.exception.NoSuchStationException;

@Service
public class StationService {

    private StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        if (!stationDao.isHaveStationByName(stationRequest.getName())) {
            Station station = stationDao.insert(stationRequest.toStation());
            return StationResponse.of(station);
        }
        throw new DuplicatedStationNameException("중복된 역 이름입니다.");
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

    public void isHaveStationById(Long id) {
        if (!stationDao.isHaveStationById(id)) {
            throw new NoSuchStationException("존재하지 않는 역은 삭제 할 수 없습니다.");
        }
    }
}
