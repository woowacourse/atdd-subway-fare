package wooteco.subway.line.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.DuplicateException;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Lines;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.SectionRequest;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
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
        if (lineDao.existsByName(request.getName())) {
            throw new DuplicateException("이미 존재하는 노선입니다.");
        }

        if (lineDao.existsByColor(request.getColor())) {
            throw new DuplicateException("이미 존재하는 노선 색깔입니다.");
        }

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
        return LineResponse.listOf(persistLines);
    }

    public List<Line> findLines() {
        return lineDao.findAll();
    }

    public LineResponse findLineResponseById(Long id) {
        Line persistLine = findLineById(id);
        return LineResponse.of(persistLine);
    }

    public Line findLineById(Long id) {
        if (lineDao.existsById(id)) {
            return lineDao.findById(id);
        }
        throw new NoSuchElementException("존재하지 않는 노선입니다.");
    }

    @Transactional
    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        if (lineDao.existsById(id)) {
            Line originalLine = lineDao.findById(id);
            Lines lines = new Lines(lineDao.findAll());
            lineDao.update(lines.update(originalLine, lineUpdateRequest));
        }
    }

    @Transactional
    public void deleteLineById(Long id) {
        if (lineDao.existsById(id)) {
            sectionDao.deleteByLineId(id);
            lineDao.deleteById(id);
            return;
        }
        throw new NoSuchElementException("존재하지 않는 노선입니다.");
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

        if (line.hasStation(stationId)) {
            line.removeSection(station);
            sectionDao.deleteByLineId(lineId);
            sectionDao.insertSections(line);
            return;
        }
        throw new UnsupportedOperationException("노선에 등록되지 않은 역은 삭제할 수 없습니다.");
    }
}
