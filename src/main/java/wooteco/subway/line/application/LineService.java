package wooteco.subway.line.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.SectionRequest;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        validateDuplicatedName(request);
        validateDuplicatedColor(request);

        Line persistLine = lineDao.insert(new Line(request.getName(), request.getColor(), request.getExtraFare()));
        persistLine.addSection(addInitSection(persistLine, request));
        return toLineResponse(persistLine);
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
        return findLines().stream()
                .map(this::toLineResponse)
                .sorted(Comparator.comparing(LineResponse::getName))
                .collect(Collectors.toList());
    }

    public List<Line> findLines() {
        return lineDao.findAll();
    }

    public LineResponse findLineResponseById(Long id) {
        return toLineResponse(findLineById(id));
    }

    public Line findLineById(Long id) {
        return lineDao.findById(id)
                .orElseThrow(() -> new LineException("존재하지 않는 노선입니다."));
    }

    @Transactional
    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        Line oldLine = findLineById(id);

        if (!oldLine.isSameName(lineUpdateRequest.getName())) {
            validateDuplicatedName(lineUpdateRequest);
        }

        if (!oldLine.isSameColor(lineUpdateRequest.getColor())) {
            validateDuplicatedColor(lineUpdateRequest);
        }

        lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    private void validateDuplicatedColor(LineRequest lineUpdateRequest) {
        if (lineDao.isExistingColor(lineUpdateRequest.getColor())) {
            throw new LineException("이미 존재하는 노선 색상입니다.");
        }
    }

    private void validateDuplicatedName(LineRequest lineUpdateRequest) {
        if (lineDao.isExistingName(lineUpdateRequest.getName())) {
            throw new LineException("이미 존재하는 노선 이름입니다.");
        }
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

    private LineResponse toLineResponse(Line line) {
        List<StationResponse> stations = line.getStations()
                .stream()
                .map(stationService::toStationResponse)
                .collect(Collectors.toList());
        return LineResponse.of(line, stations);
    }
}
