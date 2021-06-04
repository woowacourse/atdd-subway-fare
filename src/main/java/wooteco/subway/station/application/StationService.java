package wooteco.subway.station.application;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;
import wooteco.subway.station.exception.DuplicateStationNameException;
import wooteco.subway.station.exception.InvalidStationDeletionException;

@Service
public class StationService {
    private StationDao stationDao;
    private LineDao lineDao;

    public StationService(StationDao stationDao, LineDao lineDao) {
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        if (stationDao.existsByName(stationRequest.getName())) {
            throw new DuplicateStationNameException();
        }

        Station station = stationDao.insert(stationRequest.toStation());
        return StationResponse.of(station);
    }

    public void changeStation(Long id, StationRequest stationRequest) {
        if (stationDao.existsByName(stationRequest.getName())) {
            throw new DuplicateStationNameException();
        }

        Station station = stationDao.findById(id);
        stationDao.update(new Station(station.getId(), stationRequest.getName()));
    }

    public Station findStationById(Long id) {
        return stationDao.findById(id);
    }

    public List<Station> findAll() {
        return stationDao.findAll();
    }

    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();

        return stations.stream()
                .sorted(Comparator.comparing(Station::getId))
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteStationById(Long id) {
        Station deleteStation = stationDao.findById(id);
        for (Line line : lineDao.findAll()) {
            checkStationNotInLines(line, deleteStation);
        }

        stationDao.deleteById(id);
    }

    private void checkStationNotInLines(Line line, Station station) {
        if (line.isExistsStation(station)) {
            throw new InvalidStationDeletionException();
        }
    }
}
