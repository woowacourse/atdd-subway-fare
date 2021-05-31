package wooteco.subway.station.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.domain.StationWithLines;
import wooteco.subway.station.dto.StationLineResponse;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;

@Service
public class StationService {
    private final StationDao stationDao;

    public StationService(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Transactional
    public StationResponse saveStation(StationRequest stationRequest) {
        Station station = stationDao.insert(stationRequest.toStation());
        return StationResponse.of(station);
    }

    public Station findStationById(Long id) {
        return stationDao.findById(id).orElseThrow(NoSuchStationException::new);
    }

    public List<StationLineResponse> findAllStationsWithLines() {
        List<StationWithLines> stationsWithLines = stationDao.findAllWithLines();
        return StationLineResponse.listOf(stationsWithLines);
    }

    @Transactional
    public void deleteStationById(Long id) {
        stationDao.deleteById(id);
    }

    @Transactional
    public void updateStationById(final Long id, final String name) {
        if (stationDao.doesNameExist(name)) {
            throw new DuplicateStationNameException();
        }
        stationDao.updateById(id, name);
    }
}
