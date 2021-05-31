package wooteco.subway.station.application;

import org.springframework.stereotype.Service;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;
import wooteco.subway.station.dto.StationWithTransferResponse;
import wooteco.subway.station.exception.DuplicateStationNameException;
import wooteco.subway.station.exception.StationInLineException;
import wooteco.subway.station.exception.StationNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StationService {
    private final StationDao stationDao;
    private final LineDao lineDao;

    public StationService(StationDao stationDao, LineDao lineDao) {
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        validatePossibleStationName(stationRequest.getName());
        Station station = stationDao.insert(stationRequest.toStation());
        return StationResponse.of(station);
    }

    private void validatePossibleStationName(String stationName) {
        final Optional<Station> stationByName = stationDao.findByName(stationName);
        if (stationByName.isPresent()) {
            throw new DuplicateStationNameException();
        }
    }

    public Station findStationById(Long id) {
        final Optional<Station> station = stationDao.findById(id);
        return station.orElseThrow(StationNotFoundException::new);
    }

    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();
        return StationResponse.listOf(stations);
    }

    public List<StationWithTransferResponse> findAllStationWithTransferInfoResponses() {
        List<Station> stations = stationDao.findAll();
        final Map<Station, List<Line>> transferInfos = stations.stream()
                .collect(Collectors.toMap(
                        station -> station,
                        station -> lineDao.findIncludingStation(station.getId())
                ));
        return StationWithTransferResponse.listOf(transferInfos);
    }

    public void deleteStationById(Long id) {
        final Station station = findStationById(id);
        validateNotInAnyLines(station);
        stationDao.deleteById(station.getId());
    }

    private void validateNotInAnyLines(Station station) {
        List<Line> lines = lineDao.findIncludingStation(station.getId());
        if (!lines.isEmpty()) {
            throw new StationInLineException();
        }
    }
}
