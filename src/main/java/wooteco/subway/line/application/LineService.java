package wooteco.subway.line.application;

import org.springframework.stereotype.Service;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.dto.*;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

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
        if (lineDao.findByName(request.getName()).isPresent()) {
            throw new DuplicateLineException();
        }


        Line persistLine = lineDao.insert(request.toEntity());
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
        return persistLines.stream()
                .map(line -> LineResponse.of(line))
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
        validateExistLine(id);
        return lineDao.findById(id);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        validateExistLine(id);

        lineDao.findByName(lineUpdateRequest.getName())
                .filter(line -> line.getName().equals(lineUpdateRequest.getName()) && !line.getId().equals(id))
                .ifPresent(station -> {
                    throw new DuplicateLineException();
                });

        lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor(), lineUpdateRequest.getExtraFare()));
    }

    private void validateExistLine(Long id) {
        lineDao.findLineExceptSectionById(id)
                .orElseThrow(NoLineException::new);
    }

    public void deleteLineById(Long id) {
        validateExistLine(id);
        lineDao.deleteById(id);
    }

    public SectionResponse addLineStation(Long lineId, SectionRequest request) {
        Line line = findLineById(lineId);
        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());
        Section section = new Section(upStation, downStation, request.getDistance());
        line.addSection(section);

        sectionDao.deleteByLineId(lineId);
        sectionDao.insertSections(line);

        return SectionResponse.of(section);
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        Station station = stationService.findStationById(stationId);
        line.removeSection(station);

        sectionDao.deleteByLineId(lineId);
        sectionDao.insertSections(line);
    }

    public List<LinesResponse> findAllInfo() {
        List<Line> lines = lineDao.findAll();
        return lines.stream()
                .map(line ->
                        new LinesResponse(
                                line.getId(), line.getName(), line.getColor(),
                                line.getExtraFare(),
                                line.getSections()
                        )
                )
                .collect(Collectors.toList());
    }
}
