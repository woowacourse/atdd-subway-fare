package wooteco.subway.station.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.station.DuplicateStationException;
import wooteco.subway.exception.station.InvalidDeletionException;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class StationService {
    private final StationDao stationDao;
    private final LineDao lineDao;

    public StationService(StationDao stationDao, LineDao lineDao) {
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        existsStation(stationRequest);
        Station station = stationDao.insert(stationRequest.toStation());
        return StationResponse.of(station);
    }

    @Transactional(readOnly = true)
    public Station findStationById(Long id) {
        return stationDao.findById(id);
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();
        return stations.stream()
                .map(StationResponse::of)
                .collect(toList());
    }

    public StationResponse updateStationById(Long id, StationRequest stationRequest) {
        existsStation(stationRequest);
        stationDao.update(new Station(id, stationRequest.getName()));
        return StationResponse.of(stationDao.findById(id));
    }

    private void existsStation(StationRequest stationRequest) {
        if (stationDao.exists(stationRequest.getName())) {
            throw new DuplicateStationException();
        }
    }

    public void deleteStationById(Long id) {
        checkStationInLine(id);
        stationDao.deleteById(id);
    }

    private void checkStationInLine(Long id) {
        List<Line> lines = lineDao.findAll();
        lines.stream()
                .filter(isStationInLine(id))
                .findAny()
                .ifPresent(invalidDeletionException());
    }

    private Predicate<Line> isStationInLine(Long id) {
        return line -> line.getId().equals(id);
    }

    private Consumer<Line> invalidDeletionException() {
        return line -> {
            throw new InvalidDeletionException();
        };
    }
}
