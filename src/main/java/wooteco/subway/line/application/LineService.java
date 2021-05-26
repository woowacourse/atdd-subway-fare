package wooteco.subway.line.application;

import org.springframework.dao.DuplicateKeyException;
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
        try{
            Line persistLine = lineDao.insert(new Line(request.getName(), request.getColor()));
            persistLine.addSection(addInitSection(persistLine, request));
            return LineResponse.of(persistLine);
        } catch (DuplicateKeyException e) {
            throw new IllegalArgumentException("지하철 노선 이름이 이미 존재합니다");
        }
    }

    private Section addInitSection(Line line, LineRequest request) {
        if (request.getUpStationId() != null && request.getDownStationId() != null) {
            Station upStation = stationService.findStationById(request.getUpStationId());
            Station downStation = stationService.findStationById(request.getDownStationId());
            validateStationsInSection(upStation, downStation);
            Section section = new Section(upStation, downStation, request.getDistance());
            return sectionDao.insert(line, section);
        }
        return null;
    }

    private void validateStationsInSection(Station upStation, Station downStation) {
        if (upStation.equals(downStation)) {
            throw new InvalidInsertException("유효하지 않는 요청 값입니다");
        }
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
        return lineDao.findById(id);
    }

    public LineUpdateResponse updateLine(Long id, LineRequest lineUpdateRequest) {
        Line currentLine = lineDao.findById(id);
        validatesChangeName(lineUpdateRequest.getName(), currentLine.getName());
        lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
        Line line = lineDao.findById(id);
        return LineUpdateResponse.of(line);
    }

    private void validatesChangeName(String newName, String currentName) {
        if (lineDao.existNewNameExceptCurrentName(newName, currentName)) {
            throw new IllegalArgumentException("지하철 노선 이름이 이미 존재합니다");
        }
    }

    public void deleteLineById(Long id) {
        lineDao.deleteById(id);
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
