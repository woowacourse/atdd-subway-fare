package wooteco.subway.station.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StationService {
    private StationDao stationDao;
    private final SectionDao sectionDao;

    public StationService(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    @Transactional
    public StationResponse saveStation(StationRequest stationRequest) {
        Station station = stationDao.insert(stationRequest.toStation());
        return StationResponse.of(station);
    }

    public Station findStationById(Long id) {
        if (stationDao.notExistId(id)) {
            throw new NotFoundStationException();
        }
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
        if (stationDao.notExistId(id)) {
            throw new NotFoundStationException();
        }

        if (sectionDao.existSectionByStationId(id)) {
            throw new IncludedStationException();
        }

        stationDao.deleteById(id);
    }
}
