package wooteco.subway.station.application;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import wooteco.subway.exception.duplicated.DuplicatedStationException;
import wooteco.subway.exception.nosuch.NoSuchStationException;
import wooteco.subway.exception.exist.StationAlreadyInLineException;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;
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
        try {
            Station station = stationDao.insert(stationRequest.toStation());
            return StationResponse.of(station);
        } catch (DataAccessException e){
            throw new DuplicatedStationException();
        }
    }

    public Station findStationById(Long id) {
        try {
            return stationDao.findById(id);
        } catch (DataAccessException e) {
            throw new NoSuchStationException();
        }
    }

    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();

        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteStationById(Long id) {
        if(sectionDao.findSectionByStationId(id) != 0) {
            throw new StationAlreadyInLineException();
        }
        if(stationDao.deleteById(id) == 0) {
            throw new NoSuchStationException();
        }
    }
}
