package wooteco.subway.station.application;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationNameRequest;
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
            throw new IllegalArgumentException("입력하신 역이 이미 존재합니다.");
        }
    }

    public Station findStationById(Long id) {
        return stationDao.findById(id);
    }

    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();

        return StationResponse.listOf(stations);
    }

    public void deleteStationById(Long id) {
        stationDao.deleteById(id);
    }

    public StationResponse updateName(Long id, StationNameRequest req) {
        if (stationDao.findByName(req.getName()) != null) {
            throw new IllegalArgumentException("중복된 이름의 역이 존재합니다.");
        }
        Station station = stationDao.updateName(id, req.getName());
        return StationResponse.of(station);
    }
}
