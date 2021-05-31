package wooteco.subway.line.application;

import org.springframework.stereotype.Service;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.SectionRequest;
import wooteco.subway.line.exception.LineColorDuplicationException;
import wooteco.subway.line.exception.LineNameDuplicationException;
import wooteco.subway.line.exception.LineNotExistException;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LineService {
    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationService stationService;

    public LineService(LineDao lineDao, SectionDao sectionDao, StationService stationService) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationService = stationService;
    }

    public LineResponse saveLine(LineRequest request) {
        List<Line> lines = lineDao.findAll();
        findOptionalLineByName(request, lines).ifPresent(exception -> {
            throw new LineNameDuplicationException();
        });
        findOptionalLineByColor(request, lines).ifPresent(exception -> {
            throw new LineColorDuplicationException();
        });

        Line persistLine = lineDao.insert(new Line(request.getName(), request.getColor(), request.getExtraFare()));
        persistLine.addSection(addInitSection(persistLine, request));
        return LineResponse.of(persistLine);
    }

    private Optional<Line> findOptionalLineByName(LineRequest request, List<Line> lines) {
        return lines.stream()
                .filter(line -> line.isSameName(request.getName()))
                .findAny();
    }

    private Optional<Line> findOptionalLineByColor(LineRequest request, List<Line> lines) {
        return lines.stream()
                .filter(line -> line.isSameColor(request.getColor()))
                .findAny();
    }

    private Section addInitSection(Line line, LineRequest request) {
        if (request.getUpStationId() != null && request.getDownStationId() != null) {
            Station upStation = stationService.findStationById(request.getUpStationId());
            Station downStation = stationService.findStationById(request.getDownStationId());
            Section section = new Section(upStation, downStation, request.getDistance());
            return sectionDao.insert(line, section);
        }
        return null;
    }

    public List<LineResponse> findLineResponses() {
        List<Line> persistLines = findLines();
        return persistLines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public List<Line> findLines() {
        return lineDao.findAll();
    }

    public LineResponse findLineResponseById(Long id) {
        Line persistLine = findLineById(id);
        return LineResponse.of(persistLine);
    }

    public Line findLineById(Long id) {
        return lineDao.findById(id)
                .orElseThrow(LineNotExistException::new);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        validateIfUpdatable(id, lineUpdateRequest, lineDao.findAll());
        lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    private void validateIfUpdatable(Long id, LineRequest lineUpdateRequest, List<Line> lines) {
        Optional<Line> findLineByName = findOptionalLineByName(lineUpdateRequest, lines);
        findLineByName.filter(line -> !line.isSameId(id))
                .ifPresent(exception -> {
                    throw new LineNameDuplicationException();
                });

        Optional<Line> findLineByColor = findOptionalLineByColor(lineUpdateRequest, lines);
        findLineByColor.filter(line -> !line.isSameId(id))
                .ifPresent(exception -> {
                    throw new LineColorDuplicationException();
                });
    }

    public void deleteLineById(Long id) {
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
}
