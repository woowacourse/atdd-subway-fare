package wooteco.subway.line.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.DuplicatedException;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.LineWithSectionsResponse;
import wooteco.subway.line.dto.SectionRequest;
import wooteco.subway.line.exception.InvalidSectionRequestException;
import wooteco.subway.line.exception.LineNotFoundException;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

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
        if (lineDao.isExistByName(request.getName())) {
            throw new DuplicatedException(request.getName());
        }
        if (lineDao.isExistByColor(request.getColor())) {
            throw new DuplicatedException(request.getColor());
        }
        Line persistLine = lineDao.insert(new Line(request.getName(), request.getColor(), request.getExtraFare()));
        persistLine.addSection(addInitSection(persistLine, request));
        return LineResponse.of(persistLine);
    }

    private Section addInitSection(Line line, LineRequest request) {
        if (request.getUpStationId() != null && request.getDownStationId() != null) {
            checkSameEndStations(request.getUpStationId(), request.getDownStationId());
            Station upStation = stationService.findStationById(request.getUpStationId());
            Station downStation = stationService.findStationById(request.getDownStationId());
            Section section = new Section(upStation, downStation, request.getDistance());
            return sectionDao.insert(line, section);
        }
        return null;
    }

    private void checkSameEndStations(Long upStationId, Long downStationId) {
        if (upStationId.equals(downStationId)) {
            throw new InvalidSectionRequestException("상행역과 하행역은 같을 수 없습니다.");
        }
    }

    public List<LineWithSectionsResponse> findLineWithSectionsResponses() {
        List<Line> persistLines = findLines();
        return persistLines.stream()
                .map(LineWithSectionsResponse::of)
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
        if (!lineDao.isExistById(id)) {
            throw new LineNotFoundException(String.valueOf(id));
        }
        return lineDao.findById(id);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        if (!lineDao.isExistById(id)) {
            throw new LineNotFoundException(String.valueOf(id));
        }
        lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    @Transactional
    public void deleteLineById(Long id) {
        if (!lineDao.isExistById(id)) {
            throw new LineNotFoundException(String.valueOf(id));
        }
        lineDao.deleteById(id);
        if (sectionDao.isExistByLineId(id)) {
            sectionDao.deleteByLineId(id);
        }
    }

    public void addLineStation(Long lineId, SectionRequest request) {
        Line line = findLineById(lineId);
        checkSameEndStations(request.getUpStationId(), request.getDownStationId());
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
}
