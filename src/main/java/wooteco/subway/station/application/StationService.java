package wooteco.subway.station.application;

import org.springframework.stereotype.Service;
import wooteco.subway.exception.deletion.StationCannotDeleteException;
import wooteco.subway.exception.duplication.StationDuplicatedException;
import wooteco.subway.exception.notfound.StationNotFoundException;
import wooteco.subway.line.application.LineService;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {
    private StationDao stationDao;
    private LineService lineService;


    public StationService(StationDao stationDao, LineService lineService) {
        this.stationDao = stationDao;
        this.lineService = lineService;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        validateDuplicatedName(stationRequest);
        Station station = stationDao.insert(stationRequest.toStation());
        return StationResponse.of(station);
    }

    public Station findStationById(Long id) {
        return stationDao.findById(id)
                .orElseThrow(StationNotFoundException::new);
    }

    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();

        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteStationById(Long id) {
        findStationById(id);
        if (lineService.countsStationsById(id) > 0) {
            throw new StationCannotDeleteException();
        }
        stationDao.deleteById(id);
    }

    public void updateStationById(Long id, StationRequest stationRequest) {
        findStationById(id);
        validateDuplicatedName(stationRequest);
        stationDao.updateById(stationRequest.getName(), id);
    }

    private void validateDuplicatedName(StationRequest stationRequest) {
        stationDao.findByName(stationRequest.getName())
                .orElseThrow(StationDuplicatedException::new);
    }
}
