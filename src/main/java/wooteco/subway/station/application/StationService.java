package wooteco.subway.station.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.InvalidStationException;
import wooteco.subway.line.dto.StationTransferResponse;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;
import wooteco.subway.exception.AlreadyExistingStationException;

@Service
public class StationService {

    private final StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Transactional
    public StationResponse saveStation(StationRequest stationRequest) {
        Station station = stationDao.insert(stationRequest.toStation());
        return StationResponse.of(station);
    }

    public Station findStationById(Long id) {
        checkStationExist(id);
        return stationDao.findById(id);
    }

    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();

        return stations.stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStationById(Long id) {
        checkStationExist(id);
        stationDao.deleteById(id);
    }

    @Transactional
    public void updateStationById(Long id, String name) {
        checkStationExist(id);
        if (isExistingName(name)) {
            throw new AlreadyExistingStationException();
        }
        stationDao.updateById(id, name);
    }

    private boolean isExistingName(String name) {
        return stationDao.findStationByName(name);
    }

    private void checkStationExist(Long id) {
        if(!stationDao.findExistingStationById(id)) {
            throw new InvalidStationException();
        }
    }

    public List<StationTransferResponse> findStationsWithTransferLine(Long lineId) {
        return stationDao.getStationsWithTransferLines(lineId);
    }
}
