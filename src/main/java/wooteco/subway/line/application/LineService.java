package wooteco.subway.line.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.NoSuchLineException;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Lines;
import wooteco.subway.line.dto.CustomLineResponse;
import wooteco.subway.line.dto.LineMapResponse;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.SectionRequest;
import wooteco.subway.line.dto.UpdateLineRequest;
import wooteco.subway.section.application.SectionService;
import wooteco.subway.section.dto.SectionResponse;
import wooteco.subway.section.dto.SectionServiceDto;
import wooteco.subway.station.dto.StationResponse;

@Service
public class LineService {

    private static final int NOT_FOUND = 0;

    private final LineDao lineDao;
    private final SectionService sectionService;

    public LineService(LineDao lineDao, SectionService sectionService) {
        this.lineDao = lineDao;
        this.sectionService = sectionService;
    }

    @Transactional
    public LineResponse createLine(@Valid LineRequest lineRequest) {
        Line line = lineRequest.toLine();
        Lines lines = new Lines(lineDao.findAll());
        lines.validateDuplicate(line);

        Line saveLine = lineDao.insert(line);

        SectionServiceDto sectionServiceDto = SectionServiceDto.of(saveLine, lineRequest);
        sectionService.saveByLineCreate(saveLine, sectionServiceDto);
        List<StationResponse> stationResponses = sectionService.findStationResponsesByLind(saveLine);
        return LineResponse.of(saveLine, stationResponses);
    }

    public List<CustomLineResponse> findCustomLineResponses() {
        return lineDao.findAll()
            .stream()
            .map(line -> CustomLineResponse.of(line, sectionService.findSectionsByLine(line)))
            .collect(Collectors.toList());
    }

    public List<Line> findAll() {
        return lineDao.findAll();
    }

    public List<LineResponse> findAllResponses() {
        return lineDao.findAll()
            .stream()
            .map(line -> LineResponse.of(line, sectionService.findStationResponsesByLind(line)))
            .collect(Collectors.toList());
    }

    public LineResponse findOne(Long lineId) {
        Line line = lineDao.find(lineId);
        List<StationResponse> stationResponses = sectionService.findStationResponsesByLind(line);
        return LineResponse.of(line, stationResponses);
    }

    @Transactional
    public void update(Long lineId, UpdateLineRequest lineRequest) {
        Line line = lineRequest.toLine();
        Lines lines = new Lines(lineDao.findAll());
        lines.validateDuplicate(line);

        if (lineDao.update(lineId, line) == NOT_FOUND) {
            throw new NoSuchLineException();
        }
    }

    @Transactional
    public void delete(Long lineId) {
        if (lineDao.delete(lineId) == NOT_FOUND) {
            throw new NoSuchLineException();
        }
    }

    @Transactional
    public void createSection(Long lineId, SectionRequest sectionRequest) {
        Line line = lineDao.find(lineId);
        SectionServiceDto sectionServiceDto = SectionServiceDto.from(lineId, sectionRequest);
        sectionService.save(line, sectionServiceDto);
    }

    @Transactional
    public void deleteStation(@NotNull Long lineId, @NotNull Long stationId) {
        Line line = lineDao.find(lineId);
        sectionService.delete(line, stationId);
    }

    public List<LineMapResponse> findLineMapResponses() {
        List<LineMapResponse> lineMapResponses = new ArrayList<>();
        Lines lines = new Lines(lineDao.findAll());

        for (Line line : lines.sortedByName()) {
            List<SectionResponse> sectionResponses = sectionService.findSectionResponses(line);
            lineMapResponses.add(LineMapResponse.of(line, sectionResponses));
        }

        return lineMapResponses;
    }
}
