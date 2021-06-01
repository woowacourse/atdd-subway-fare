package wooteco.subway.station.application;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.NoSuchStationException;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;
import wooteco.subway.station.dto.StationServiceDto;

@Service
public class StationService {

    private static final int NOT_FOUND = 0;

    private final StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
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
        if (stationDao.delete(id) == NOT_FOUND) {
            throw new NoSuchStationException();
        }
    }
}

