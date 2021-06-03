package wooteco.subway.station.application;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.exception.NotAbleToDeleteStationInLineException;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;
import wooteco.subway.station.exception.StationNameDuplicationException;
import wooteco.subway.station.exception.StationNotExistException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {
    private StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        if (stationDao.findByName(stationRequest.getName()).isPresent()) {
            throw new StationNameDuplicationException();
        }
        Station station = stationDao.insert(stationRequest.toStation());
        return StationResponse.from(station);
    }

    public Station findStationById(Long id) {
        return stationDao.findById(id)
                .orElseThrow(StationNotExistException::new);
    }

    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();

        return stations.stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }

    public void deleteStationById(Long id) {
        findStationById(id);
        try {
            stationDao.deleteById(id);
        } catch (DataAccessException e) {
            throw new NotAbleToDeleteStationInLineException();
        }
    }
}
