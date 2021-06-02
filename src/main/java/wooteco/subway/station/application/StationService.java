package wooteco.subway.station.application;

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

@Service
public class StationService {
    private StationDao stationDao;
    private LineDao lineDao;

    public StationService(StationDao stationDao, LineDao lineDao) {
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        if (stationDao.isExistByName(stationRequest.getName())) {
            throw new DuplicateException("이미 존재하는 역입니다.");
        }
        return StationResponse.of(stationDao.insert(stationRequest.toStation()));
    }

    public Station findStationById(Long id) {
        if (stationDao.isNotExistById(id)) {
            throw new NoSuchElementException("존재하지 않는 역입니다.");
        }
        return stationDao.findById(id);
    }

    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();
        return StationResponse.listOf(stations);
    }

    public void deleteStationById(Long id) {
        List<Line> lines = lineDao.findAll();

        if (isStationRegisteredInLine(id, lines)) {
            throw new UnsupportedOperationException("노선에 등록된 역은 삭제할 수 없습니다.");
        }

        if (stationDao.isNotExistById(id)) {
            throw new NoSuchElementException("존재하지 않는 역입니다.");
        }
        stationDao.deleteById(id);
    }

    private boolean isStationRegisteredInLine(Long id, List<Line> lines) {
        return lines.stream()
                .anyMatch(line -> line.hasStation(id));
    }

    public List<TransferResponse> findAllStationResponsesWithTransferable() {
        List<Line> lines = lineDao.findAll();
        List<Station> stations = stationDao.findAll();
        return TransferResponse.of(stations, lines);
    }
}
