package wooteco.subway.station.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.exception.ExistStationInSectionException;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;
import wooteco.subway.station.exception.DuplicateStationException;
import wooteco.subway.station.exception.NotFoundStationException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public StationService(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    @Transactional
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

    @Transactional
    public void deleteStationById(Long id) {
        findStationById(id);

        if (sectionDao.countSectionByStation(id) > 0) {
            throw new ExistStationInSectionException();
        }

        stationDao.deleteById(id);
    }

    @Transactional
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
