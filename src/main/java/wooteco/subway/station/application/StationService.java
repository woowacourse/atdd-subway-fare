package wooteco.subway.station.application;

import static wooteco.subway.exception.SubwayExceptions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.line.dao.SectionDao;
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

    @Transactional
    public StationResponse saveStation(StationRequest stationRequest) {
        try {
            Station station = stationDao.insert(stationRequest.toStation());
            return StationResponse.of(station);
        } catch (DataAccessException e) {
            throw DUPLICATED_STATION_NAME.makeException();
        }
    }

    @Transactional(readOnly = true)
    public Station findStationById(Long id) {
        try {
            return stationDao.findById(id);
        } catch (DataAccessException e) {
            throw NO_SUCH_STATION.makeException();
        }
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();

        return stations.stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStationById(Long id) {
        if (sectionDao.findSectionByStationId(id) != 0) {
            throw STATION_ALREADY_REGISTERED_IN_LINE.makeException();
        }
        if (stationDao.deleteById(id) == 0) {
            throw NO_SUCH_STATION.makeException();
        }
    }
}
