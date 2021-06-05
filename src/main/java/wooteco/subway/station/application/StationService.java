package wooteco.subway.station.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.badrequest.DuplicateStationException;
import wooteco.subway.exception.badrequest.ExistStationInSectionException;
import wooteco.subway.exception.notfound.NotFoundStationException;
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
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public StationService(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        stationDao.findByName(stationRequest.getName())
                .ifPresent(station -> {
                    throw new DuplicateStationException();
                });

        Station station = stationDao.insert(stationRequest.toStation());
        return StationResponse.of(station);
    }

    public Station findStationById(Long id) {
        return stationDao.findById(id)
                .orElseThrow(NotFoundStationException::new);
    }

    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();

        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteStationById(Long id) {
        findStationById(id);

        if (sectionDao.countSectionByStation(id) > 0) {
            throw new ExistStationInSectionException();
        }

        stationDao.deleteById(id);
    }

    public StationResponse updateStationById(Long id, StationRequest stationRequest) {
        findStationById(id);

        stationDao.findByName(stationRequest.getName())
                .filter(station -> station.sameNameAs(stationRequest.getName()) && !station.sameAs(id))
                .ifPresent(station -> {
                    throw new DuplicateStationException();
                });

        Station newStation = new Station(id, stationRequest.getName());
        stationDao.update(id, newStation);

        return StationResponse.of(findStationById(id));
    }
}
