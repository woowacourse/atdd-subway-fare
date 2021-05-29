package wooteco.subway.line.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.DuplicateException;
import wooteco.subway.exception.NotFoundException;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.infrastructure.dao.LineDao;
import wooteco.subway.line.infrastructure.dao.SectionDao;
import wooteco.subway.line.ui.dto.LineRequest;
import wooteco.subway.line.ui.dto.LineResponse;
import wooteco.subway.line.ui.dto.SectionRequest;
import wooteco.subway.line.ui.dto.SectionResponse;
import wooteco.subway.line.ui.dto.SectionsResponse;
import wooteco.subway.line.ui.dto.map.MapResponse;
import wooteco.subway.line.ui.dto.map.StationOfMapResponse;
import wooteco.subway.line.ui.dto.sectionsofline.LineWithTransferLineResponse;
import wooteco.subway.line.ui.dto.sectionsofline.SectionsOfLineResponse;
import wooteco.subway.line.ui.dto.sectionsofline.StationOfLineResponse;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

@Transactional(readOnly = true)
@Service
public class LineService {

    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationService stationService;

    public LineService(LineDao lineDao,
        SectionDao sectionDao,
        StationService stationService) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse saveLine(LineRequest request) {
        validateThatColorAlreadyExists(request);
        Line persistLine = lineDao.insert(
            new Line(
                request.getName(),
                request.getColor(),
                request.getExtraFare()
            )
        );

        persistLine.addSection(addInitSection(persistLine, request));

        return LineResponse.of(persistLine);
    }

    private void validateThatColorAlreadyExists(LineRequest request) {
        if (lineDao.existsByColor(request.getColor())) {
            throw new DuplicateException("이미 존재하는 노선의 색깔입니다.");
        }
    }

    private Section addInitSection(Line line, LineRequest request) {
        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());
        Section section = new Section(upStation, downStation, request.getDistance());
        return sectionDao.insert(line, section);
    }

    public List<LineResponse> findLineResponses() {
        List<Line> persistLines = findLines();
        return persistLines.stream()
            .map(LineResponse::of)
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
        validateThatLineExists(id);

        return lineDao.findById(id);
    }

    @Transactional
    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        validateThatLineExists(id);
        validateUpdatableLine(id, lineUpdateRequest);

        lineDao.update(
            new Line(
                id,
                lineUpdateRequest.getName(),
                lineUpdateRequest.getColor(),
                lineUpdateRequest.getExtraFare()
            )
        );
    }

    private void validateUpdatableLine(Long id, LineRequest lineUpdateRequest) {
        if (lineDao.existsByColorExceptId(lineUpdateRequest.getColor(), id)) {
            throw new DuplicateException("이미 존재하는 노선의 색깔입니다.");
        }
    }

    private void validateThatLineExists(Long id) {
        if (lineDao.existsById(id)) {
            throw new NotFoundException("존재하지 않는 노선입니다.");
        }
    }

    @Transactional
    public void deleteLineById(Long id) {
        validateThatLineExists(id);
        lineDao.deleteById(id);
    }

    @Transactional
    public void addLineStation(Long lineId, SectionRequest request) {
        Line line = findLineById(lineId);
        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());
        line.addSection(upStation, downStation, request.getDistance());

        sectionDao.deleteByLineId(lineId);
        sectionDao.insertSections(line);
    }

    @Transactional
    public void removeLineStation(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        Station station = stationService.findStationById(stationId);
        line.removeSection(station);

        sectionDao.deleteByLineId(lineId);
        sectionDao.insertSections(line);
    }

    public SectionsOfLineResponse getSectionsResponseOfLine(Long lineId) {
        Line line = findLineById(lineId);

        return createSectionOfLineResponse(line);
    }

    private SectionsOfLineResponse createSectionOfLineResponse(Line line) {
        LineWithTransferLineResponse lineResponse = createLineWithTransferLineResponse(line);
        SectionsResponse sectionsResponse = new SectionsResponse(line.getSections());

        return new SectionsOfLineResponse(lineResponse, sectionsResponse);
    }

    private LineWithTransferLineResponse createLineWithTransferLineResponse(Line line) {
        List<StationOfLineResponse> stationOfLineResponses = createStationOfLineResponses(line);
        return new LineWithTransferLineResponse(
            line.getId(),
            line.getColor(),
            line.getName(),
            stationOfLineResponses
        );
    }

    private List<StationOfLineResponse> createStationOfLineResponses(Line line) {
        return line.getStations().stream()
            .map(station -> new StationOfLineResponse(station,
                findTransferLinesOfStation(station, line)))
            .collect(toList());
    }

    private List<Line> findTransferLinesOfStation(Station station, Line targetLine) {
        List<Line> all = lineDao.findAll();
        return all.stream()
            .filter(line -> !line.equals(targetLine))
            .filter(line -> line.getStations().contains(station))
            .collect(toList());
    }

    @Transactional
    public void updateSectionDistance(Long lineId, Long upStationId, Long downStationId,
        int distance) {
        Line line = findLineById(lineId);

        line.updateSectionDistance(
            upStationId,
            downStationId,
            distance
        );

        sectionDao.update(line.getSections().getSections());
    }

    public List<MapResponse> findMap() {
        List<Line> lines = findLines();

        return lines.stream()
            .map(this::createMapResponse)
            .collect(toList());
    }

    private MapResponse createMapResponse(Line line) {
        SectionsOfLineResponse sectionsResponseOfLine = getSectionsResponseOfLine(line.getId());
        List<StationOfMapResponse> stations = createStationOfMapResponses(sectionsResponseOfLine);

        return new MapResponse(line.getId(), line.getName(), line.getColor(), stations);
    }

    private List<StationOfMapResponse> createStationOfMapResponses(
        SectionsOfLineResponse sectionsResponseOfLine) {
        return sectionsResponseOfLine.getStations().stream()
            .map(stationOfLineResponse ->
                createSectionOfMapResponse(
                    sectionsResponseOfLine,
                    stationOfLineResponse
                )
            )
            .collect(toList());
    }

    private StationOfMapResponse createSectionOfMapResponse(
        SectionsOfLineResponse sectionsResponseOfLine,
        StationOfLineResponse stationOfLineResponse) {
        return new StationOfMapResponse(
            stationOfLineResponse.getId(),
            stationOfLineResponse.getName(),
            getDistance(sectionsResponseOfLine, stationOfLineResponse),
            stationOfLineResponse.getTransferLines()
        );
    }

    private Integer getDistance(SectionsOfLineResponse sectionsResponseOfLine,
        StationOfLineResponse w) {
        return sectionsResponseOfLine.getSections().stream()
            .filter(section -> section.getUpStation().getId().equals(w.getId()))
            .map(SectionResponse::getDistance)
            .findAny()
            .orElse(-1);
    }

}
