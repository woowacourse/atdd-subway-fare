package wooteco.subway.line.application;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.exception.duplicated.DuplicatedLineException;
import wooteco.subway.exception.nosuch.NoSuchLineException;
import wooteco.subway.exception.nosuch.NoSuchSectionException;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.dto.LineInfoResponse;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.LineResponseWithSection;
import wooteco.subway.line.dto.SectionRequest;
import wooteco.subway.line.dto.SectionResponse;
import wooteco.subway.line.dto.SectionResponseAssembler;
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

    @Transactional
    public LineResponse saveLine(LineRequest request) {
        try {
            Line persistLine = lineDao.insert(new Line(request));
            persistLine.addSection(addInitSection(persistLine, request));
            return LineResponse.of(persistLine);
        } catch (DataAccessException e) {
            throw new DuplicatedLineException();
        }
    }

    private Section addInitSection(Line line, LineRequest request) {
        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());
        Section section = new Section(upStation, downStation, request.getDistance());
        return sectionDao.insert(line, section);
    }

    @Transactional(readOnly = true)
    public List<LineInfoResponse> findLineResponses() {
        return findLines().stream()
            .map(LineInfoResponse::new)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Line> findLines() {
        return lineDao.findAll();
    }

    @Transactional(readOnly = true)
    public LineResponse findLineResponseById(Long id) {
        Line persistLine = findLineById(id);
        return LineResponse.of(persistLine);
    }

    @Transactional(readOnly = true)
    public Line findLineById(Long id) {
        try {
            return lineDao.findById(id);
        } catch (Exception e) {
            throw new NoSuchLineException();
        }
    }

    @Transactional
    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        Line line = new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor());
        if (lineDao.update(line) == 0) {
            throw new NoSuchLineException();
        }
    }

    @Transactional
    public void deleteLineById(Long id) {
        if (lineDao.deleteById(id) == 0) {
            throw new NoSuchLineException();
        }
    }

    @Transactional
    public void addLineStation(Long lineId, SectionRequest request) {
        Line line = findLineById(lineId);
        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());
        line.addSection(upStation, downStation, request.getDistance());

        if (sectionDao.deleteByLineId(lineId) == 0) {
            throw new NoSuchSectionException();
        }
        sectionDao.insertSections(line);
    }

    @Transactional
    public void removeLineStation(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        Station station = stationService.findStationById(stationId);
        line.removeSection(station);

        if (sectionDao.deleteByLineId(lineId) == 0) {
            throw new NoSuchSectionException();
        }
        sectionDao.insertSections(line);
    }

    @Transactional
    public void deleteSectionsByLineId(Long id) {
        if (sectionDao.deleteByLineId(id) == 0) {
            throw new NoSuchSectionException();
        }
    }

    @Transactional(readOnly = true)
    public List<LineResponseWithSection> findMap() {
        List<Line> lines = lineDao.findAll();
        return assembleLineResponseWithSections(lines);
    }

    private List<LineResponseWithSection> assembleLineResponseWithSections(List<Line> lines) {
        return lines.stream()
            .map(line -> new LineResponseWithSection(line, assembleSectionResponses(line.getSections())))
            .sorted(Comparator.comparing(LineResponseWithSection::getName))
            .collect(Collectors.toList());
    }

    private List<SectionResponse> assembleSectionResponses(List<Section> sections) {
        List<SectionResponse> sectionResponses = sections.stream()
            .map(section -> SectionResponseAssembler.assemble(section, section.getUpStation(),
                lineDao.findByStationId(section.getUpStation())))
            .collect(Collectors.toList());
        Section lastSection = sections.get(sections.size() - 1);
        sectionResponses.add(SectionResponseAssembler.assemble(lastSection, lastSection.getDownStation(),
            lineDao.findByStationId(lastSection.getDownStation())));
        return sectionResponses;
    }
}