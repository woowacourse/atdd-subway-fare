package wooteco.subway.station.application;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.StationAlreadyRegisteredInLineException;
import wooteco.subway.section.dao.SectionDao;
import wooteco.subway.section.domain.Sections;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

@Service
public class StationService {

    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public StationService(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public List<StationResponse> showStations() {
        List<Station> stations = stationDao.findAll();

        return stations.stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());
    }

    public Station findById(Long stationId) {
        return stationDao.findById(stationId);
    }

    @Transactional
    public StationResponse save(@Valid StationRequest stationRequest) {
        Station station = stationRequest.toStation();
        Station saveStation = stationDao.insert(station);
        return StationResponse.of(saveStation);
    }

    @Transactional
    public void delete(@NotNull Long id) {
        validateExistOnLines(id);
        stationDao.delete(id);
    }

    private void validateExistOnLines(Long id) {
        Station station = stationDao.findById(id);
        Sections sections = new Sections(sectionDao.findAll());
        if (sections.hasStation(station)) {
            throw new StationAlreadyRegisteredInLineException();
        }
    }
}

