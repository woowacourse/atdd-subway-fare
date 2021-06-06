package wooteco.subway.line.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.DuplicateLineException;
import wooteco.subway.exception.InvalidLineException;
import wooteco.subway.exception.InvalidSectionException;
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
import wooteco.subway.line.dto.StationTransferResponse;
import wooteco.subway.line.dto.TransferLineResponse;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationMapResponse;

@Service
public class LineService {

    private static final int LAST_SORTED_STATION_MARK = -1;
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
        if (isExistingLineName(request.getName())) {
            throw new DuplicateLineException();
        }
        Line persistLine = lineDao.insert(new Line(request.getName(), request.getColor(),
            request.getFare()));
        persistLine.addSection(addInitSection(persistLine, request));
        return LineResponse.of(persistLine);
    }

    private boolean isExistingLineName(String name) {
        return lineDao.exists(name);
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

    @Transactional(readOnly = true)
    public List<LineResponse> findLineResponses() {
        List<Line> persistLines = findLines();
        return persistLines.stream()
            .map(LineResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Line> findLines() {
        return lineDao.findAll();
    }

    @Transactional(readOnly = true)
    public LineResponse findLineResponseById(Long id) {
        Line persistLine = findLineById(id);
        return LineResponse.of(persistLine);
    }

    @Transactional(readOnly = true)
    public Line findLineById(Long id) {
        return lineDao.findById(id)
            .orElseThrow(InvalidLineException::new);
    }

    @Transactional
    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        lineDao.findById(id)
            .orElseThrow(InvalidLineException::new);
        lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineDao.findById(id)
            .orElseThrow(InvalidLineException::new);
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
        lineDao.findById(lineId)
            .orElseThrow(InvalidLineException::new);
        checkSectionExist(lineId, stationId, downStationId);
        int distance = request.getDistance();
        sectionDao.updateByLineId(lineId, stationId, downStationId, distance);
    }

    @Transactional(readOnly = true)
    public LineSectionResponse findSectionsById(Long lineId) {
        Line line = lineDao.findById(lineId).orElseThrow(InvalidLineException::new);
        return LineSectionResponse.of(line, stationService.findStationsWithTransferLine(lineId));
    }

    @Transactional(readOnly = true)
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
            .orElse(LAST_SORTED_STATION_MARK);
    }

    private void checkSectionExist(Long lineId, Long stationId, Long downStationId) {
        if (!sectionDao.exists(lineId, stationId, downStationId)) {
            throw new InvalidSectionException();
        }
    }
}
