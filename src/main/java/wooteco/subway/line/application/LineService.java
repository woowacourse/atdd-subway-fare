package wooteco.subway.line.application;

import org.springframework.stereotype.Service;
import wooteco.subway.exception.InvalidInsertException;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.dto.*;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

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

    public LineResponse saveLine(LineRequest request) {
        validatesNameAndColor(request.getName(), request.getColor());
        Line persistLine = lineDao.insert(new Line(request.getName(), request.getColor(), request.getExtraFare()));
        persistLine.addSection(addInitSection(persistLine, request));
        return LineResponse.of(persistLine);
    }

    private void validatesNameAndColor(String name, String color) {
        if (lineDao.isExistsName(name)) {
            throw new InvalidInsertException("지하철 노선 이름이 이미 존재합니다.");
        }
        if (lineDao.isExistsColor(color)) {
            throw new InvalidInsertException("지하철 노선 색깔이 이미 존재합니다.");
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

    public List<LineWithStationsResponse> findLineResponses() {
        List<Line> persistLines = findLines();
        return persistLines.stream()
                .map(this::getLineWithStationsResponse)
                .collect(Collectors.toList());
    }

    public List<Line> findLines() {
        List<Line> lines = lineDao.findAll();
        return lines;
    }

    public LineWithStationsResponse findLineResponseById(Long id) {
        Line persistLine = findLineById(id);
        return getLineWithStationsResponse(persistLine);
    }

    private LineWithStationsResponse getLineWithStationsResponse(Line persistLine) {
        List<Line> persistLines = lineDao.findAll();
        List<Station> stationInLine = persistLine.getStations();

        List<StationsResponseInLine> stationsResponseInLines = stationInLine.stream()
                .map(station -> createResponse(persistLine, persistLines, station))
                .collect(Collectors.toList());

        return LineWithStationsResponse.of(persistLine, stationsResponseInLines);
    }

    private StationsResponseInLine createResponse(Line persistLine, List<Line> persistLines, Station station) {
        List<TransferLinesResponse> transferLinesResponse = makeLineWithTransferLineResponse(persistLine, persistLines, station);
        int nextStationDistance = persistLine.getNextStationDistance(station);
        return StationsResponseInLine.of(station, nextStationDistance, transferLinesResponse);
    }

    private List<TransferLinesResponse> makeLineWithTransferLineResponse(Line persistLine, List<Line> persistLines, Station station) {
        List<Line> transferLine = persistLines.stream()
                .filter(line -> line.contain(station) && !line.isSameIdWith(persistLine.getId()))
                .collect(Collectors.toList());

        return transferLine.stream()
                .map(TransferLinesResponse::of)
                .collect(Collectors.toList());
    }

    public Line findLineById(Long id) {
        return lineDao.findById(id);
    }

    public LineUpdateResponse updateLine(Long id, LineRequest lineUpdateRequest) {
        Line currentLine = lineDao.findById(id);
        validatesChangeName(lineUpdateRequest.getName(), currentLine.getName());
        validatesChangeColor(lineUpdateRequest.getColor(), currentLine.getColor());
        lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
        Line line = lineDao.findById(id);
        return LineUpdateResponse.of(line);
    }

    private void validatesChangeColor(String newColor, String currentColor) {
        if (lineDao.existNewColorExceptCurrentColor(newColor, currentColor)) {
            throw new InvalidInsertException("지하철 노선 색깔이 이미 존재합니다");
        }
    }

    private void validatesChangeName(String newName, String currentName) {
        if (lineDao.existNewNameExceptCurrentName(newName, currentName)) {
            throw new InvalidInsertException("지하철 노선 이름이 이미 존재합니다");
        }
    }

    public void deleteLineById(Long id) {
        lineDao.deleteById(id);
        sectionDao.deleteByLineId(id);
    }

    public SectionResponse addLineStation(Long lineId, SectionRequest request) {
        Line line = findLineById(lineId);
        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());
        line.addSection(upStation, downStation, request.getDistance());

        sectionDao.deleteByLineId(lineId);
        sectionDao.insertSections(line);

        Line updatedLine = findLineById(lineId);
        Section section = updatedLine.findSectionByIds(request.getUpStationId(), request.getDownStationId());
        return SectionResponse.of(section);
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        Station station = stationService.findStationById(stationId);
        line.removeSection(station);

        sectionDao.deleteByLineId(lineId);
        sectionDao.insertSections(line);
    }

}
