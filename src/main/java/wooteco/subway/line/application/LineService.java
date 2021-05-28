package wooteco.subway.line.application;

import org.springframework.stereotype.Service;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.SectionRequest;
import wooteco.subway.line.exception.DuplicateLineColorException;
import wooteco.subway.line.exception.DuplicateLineNameException;
import wooteco.subway.line.exception.LineNotFoundException;
import wooteco.subway.line.exception.SectionRemovalException;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        validateDuplicateLineName(request.getName());
        validateDuplicateLineColor(request.getColor());
        Line persistLine = lineDao.insert(new Line(request.getName(), request.getColor(), request.getExtraFare()));
        persistLine.addSection(addInitSection(persistLine, request));
        return LineResponse.of(persistLine);
    }

    private void validateDuplicateLineName(String lineName) {
        final Optional<Line> lineByName = lineDao.findByName(lineName);
        if (lineByName.isPresent()) {
            throw new DuplicateLineNameException();
        }
    }

    private void validateDuplicateLineColor(String lineColor) {
        final Optional<Line> lineByColor = lineDao.findByColor(lineColor);
        if (lineByColor.isPresent()) {
            throw new DuplicateLineColorException();
        }
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
                .orElseThrow(LineNotFoundException::new);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        final Line line = findLineById(id);
        if (!line.hasSameName(lineUpdateRequest.getName())) {
            validateDuplicateLineName(lineUpdateRequest.getName());
        }
        if (!line.hasSameColor(lineUpdateRequest.getColor())) {
            validateDuplicateLineColor(lineUpdateRequest.getColor());
        }

        lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor(), lineUpdateRequest.getExtraFare()));
    }

    public void deleteLineById(Long id) {
        final Line line = findLineById(id);
        lineDao.deleteById(line.getId());
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
        validateStationInLine(line, station);

        line.removeSection(station);

        sectionDao.deleteByLineId(lineId);
        sectionDao.insertSections(line);
    }

    private void validateStationInLine(Line line, Station station) {
        if (!line.containsStation(station)) {
            throw new SectionRemovalException();
        }
    }

}
