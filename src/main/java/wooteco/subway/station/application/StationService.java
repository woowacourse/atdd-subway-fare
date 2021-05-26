package wooteco.subway.station.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.exception.StationException;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class StationService {
    private StationDao stationDao;
    private LineDao lineDao;

    public StationService(StationDao stationDao, LineDao lineDao) {
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        Station station = stationDao.insert(stationRequest.toStation());
        return StationResponse.of(station);
    }

    @Transactional(readOnly = true)
    public Station findStationById(Long id) {
        return stationDao.findById(id);
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();

        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public void updateStationById(Long id, StationRequest stationRequest) {
        stationDao.updateById(id, stationRequest.getName());
    }

    public void deleteStationById(Long id) {
        validate(id);
        stationDao.deleteById(id);
    }

    private void validate(Long id) {
        boolean isPresent = lineDao.findAll()
                                   .stream()
                                   .anyMatch(it -> it.containsStation(id));
        if (isPresent) {
            throw new StationException("이미 등록되어있는 역입니다.");
        }
    }
}
