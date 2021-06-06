package wooteco.subway.line.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.duplication.LineColorDuplicatedException;
import wooteco.subway.exception.duplication.LineNameDuplicatedException;
import wooteco.subway.exception.notfound.LineNotFoundException;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.SectionRequest;
import wooteco.subway.line.dto.SimpleLineResponse;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

import java.util.List;

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
        validateDuplicatedName(request.getName());
        validateDuplicatedColor(request.getColor());

        Line persistLine = lineDao.insert(new Line(request.getName(), request.getColor(), request.getExtraFare()));
        persistLine.addSection(addInitSection(persistLine, request));

        return LineResponse.of(persistLine);
    }

    private void validateDuplicatedName(String name) {
        if (lineDao.countByName(name) > 0) {
            throw new LineNameDuplicatedException();
        }
    }

    private void validateDuplicatedColor(String color) {
        if (lineDao.countByColor(color) > 0) {
            throw new LineColorDuplicatedException();
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

    public List<SimpleLineResponse> findSimpleLineResponses() {
        return SimpleLineResponse.listOf(findLines());
    }

    public List<LineResponse> findLineResponses() {
        return LineResponse.listOf(findLines());
    }

    public List<Line> findLines() {
        return lineDao.findAll();
    }

    public LineResponse findLineResponseById(Long id) {
        Line persistLine = findLineById(id);
        return LineResponse.of(persistLine);
    }

    public Line findLineById(Long id) {
        checkIfLineExists(id);
        return lineDao.findById(id);
    }

    private void checkIfLineExists(Long id) {
        if (lineDao.countById(id) == 0) {
            throw new LineNotFoundException();
        }
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        checkIfLineExists(id);
        validateDuplicatedName(lineUpdateRequest.getName());
        validateDuplicatedColor(lineUpdateRequest.getColor());
        lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    @Transactional
    public void deleteLineById(Long id) {
        checkIfLineExists(id);
        lineDao.deleteById(id);
        sectionDao.deleteByLineId(id);
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
}
