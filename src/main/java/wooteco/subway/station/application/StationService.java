package wooteco.subway.station.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.exception.StationException;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.domain.Line;
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
        return StationResponse.of(station, lineDao.findAll());
    }

    @Transactional(readOnly = true)
    public Station findStationById(Long id) {
        return stationDao.findById(id);
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();
        final List<Line> lines = lineDao.findAll();

        return stations.stream()
                .map(station -> StationResponse.of(station, lines))
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
        if (!stationDao.isExistStation(id)) {
            throw new StationException("존재하지 않는 역입니다.");
        }

        boolean isPresent = lineDao.findAll()
                                   .stream()
                                   .anyMatch(it -> it.isIncludingStation(id));
        if (isPresent) {
            throw new StationException("이미 등록되어있는 역입니다.");
        }
    }
}
