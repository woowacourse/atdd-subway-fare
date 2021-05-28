package wooteco.subway.line.application;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import wooteco.subway.exception.badrequest.DuplicateUniqueKeyException;
import wooteco.subway.exception.notfound.LineNotFoundException;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.dto.*;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationTransferLinesDto;

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
        validateUniqueKey(request.getName(), request.getColor());
        Line persistLine = lineDao.insert(new Line(request.getName(), request.getColor()));
        persistLine.addSection(addInitSection(persistLine, request));
        return LineResponse.of(persistLine);
    }

    private void validateUniqueKey(String name, String color) {
        validateDuplicateName(name);
        validateDuplicateColor(color);
    }


    private void validateDuplicateName(String name) {
        if (lineDao.existLineName(name)) {
            throw new DuplicateUniqueKeyException("지하철 노선 이름이 이미 존재합니다");
        }
    }

    private void validateDuplicateColor(String color) {
        if (lineDao.existLineColor(color)) {
            throw new DuplicateUniqueKeyException("지하철 노선 색깔이 이미 존재합니다");
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

    public List<Line> findLines() {
        return lineDao.findAll();
    }

    public List<LineStationWithTransferLinesResponse> findLinesWithStationsTransferLines() {
        List<Line> persistLines = findLines();
        List<StationTransferLinesDto> allStationWithTransferLines = stationService.findAllWithTransferLines();
        return persistLines.stream()
                .map(line -> LineStationWithTransferLinesResponse.of(line, allStationWithTransferLines))
                .collect(Collectors.toList());
    }

    public LineStationWithTransferLinesResponse findLineResponseById(Long id) {
        Line persistLine = findLineById(id);
        List<StationTransferLinesDto> allStationWithTransferLines = stationService.findAllWithTransferLines();
        return LineStationWithTransferLinesResponse.of(persistLine, allStationWithTransferLines);
    }

    public Line findLineById(Long id) {
        return lineDao.findById(id).orElseThrow(LineNotFoundException::new);
    }

    public LineNameColorResponse updateLine(Long id, LineUpdateRequest request) {
        Line line = lineDao.findById(id).orElseThrow(LineNotFoundException::new);
        try {
            Line updatedLine = line.update(request.getName(), request.getColor());
            lineDao.update(updatedLine);
            return new LineNameColorResponse(id, request.getName(), request.getColor());
        } catch (DuplicateKeyException e) {
            validateUpdateUniqueKey(line, request);
        }
        return null;
    }

    private void validateUpdateUniqueKey(Line line, LineUpdateRequest request) {
        if (!line.getName().equals(request.getName())) {
            validateDuplicateName(request.getName());
        }
        if (!line.getColor().equals(request.getColor())) {
            validateDuplicateColor(request.getColor());
        }
    }

    public void deleteLineById(Long id) {
        lineDao.deleteById(id);
    }

    public SectionAddResponse addLineStation(Long lineId, SectionRequest request) {
        Line line = findLineById(lineId);
        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());
        line.addSection(upStation, downStation, request.getDistance());

        sectionDao.deleteByLineId(lineId);
        sectionDao.insertSections(line);

        return new SectionAddResponse(request.getUpStationId(), request.getDownStationId(), request.getDistance());
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        Station station = stationService.findStationById(stationId);
        line.removeSection(station);

        sectionDao.deleteByLineId(lineId);
        sectionDao.insertSections(line);
    }
}
