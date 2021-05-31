package wooteco.subway.station.application;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import wooteco.subway.exception.DuplicateException;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;
import wooteco.subway.station.dto.TransferResponse;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class StationService {
    private StationDao stationDao;
    private LineDao lineDao;

    public StationService(StationDao stationDao, LineDao lineDao) {
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        try {
            return StationResponse.of(stationDao.insert(stationRequest.toStation()));
        } catch (Exception e) {
            throw new DuplicateException("이미 존재하는 역입니다.");
        }
    }

    public Station findStationById(Long id) {
        try {
            return stationDao.findById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchElementException("존재하지 않는 역입니다.");
        }
    }

    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();

        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteStationById(Long id) {
        int affectedRowNumber = stationDao.deleteById(id);
        if (affectedRowNumber == 0) {
            throw new NoSuchElementException("존재하지 않는 역입니다.");
        }
    }

    public List<TransferResponse> findAllStationResponsesWithTransferable() {
        List<Line> lines = lineDao.findAll();
        List<Station> stations = stationDao.findAll();
        return TransferResponse.of(stations, lines);
    }
}
