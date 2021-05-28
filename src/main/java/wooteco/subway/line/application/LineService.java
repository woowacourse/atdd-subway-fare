package wooteco.subway.line.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.DuplicateException;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.infrastructure.dao.LineDao;
import wooteco.subway.line.infrastructure.dao.SectionDao;
import wooteco.subway.line.ui.dto.*;
import wooteco.subway.line.ui.dto.map.MapResponse;
import wooteco.subway.line.ui.dto.map.StationOfMapResponse;
import wooteco.subway.line.ui.dto.sectionsofline.LineWithTransferLineResponse;
import wooteco.subway.line.ui.dto.sectionsofline.SectionsOfLineResponse;
import wooteco.subway.line.ui.dto.sectionsofline.StationOfLineResponse;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

import java.util.List;

import static java.util.stream.Collectors.toList;

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
        validateSavableLine(request);
        Line persistLine = lineDao.insert(new Line(request.getName(), request.getColor()));
        persistLine.addSection(addInitSection(persistLine, request));

        return LineResponse.of(persistLine);
    }

    private void validateSavableLine(LineRequest request) {
        if (lineDao.existsByColor(request.getColor())) {
            throw new DuplicateException("이미 존재하는 노선의 색깔입니다.");
        }
    }

    private void validateUpdatableLine(Long id, LineRequest lineUpdateRequest) {
        if (lineDao.existsByColorWithoutId(lineUpdateRequest.getColor(), id)) {
            throw new DuplicateException("이미 존재하는 노선의 색깔입니다.");
        }
    }

    private Section addInitSection(Line line, LineRequest request) {
        if (request.getUpStationId() == null || request.getDownStationId() == null) {
            return null;
        }

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
        return lineDao.findById(id);
    }

    @Transactional
    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        validateUpdatableLine(id, lineUpdateRequest);
        lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    @Transactional
    public void deleteLineById(Long id) {
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

        return createSectionOfResponse(line);
    }

    private SectionsOfLineResponse createSectionOfResponse(Line line) {
        LineWithTransferLineResponse lineResponse = createLineResponse(line);
        SectionsResponse sectionsResponse = new SectionsResponse(line.getSections());

        return new SectionsOfLineResponse(lineResponse, sectionsResponse);
    }

    private LineWithTransferLineResponse createLineResponse(Line line) {
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
                .map(this::toMapResponse)
                .collect(toList());
    }

    private MapResponse toMapResponse(Line line) {
        SectionsOfLineResponse sectionsResponseOfLine = getSectionsResponseOfLine(line.getId());
        List<StationOfMapResponse> stations = toStationOfMapResponses(sectionsResponseOfLine);

        return new MapResponse(line.getId(), line.getName(), line.getColor(), stations);
    }

    private List<StationOfMapResponse> toStationOfMapResponses(SectionsOfLineResponse sectionsResponseOfLine) {
        return sectionsResponseOfLine.getStations().stream()
                .map(stationOfLineResponse ->
                        createSectionOfMapResponse(
                                sectionsResponseOfLine,
                                stationOfLineResponse
                        )
                )
                .collect(toList());
    }

    private StationOfMapResponse createSectionOfMapResponse(SectionsOfLineResponse sectionsResponseOfLine,
                                                            StationOfLineResponse stationOfLineResponse) {
        return new StationOfMapResponse(
                stationOfLineResponse.getId(),
                stationOfLineResponse.getName(),
                getDistance(sectionsResponseOfLine, stationOfLineResponse),
                stationOfLineResponse.getTransferLines()
        );
    }

    private Integer getDistance(SectionsOfLineResponse sectionsResponseOfLine, StationOfLineResponse w) {
        return sectionsResponseOfLine.getSections().stream()
                .filter(section -> section.getUpStation().getId().equals(w.getId()))
                .map(SectionResponse::getDistance)
                .findAny()
                .orElse(-1);
    }

}
