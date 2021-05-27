package wooteco.subway.line.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.InvalidLineException;
import wooteco.subway.exception.NotFoundException;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.line.dto.LineMapResponse;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.LineSectionResponse;
import wooteco.subway.line.dto.SectionDistanceRequest;
import wooteco.subway.line.dto.SectionRequest;
import wooteco.subway.line.dto.SectionResponse;
import wooteco.subway.line.dto.StationTransferResponse;
import wooteco.subway.line.dto.TransferLineResponse;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationMapResponse;

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

    @Transactional
    public LineResponse saveLine(LineRequest request) {
        Line persistLine = lineDao.insert(new Line(request.getName(), request.getColor(),
            request.getFare()));
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
        checkLineExist(id);
        return lineDao.findById(id);
    }

    @Transactional
    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        checkLineExist(id);
        lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    @Transactional
    public void deleteLineById(Long id) {
        checkLineExist(id);
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

    @Transactional
    public void updateLineStation(Long lineId, Long stationId, Long downStationId,
        SectionDistanceRequest request) {
        checkLineExist(lineId);
        checkSectionExist(lineId, stationId, downStationId);
        int distance = request.getDistance();
        sectionDao.updateByLineId(lineId, stationId, downStationId, distance);
    }

    public LineSectionResponse findSectionsById(Long lineId) {
        Line line = lineDao.findById(lineId);
        List<StationTransferResponse> stationResponses = sortStations(line, stationService
            .findStationsWithTransferLine(lineId));
        List<SectionResponse> sectionResponses = convertToSectionResponse(
            line.getSections().sort());
        return new LineSectionResponse(lineId, line.getName(),
            line.getColor(), stationResponses, sectionResponses);
    }

    private List<StationTransferResponse> sortStations(Line line,
        List<StationTransferResponse> stationResponses) {
        return line.getStations().stream()
            .map(station -> stationResponses.stream()
                .filter(response -> response.getId().equals(station.getId()))
                .findFirst()
                .get())
            .collect(Collectors.toList());
    }

    private List<SectionResponse> convertToSectionResponse(Sections sections) {
        return sections.getSections().stream()
            .map(SectionResponse::of)
            .collect(Collectors.toList());
    }

    private void checkLineExist(Long id) {
        if (!lineDao.findExistingLineById(id)) {
            throw new InvalidLineException();
        }
    }

    private void checkSectionExist(Long lineId, Long stationId, Long downStationId) {
        if (!sectionDao.findExistingSection(lineId, stationId, downStationId)) {
            throw new NotFoundException("존재하지 않는 구간입니다.");
        }
    }

    public List<LineMapResponse> findMap() {
        return lineDao.findAll().stream()
            .map(line -> LineMapResponse
                .of(line, convertToStationMapResponse(line, line.getStations())))
            .collect(Collectors.toList());
    }

    private List<StationMapResponse> convertToStationMapResponse(Line line,
        List<Station> stations) {
        Sections sections = line.getSections();
        Map<Long, List<TransferLineResponse>> stationTransferResponseMap = stationService
            .findStationsWithTransferLine(line.getId())
            .stream()
            .collect(Collectors.toMap(StationTransferResponse::getId,
                StationTransferResponse::getTransferLines));

        return stations.stream()
            .map(station -> new StationMapResponse(station.getId(), station.getName(),
                findDistance(sections, station.getId()),
                stationTransferResponseMap.get(station.getId())))
            .collect(Collectors.toList());
    }

    private int findDistance(Sections sections, Long stationId) {
        return sections.getSections()
            .stream()
            .filter(section -> section.getUpStation().getId().equals(stationId))
            .findFirst()
            .map(Section::getDistance)
            .orElse(-1);
    }
}
