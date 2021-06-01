package wooteco.subway.station.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.DuplicateException;
import wooteco.subway.exception.InvalidInputException;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {

    private final StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Transactional
    public StationResponse saveStation(StationRequest stationRequest) {
        validateDuplicateName(stationRequest.getName());
        Station station = stationDao.insert(stationRequest.toStation());
        return StationResponse.of(station);
    }

    private void validateDuplicateName(String name) {
        if (stationDao.existsByName(name)) {
            throw new DuplicateException("이미 존재하는 역 이름 입니다. (입력된 이름 값 : " + name + ")");
        }
    }

    @Transactional(readOnly = true)
    public Station findStationById(Long id) {
        return stationDao.findById(id)
                .orElseThrow(() -> new InvalidInputException("해당하는 id의 역이 없습니다."));
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();

        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStationById(Long id) {
        findStationById(id);
        validateDeletable(id);
        stationDao.deleteById(id);
    }

    private void validateDeletable(Long id) {
        if (stationDao.countRegisteredById(id) != 0) {
            throw new InvalidInputException("노선에 등록된 역은 삭제할 수 없습니다.");
        }
    }
}
