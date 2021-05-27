package wooteco.subway.line.application;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.SubwayCustomException;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.SectionRequest;
import wooteco.subway.line.dto.SimpleLineResponse;
import wooteco.subway.line.exception.LineException;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.exception.StationException;

import java.util.List;
import java.util.stream.Collectors;

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
            checkDuplicatedLineColor(request.getColor());
            Line persistLine = lineDao.insert(new Line(request.getName(), request.getColor()));
            persistLine.addSection(addInitSection(persistLine, request));
            return LineResponse.of(persistLine);
        } catch (DuplicateKeyException e) {
            throw new SubwayCustomException(LineException.DUPLICATED_LINE_NAME_EXCEPTION);
        }
    }

    private void checkDuplicatedLineColor(String color) {
        if(lineDao.existColor(color)) {
           throw new SubwayCustomException(LineException.DUPLICATED_LINE_COLOR_EXCEPTION);
        }
    }

    private Section addInitSection(Line line, LineRequest request) {
        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());
        Section section = new Section(upStation, downStation, request.getDistance());
        return sectionDao.insert(line, section);
    }

    public List<SimpleLineResponse> findSimpleLineResponses() {
        List<Line> persistLines = findAllSimple();
        return persistLines.stream()
                .map(SimpleLineResponse::of)
                .collect(Collectors.toList());
    }

    private List<Line> findAllSimple() {
        return lineDao.findAllSimple();
    }

    public List<Line> findLines() {
        return lineDao.findAll();
    }

    public LineResponse findLineResponseById(Long id) {
        Line persistLine = findLineById(id);
        return LineResponse.of(persistLine);
    }
    
    public List<LineResponse> findLineResponses() {
        return LineResponse.listOf(findLines());
    }

    public Line findLineById(Long id) {
        try {
            return lineDao.findById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new SubwayCustomException(LineException.NOT_EXIST_LINE_EXCEPTION);
        }
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        try {
            checkDuplicatedUpdateColor(id, lineUpdateRequest.getColor());
            int updateRow = lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
            validateUpdateRow(updateRow);
        } catch (DuplicateKeyException e) {
            throw new SubwayCustomException(LineException.DUPLICATED_LINE_NAME_EXCEPTION);
        }
    }

    private void checkDuplicatedUpdateColor(Long id, String color) {
        if(lineDao.existColor(id, color)) {
            throw new SubwayCustomException(LineException.DUPLICATED_LINE_COLOR_EXCEPTION);
        }
    }

    public void deleteLineById(Long id) {
        sectionDao.deleteByLineId(id);
        int updateRow = lineDao.deleteById(id);
        validateUpdateRow(updateRow);
    }

    private void validateUpdateRow(int updateRow) {
        if(updateRow != 1) {
            throw new SubwayCustomException(LineException.NOT_EXIST_LINE_EXCEPTION);
        }
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
