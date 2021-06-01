package wooteco.subway.station.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.DuplicateStationException;
import wooteco.subway.exception.InvalidStationException;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.dto.StationTransferResponse;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationLinesResponse;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

@Service
public class StationService {

    private final StationDao stationDao;
    private final LineDao lineDao;

    public StationService(StationDao stationDao, LineDao lineDao) {
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    @Transactional
    public StationResponse saveStation(StationRequest stationRequest) {
        if (isExistingName(stationRequest.getName())) {
            throw new DuplicateStationException();
        }
        Station station = stationDao.insert(stationRequest.toStation());
        return StationResponse.of(station);
    }

    @Transactional(readOnly = true)
    public Station findStationById(Long id) {
        checkStationExist(id);
        return stationDao.findById(id);
    }

    @Transactional(readOnly = true)
    public List<StationLinesResponse> findAll() {
        List<Station> stations = stationDao.findAll();
        List<Line> lines = lineDao.findAll();
        Map<List<Station>, Line> lineStations = lines.stream()
            .collect(Collectors.toMap(Line::getStations,
                line -> line));

        return stations.stream()
            .map(station -> StationLinesResponse.of(station, lineStations))
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
            throw new DuplicateStationException();
        }
        stationDao.updateById(id, name);
    }

    @Transactional(readOnly = true)
    public List<StationTransferResponse> findStationsWithTransferLine(Long lineId) {
        return stationDao.getStationsWithTransferLines(lineId);
    }

    private boolean isExistingName(String name) {
        return stationDao.findStationByName(name);
    }

    private void checkStationExist(Long id) {
        if (!stationDao.findExistingStationById(id)) {
            throw new InvalidStationException();
        }
    }
}
