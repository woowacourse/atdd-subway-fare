package wooteco.subway.station.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.line.application.NoSuchLineException;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationLineResponse;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;
import wooteco.subway.station.dto.StationTransferResponse;

import java.util.List;

@Service
public class StationService {
    private final StationDao stationDao;
    private final LineDao lineDao;

    public StationService(final StationDao stationDao, final LineDao lineDao) {
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    @Transactional
    public StationResponse saveStation(StationRequest stationRequest) {
        Station station = stationDao.insert(stationRequest.toStation());
        return StationResponse.of(station);
    }

    public Station findStationById(Long id) {
        return stationDao.findById(id).orElseThrow(NoSuchStationException::new);
    }

    public List<StationLineResponse> findAllStationResponses() {
        return stationDao.findAll();
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

    public List<StationTransferResponse> getStationsWithTransferLines(final long lineId) {
        if (lineDao.doesIdNotExist(lineId)) {
            throw new NoSuchLineException();
        }
        return stationDao.getStationsWithTransferLines(lineId);
    }
}
