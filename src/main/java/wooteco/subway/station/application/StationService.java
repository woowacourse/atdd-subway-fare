package wooteco.subway.station.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.DuplicateException;
import wooteco.subway.exception.NotFoundException;
import wooteco.subway.exception.SubwayException;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.infrastructure.dao.LineDao;
import wooteco.subway.line.infrastructure.dao.SectionDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.infrastructure.dao.StationDao;
import wooteco.subway.station.ui.dto.LineResponse;
import wooteco.subway.station.ui.dto.StationRequest;
import wooteco.subway.station.ui.dto.StationResponse;
import wooteco.subway.station.ui.dto.StationWithLinesResponse;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Transactional(readOnly = true)
@Service
public class StationService {

    private final StationDao stationDao;
    private final SectionDao sectionDao;
    private final LineDao lineDao;

    public StationService(StationDao stationDao, SectionDao sectionDao, LineDao lineDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
    }

    @Transactional
    public StationResponse saveStation(StationRequest stationRequest) {
        Station station = stationDao.insert(stationRequest.toStation());
        return StationResponse.of(station);
    }

    public Station findStationById(Long id) {
        validateThatStationExists(id);
        return stationDao.findById(id);
    }

    private void validateThatStationExists(Long id) {
        if (!stationDao.existsById(id)) {
            throw new NotFoundException("존재하지 않는 역입니다.");
        }
    }

    public List<StationWithLinesResponse> findAllStationResponses() {
        List<Station> allStations = stationDao.findAll();
        List<Line> allLines = lineDao.findAll();

        Map<Station, List<Line>> stationsWithLines = allStations.stream()
            .collect(toMap(Function.identity(), station ->
                allLines.stream()
                    .filter(line -> line.getStations().contains(station))
                    .collect(toList())
            ));

        return stationsWithLines.keySet().stream()
            .map(station -> new StationWithLinesResponse(station.getId(), station.getName(),
                convertLinesToLineResponses(stationsWithLines.get(station))))
            .collect(toList());
    }

    private List<LineResponse> convertLinesToLineResponses(List<Line> lines) {
        return lines.stream()
            .map(line -> new LineResponse(line.getId(), line.getName(), line.getColor()))
            .collect(toList());
    }

    @Transactional
    public void deleteStationById(Long id) {
        validateThatStationExists(id);
        validateDeletableStation(id);
        stationDao.deleteById(id);
    }

    private void validateDeletableStation(Long id) {
        if(sectionDao.existsByStationId(id)) {
            throw new SubwayException("노선에 등록된 역은 삭제할 수 없습니다.");
        }
    }

    @Transactional
    public void updateStation(Long id, StationRequest stationRequest) {
        validateStationRequest(id, stationRequest);
        Station station = findStationById(id);
        Station newStation = new Station(station.getId(), stationRequest.getName());

        stationDao.updateStation(newStation);
    }

    private void validateStationRequest(Long id, StationRequest stationRequest) {
        if (stationDao.existsByNameExceptId(id, stationRequest.getName())) {
            throw new DuplicateException("이미 존재하는 역 이름입니다.");
        }
    }

}
