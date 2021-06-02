package wooteco.subway.line.application;

import java.util.List;
import java.util.Objects;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.DuplicatedFieldException;
import wooteco.subway.exception.ValidationFailureException;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.LineUpdateRequest;
import wooteco.subway.line.dto.MapResponse;
import wooteco.subway.line.dto.SectionRequest;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

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

    public LineResponse saveLine(LineRequest request) {
        Line persistLine;
        try {
            persistLine = lineDao.insert(new Line(request.getName(), request.getColor(), request.getExtraFare()));
        } catch (DuplicateKeyException e) {
            throw new DuplicatedFieldException("중복 필드가 있어 노선 저장에 실패했습니다.");
        }

        persistLine.addSection(addInitSection(persistLine, request));
        return LineResponse.of(persistLine);
    }

    private Section addInitSection(Line line, LineRequest request) {
        validateSectionStations(request.getUpStationId(), request.getDownStationId());

        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());
        Section section = new Section(upStation, downStation, request.getDistance());
        return sectionDao.insert(line, section);
    }

    private void validateSectionStations(Long upStationId, Long downStationId) {
        validateStationsNonNull(upStationId, downStationId);
        validateDifferentStations(upStationId, downStationId);
    }

    private void validateStationsNonNull(Long upStationId, Long downStationId) {
        if (Objects.isNull(upStationId) || Objects.isNull(downStationId)) {
            throw new ValidationFailureException("추가하려는 구간의 상행역과 하행역에 null이 있습니다.");
        }
    }

    private void validateDifferentStations(Long upStationId, Long downStationId) {
        if (upStationId.equals(downStationId)) {
            throw new ValidationFailureException("추가하려는 구간의 상행역과 하향역이 같습니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findLineResponses() {
        return LineResponse.listOf(findLines());
    }

    public List<Line> findLines() {
        return lineDao.findAll();
    }

    public LineResponse findLineResponseById(Long id) {
        return LineResponse.of(
            findLineById(id)
        );
    }

    public Line findLineById(Long id) {
        return lineDao.findById(id);
    }

    public void updateLine(Long id, LineUpdateRequest request) {
        try {
            lineDao.update(new Line(id, request.getName(), request.getColor(), request.getExtraFare()));
        } catch (DuplicateKeyException e) {
            throw new DuplicatedFieldException("중복 필드가 있어 노선 수정에 실패했습니다.");
        }
    }

    public void deleteLineById(Long id) {
        lineDao.deleteById(id);
    }

    public void addLineStation(Long lineId, SectionRequest request) {
        validateSectionStations(request.getUpStationId(), request.getDownStationId());

        Line line = findLineById(lineId);
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

    public List<MapResponse> findMapResponse() {
        return MapResponse.listOf(findLines());
    }
}
