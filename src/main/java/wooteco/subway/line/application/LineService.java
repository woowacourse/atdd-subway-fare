package wooteco.subway.line.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import org.springframework.stereotype.Service;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.infrastructure.dao.LineDao;
import wooteco.subway.line.infrastructure.dao.SectionDao;
import wooteco.subway.line.ui.dto.LineRequest;
import wooteco.subway.line.ui.dto.LineResponse;
import wooteco.subway.line.ui.dto.LineWithTransferLineResponse;
import wooteco.subway.line.ui.dto.SectionRequest;
import wooteco.subway.line.ui.dto.SectionsOfLineResponse;
import wooteco.subway.line.ui.dto.SectionsResponse;
import wooteco.subway.line.ui.dto.StationOfLineResponse;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

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

        Line persistLine = lineDao.insert(new Line(request.getName(), request.getColor()));
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
        return null;
    }

    public List<LineResponse> findLineResponses() {
        List<Line> persistLines = findLines();
        return persistLines.stream()
            .map(line -> LineResponse.of(line))
            .collect(toList());
    }

    public List<Line> findLines() {
        return lineDao.findAll();
    }

    public LineResponse findLineResponseById(Long id) {
        Line persistLine = findLineById(id);
        return LineResponse.of(persistLine);
    }

    public Line findLineById(Long id) {
        return lineDao.findById(id);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
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

    public SectionsOfLineResponse getSectionsResponseOfLine(Long lineId) {
        Line line = findLineById(lineId);

        List<StationOfLineResponse> collect = line.getStations().stream()
            .map(station -> new StationOfLineResponse(station, findTransferLinesOfStation(station, line)))
            .collect(toList());

        LineWithTransferLineResponse lineResponse = new LineWithTransferLineResponse(line.getId(),
            line.getColor(), line.getName(), collect);
        SectionsResponse sectionsResponse = new SectionsResponse(line.getSections());

        return new SectionsOfLineResponse(lineResponse, sectionsResponse);
    }

    private List<Line> findTransferLinesOfStation(Station station, Line targetLine) {
        List<Line> all = lineDao.findAll();
        return all.stream()
            .filter(line -> !line.equals(targetLine))
            .filter(line -> line.getStations().contains(station))
            .collect(toList());
    }

}
