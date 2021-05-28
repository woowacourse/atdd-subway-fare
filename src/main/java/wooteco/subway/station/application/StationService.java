package wooteco.subway.station.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.CannotRemoveStationException;
import wooteco.subway.exception.DuplicatedStationNameException;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Lines;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.SimpleLineResponse;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

@Service
@Transactional
public class StationService {

    private static final int ZERO_COUNT = 0;
    private StationDao stationDao;
    private SectionDao sectionDao;
    private LineDao lineDao;

    public StationService(StationDao stationDao, SectionDao sectionDao,
        LineDao lineDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        String name = stationRequest.getName();
        validateDuplicate(name);

        Station station = stationDao.insert(stationRequest.toStation());
        return StationResponse.of(station);
    }

    public Station findStationById(Long id) {
        return stationDao.findById(id);
    }

    public List<StationResponse> findAllStations() {
        List<Station> stations = stationDao.findAll();
        Lines lines = new Lines(lineDao.findAll());

        return stations.stream()
            .map(station -> StationResponse.of(
                station,
                SimpleLineResponse.listOf(lines.getLineContainStation(station))
            ))
            .collect(Collectors.toList());
    }

    public void deleteStationById(Long id) {
        if (sectionDao.countSectionByStationId(id) > ZERO_COUNT) {
            throw new CannotRemoveStationException();
        }
        stationDao.deleteById(id);
    }

    public StationResponse updateStation(Long id, StationRequest stationRequest) {
        String name = stationRequest.getName();
        validateDuplicate(name);

        Station station = new Station(id, name);
        stationDao.update(station);
        return StationResponse.of(station);
    }

    private void validateDuplicate(String name) {
        if (stationDao.findByName(name).isPresent()) {
            throw new DuplicatedStationNameException();
        }
    }
}
