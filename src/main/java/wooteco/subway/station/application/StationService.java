package wooteco.subway.station.application;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.springframework.stereotype.Service;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.infrastructure.dao.LineDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.infrastructure.dao.StationDao;
import wooteco.subway.station.ui.dto.LineResponse;
import wooteco.subway.station.ui.dto.StationRequest;
import wooteco.subway.station.ui.dto.StationResponse;
import wooteco.subway.station.ui.dto.StationWithLinesResponse;

@Service
public class StationService {

    private final StationDao stationDao;
    private final LineDao lineDao;

    public StationService(StationDao stationDao, LineDao lineDao) {
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        Station station = stationDao.insert(stationRequest.toStation());
        return StationResponse.of(station);
    }

    public Station findStationById(Long id) {
        return stationDao.findById(id);
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

    public void deleteStationById(Long id) {
        stationDao.deleteById(id);
    }

    public void updateStation(Long id, StationRequest stationRequest) {
        validateStationRequest(id, stationRequest);
        Station station = findStationById(id);
        Station newStation = new Station(station.getId(), stationRequest.getName());
        stationDao.updateStation(newStation);
    }

    private void validateStationRequest(Long id, StationRequest stationRequest) {
        if (stationDao.existsByNameExceptId(id, stationRequest.getName())) {
            throw new IllegalArgumentException("이미 존재하는 이름입니다.");
        }
    }
}
