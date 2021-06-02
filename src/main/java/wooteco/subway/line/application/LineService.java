package wooteco.subway.line.application;

import java.util.List;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.SubwayException;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.SectionRequest;
import wooteco.subway.line.dto.SimpleLineResponse;
import wooteco.subway.line.exception.LineExceptionSet;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

@Service
@Transactional
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
        try {
            checkDuplicateLineColor(request);
            Line persistLine = lineDao
                .insert(new Line(request.getName(), request.getColor(), request.getExtraFare()));
            persistLine.addSection(addInitSection(persistLine, request));
            return LineResponse.of(persistLine);
        } catch (DuplicateKeyException exception) {
            throw new SubwayException(LineExceptionSet.DUPLICATE_LINE_NAME_EXCEPTION);
        }
    }

    private void checkDuplicateLineColor(LineRequest request) {
        if (lineDao.existColor(request.getColor())) {
            throw new SubwayException(LineExceptionSet.DUPLICATE_LINE_COLOR_EXCEPTION);
        }
    }

    private Section addInitSection(Line line, LineRequest request) {
        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());
        Section section = new Section(upStation, downStation, request.getDistance());
        return sectionDao.insert(line, section);
    }

    public List<SimpleLineResponse> findAllSimpleLineResponses() {
        List<Line> simpleLines = lineDao.findAll();
        return SimpleLineResponse.listOf(simpleLines);
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
        try {
            return lineDao.findById(id);
        } catch (EmptyResultDataAccessException exception) {
            throw new SubwayException(LineExceptionSet.NOT_EXIST_LINE_EXCEPTION);
        }
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        checkUpdateDuplicateLineColor(id, lineUpdateRequest);
        try {
            int updateRow = lineDao
                .update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
            validateUpdate(updateRow);
        } catch (DuplicateKeyException exception) {
            throw new SubwayException(LineExceptionSet.DUPLICATE_LINE_NAME_EXCEPTION);
        }
    }

    private void checkUpdateDuplicateLineColor(Long id, LineRequest lineUpdateRequest) {
        if (lineDao.existColor(id, lineUpdateRequest.getColor())) {
            throw new SubwayException(LineExceptionSet.DUPLICATE_LINE_COLOR_EXCEPTION);
        }
    }

    private void validateUpdate(int updateRow) {
        if (updateRow != 1) {
            throw new SubwayException(LineExceptionSet.NOT_EXIST_LINE_EXCEPTION);
        }
    }

    public void deleteLineById(Long id) {
        sectionDao.deleteByLineId(id);
        int updateRow = lineDao.deleteById(id);
        validateUpdate(updateRow);
    }

    public void addLineStation(Long lineId, SectionRequest request) {
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

}
