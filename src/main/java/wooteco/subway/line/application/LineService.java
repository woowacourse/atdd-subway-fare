package wooteco.subway.line.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.SubwayException;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.line.dto.*;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationMapResponse;
import wooteco.subway.station.dto.StationTransferResponse;

import java.util.ArrayList;
import java.util.List;
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

    @Transactional
    public LineResponse saveLine(LineRequest request) {
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
        return null;
    }

    public List<LineResponse> findLineResponses() {
        List<Line> persistLines = findLines();
        return persistLines.stream()
                .map(line -> LineResponse.of(line))
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
        validateLineExistence(id);
        return lineDao.findById(id);
    }

    @Transactional
    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        validateLineExistence(id);
        //TODO: 중복 이름 검사?
        lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    @Transactional
    public void deleteLineById(Long id) {
        validateLineExistence(id);
        lineDao.deleteById(id);
    }

    @Transactional
    public void addLineStation(Long lineId, SectionRequest request) {
        validateLineExistence(lineId);
        Line line = findLineById(lineId);
        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());
        line.addSection(upStation, downStation, request.getDistance());

        sectionDao.deleteByLineId(lineId);
        sectionDao.insertSections(line);
    }

    @Transactional
    public void removeLineStation(Long lineId, Long stationId) {
        validateLineExistence(lineId);
        Line line = findLineById(lineId);
        Station station = stationService.findStationById(stationId);
        line.removeSection(station);

        sectionDao.deleteByLineId(lineId);
        sectionDao.insertSections(line);
    }

    @Transactional
    public void updateDistance(final long lineId, final long upStationId, final long downStationId, final Integer distance) {
        if (sectionDao.doesSectionNotExists(lineId, upStationId, downStationId)) {
            throw new NoSuchSectionException();
        }
        sectionDao.updateDistance(lineId, upStationId, downStationId, distance);
    }

    public LineSectionResponse findSectionsById(final long lineId) {
        validateLineExistence(lineId);
        Line line = lineDao.findById(lineId);

        List<StationTransferResponse> stations = stationService.getStationsWithTransferLines(lineId);

        List<SectionResponse> sections = SectionResponse.listOf(line.getSections());

        return LineSectionResponse.of(
                line.getId(),
                line.getName(),
                line.getColor(),
                stations,
                sections
        );
    }

    public void validateLineExistence(final Long id) {
        if (lineDao.doesIdNotExist(id)) {
            throw new NoSuchLineException();
        }
    }

    public List<LineMapResponse> findLinesForMap() {
        List<Line> lines = lineDao.findAll();

        return lines.stream()
                .map(line -> {
                    List<StationMapResponse> stationMapResponses = getStationMapResponses(line, lines);
                    return new LineMapResponse(line.getId(), line.getName(), line.getColor(), stationMapResponses);
                })
                .collect(Collectors.toList());
    }

    private List<StationMapResponse> getStationMapResponses(final Line line, final List<Line> lines) {
        Sections sections = line.getSections();
        List<Station> stations = sections.getStations();

        return stations.stream()
                .map(station -> {
                    Integer distanceToNextStation = sections.getDistanceToNextStation(station);
                    List<TransferLineResponse> transferLineResponses = getTransferLineResponses(lines, line, station);
                    return new StationMapResponse(station.getId(), station.getName(), distanceToNextStation, transferLineResponses);
                })
                .collect(Collectors.toList());
    }

    private List<TransferLineResponse> getTransferLineResponses(final List<Line> lines, final Line currentLine, final Station station) {
        return lines.stream()
                .filter(it -> it.contains(station) && !it.equals(currentLine))
                .map(it -> new TransferLineResponse(it.getId(), it.getName(), it.getColor()))
                .collect(Collectors.toList());
    }
}
