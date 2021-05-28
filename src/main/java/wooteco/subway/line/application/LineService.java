package wooteco.subway.line.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import wooteco.subway.exception.HttpException;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.LineUpdateRequest;
import wooteco.subway.line.dto.SectionRequest;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

@Service
public class LineService {
    private static final String DUPLICATE_LINE_NAME_ERROR_MESSAGE = "이미 존재하는 노선 이름 입니다.";
    private static final String DUPLICATE_LINE_COLOR_ERROR_MESSAGE = "이미 존재하는 노선 색깔 입니다.";

    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationService stationService;


    public LineService(LineDao lineDao, SectionDao sectionDao, StationService stationService) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationService = stationService;
    }

    public LineResponse saveLine(LineRequest request) {
        Section initSection = getInitSection(request);
        Line newLine = new Line(request.getName(), request.getColor(), request.getExtraFare(), initSection);
        lineNameDuplicateCheckForCreate(newLine);
        lineColorDuplicateCheckForCreate(newLine);
        Line persistLine = lineDao.insert(newLine);
        Section persistSection = sectionDao.insert(persistLine, initSection);
        return LineResponse.of(persistLine, persistSection);
    }

    private void lineNameDuplicateCheckForCreate(Line line) {
        List<Line> linesWithName = lineDao.findAllByName(line.getName());
        if (linesWithName.size() != 0) {
            throw new HttpException(HttpStatus.BAD_REQUEST, DUPLICATE_LINE_NAME_ERROR_MESSAGE);
        }
    }

    private void lineColorDuplicateCheckForCreate(Line line) {
        List<Line> linesWithColor = lineDao.findAllByColor(line.getColor());
        if (linesWithColor.size() != 0) {
            throw new HttpException(HttpStatus.BAD_REQUEST, DUPLICATE_LINE_COLOR_ERROR_MESSAGE);
        }
    }

    private Section getInitSection(LineRequest request) {
        if (request.getUpStationId() != null && request.getDownStationId() != null) {
            Station upStation = stationService.findStationById(request.getUpStationId());
            Station downStation = stationService.findStationById(request.getDownStationId());
            return new Section(upStation, downStation, request.getDistance());
        }
        throw new IllegalArgumentException("라인 생성 시, 초기 구간의 상행역, 하행역이 모두 존재해야 합니다.");
    }

    public List<LineResponse> findLineResponses() {
        List<Line> persistLines = findLines();
        return persistLines.stream()
            .map(LineResponse::of)
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

    public void updateLine(Long id, LineUpdateRequest request) {
        lineNameDuplicateCheckForUpdate(id, request);
        lineColorDuplicateCheckForUpdate(id, request);
        lineDao.update(new Line(id, request.getName(), request.getColor()));
    }

    private void lineNameDuplicateCheckForUpdate(Long lineIdToUpdate, LineUpdateRequest request) {
        List<Line> linesWithName = lineDao.findAllByName(request.getName());
        if (linesWithName.isEmpty()) {
            return;
        }
        Line lineWithName = linesWithName.get(0);
        if (!lineWithName.getId().equals(lineIdToUpdate)) {
            throw new HttpException(HttpStatus.BAD_REQUEST, DUPLICATE_LINE_NAME_ERROR_MESSAGE);
        }
    }

    private void lineColorDuplicateCheckForUpdate(Long lineIdToUpdate, LineUpdateRequest request) {
        List<Line> linesWithName = lineDao.findAllByColor(request.getColor());
        if (linesWithName.isEmpty()) {
            return;
        }
        Line lineWithName = linesWithName.get(0);
        if (!lineWithName.hasId(lineIdToUpdate)) {
            throw new HttpException(HttpStatus.BAD_REQUEST, DUPLICATE_LINE_COLOR_ERROR_MESSAGE);
        }
    }

    public void deleteLineById(Long id) {
        if (lineDao.deleteById(id) != 1) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "존재하지 않은 노선입니다.");
        }
    }
}
