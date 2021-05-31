package wooteco.subway.station.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import wooteco.subway.exception.DuplicatedException;
import wooteco.subway.exception.InvalidInputException;
import wooteco.subway.exception.NotFoundException;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;
import wooteco.subway.station.dto.StationTransferResponse;

@Service
public class StationService {

    private final StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        final String name = stationRequest.getName();
        if (stationDao.existsByName(name)) {
            throw new DuplicatedException(String.format("이미 존재하는 지하철역 이름입니다. (입력한 값: %s)", name));
        }
        final Station station = stationDao.insert(stationRequest.toStation());
        return StationResponse.of(station);
    }

    private void validateNotFoundStationId(Long id) {
        if (!stationDao.existsById(id)) {
            throw new NotFoundException("해당하는 지하철역이 없습니다.");
        }
    }

    public Station findStationById(Long id) {
        validateNotFoundStationId(id);
        return stationDao.findById(id);
    }

    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAllDescById();

        return stations.stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());
    }

    public List<StationTransferResponse> findAllStationWithTransfer() {
        return stationDao.findAllWithTransfer();
    }

    public void deleteStationById(Long id) {
        validateNotFoundStationId(id);
        try {
            stationDao.deleteById(id);
        } catch (DataAccessException e) {
            throw new InvalidInputException("노선에 등록된 지하철역은 지울 수 없습니다.");
        }
    }
}
