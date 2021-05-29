package wooteco.subway.station.application;

import org.springframework.stereotype.Service;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;
import wooteco.subway.station.exception.DuplicateStationNameException;
import wooteco.subway.station.exception.StationInLineException;
import wooteco.subway.station.exception.StationNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StationService {
    private StationDao stationDao;
    private SectionDao sectionDao;

    public StationService(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        validateDuplicateStationName(stationRequest.getName());
        Station station = stationDao.insert(stationRequest.toStation());
        return StationResponse.of(station);
    }

    private void validateDuplicateStationName(String stationName) {
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

        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteStationById(Long id) {
        final Station station = findStationById(id);
        validateNotInAnyLines(station);
        stationDao.deleteById(station.getId());
    }

    private void validateNotInAnyLines(Station station) {
        Optional<List<Long>> lineIds = sectionDao.findLineIdsContains(station.getId());
        if (lineIds.isPresent()) {
            throw new StationInLineException();
        }
    }
}
