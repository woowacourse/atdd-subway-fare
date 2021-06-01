package wooteco.subway.line.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.LineUpdateRequest;
import wooteco.subway.line.dto.LineWithTransferAndDistanceResponse;
import wooteco.subway.line.dto.LineWithoutSectionsResponse;
import wooteco.subway.line.dto.SectionRequest;
import wooteco.subway.line.exception.DuplicateLineColorException;
import wooteco.subway.line.exception.DuplicateLineNameException;
import wooteco.subway.line.exception.InvalidSectionException;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationWithTransferAndNextDistanceResponse;

@Service
public class LineService {
    private LineDao lineDao;
    private SectionDao sectionDao;
    private StationService stationService;

    public LineService(LineDao lineDao, SectionDao sectionDao, StationService stationService) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationService = stationService;
    }

    public LineResponse saveLine(LineRequest request) {
        checkAlreadyExistsName(request.getName());
        checkAlreadyExistsColor(request.getColor());

        Line persistLine = lineDao.insert(new Line(request.getName(), request.getColor(), request.getExtraFare()));
        persistLine.addSection(addInitSection(persistLine, request));
        return LineResponse.of(persistLine);
    }

    private Section addInitSection(Line line, LineRequest request) {
        if (request.getUpStationId() != null && request.getDownStationId() != null) {
            Station upStation = stationService.findStationById(request.getUpStationId());
            Station downStation = stationService.findStationById(request.getDownStationId());
            Section section = new Section(upStation, downStation, request.getDistance());
            return sectionDao.insert(line, section);
        }
        throw new InvalidSectionException("유효하지 않는 요청 값입니다");
    }

    public List<LineWithTransferAndDistanceResponse> findLineResponses() {
        List<Line> persistLines = findLines();

        return persistLines
            .stream()
            .map(line -> findLineResponseById(line.getId()))
            .collect(Collectors.toList());
    }

    public List<Line> findLines() {
        return lineDao.findAll();
    }

    public LineWithTransferAndDistanceResponse findLineResponseById(Long id) {
        List<Line> persistLines = findLines();
        Line persistLine = findLineById(id);
        List<Station> persistStations = persistLine.getStations();

        List<StationWithTransferAndNextDistanceResponse> stationsWithTransferAndNextDistance = persistStations
            .stream()
            .map(station -> new StationWithTransferAndNextDistanceResponse(
                    station.getId(),
                    station.getName(),
                    persistLine.getNextStationDistance(station),
                    findTransferLines(persistLines, station, persistLine))
            )
            .collect(Collectors.toList());

        return new LineWithTransferAndDistanceResponse(
            persistLine.getId(),
            persistLine.getName(),
            persistLine.getColor(),
            stationsWithTransferAndNextDistance
        );
    }

    private List<LineWithoutSectionsResponse> findTransferLines(List<Line> persistLines, Station station, Line persistLine) {
        return persistLines
            .stream()
            .filter(line -> line.contains(station) && !line.equals(persistLine))
            .map(line -> new LineWithoutSectionsResponse(line.getId(), line.getName(), line.getColor()))
            .collect(Collectors.toList());
    }

    public Line findLineById(Long id) {
        return lineDao.findById(id);
    }

    public void updateLine(Long id, LineUpdateRequest lineUpdateRequest) {
        Line line = lineDao.findById(id);
        if (!line.isSameName(lineUpdateRequest.getName())) {
            checkAlreadyExistsName(lineUpdateRequest.getName());
        }
        if (!line.isSameColor(lineUpdateRequest.getColor())) {
            checkAlreadyExistsColor(lineUpdateRequest.getColor());
        }

        lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        lineDao.findById(id);
        sectionDao.deleteByLineId(id);
        lineDao.deleteById(id);
    }

    public void addLineStation(Long lineId, SectionRequest request) {
        Line line = findLineById(lineId);
        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());
        line.addSection(upStation, downStation, request.getDistance());

        sectionDao.deleteByLineId(lineId);
        sectionDao.insertSections(line);
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        Station station = stationService.findStationById(stationId);
        line.removeSection(station);

        sectionDao.deleteByLineId(lineId);
        sectionDao.insertSections(line);
    }

    private void checkAlreadyExistsName(String updateName) {
        if (lineDao.existsByName(updateName)) {
            throw new DuplicateLineNameException();
        }
    }

    private void checkAlreadyExistsColor(String updateColor) {
        if (lineDao.existsByColor(updateColor)) {
            throw new DuplicateLineColorException();
        }
    }
}
