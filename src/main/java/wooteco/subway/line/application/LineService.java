package wooteco.subway.line.application;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.exception.DuplicatedLineException;
import wooteco.subway.exception.NoAnyLineException;
import wooteco.subway.exception.NoSuchLineException;
import wooteco.subway.exception.NoSuchSectionException;
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
import wooteco.subway.line.dto.TransferLineResponse;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

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

    @Transactional
    public LineResponse saveLine(LineRequest request) {
        try {
            Line persistLine = lineDao.insert(new Line(request.getName(), request.getColor(), request.getExtraFare()));
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

    public List<LineInfoResponse> findLineResponses() {
        List<Line> persistLines = findLines();

        if (persistLines.isEmpty()) {
            throw new NoAnyLineException();
        }

        return persistLines.stream()
            .map(line -> new LineInfoResponse(line.getId(), line.getName(), line.getColor(), line.getStartStation(),
                line.getEndStation(), line.getTotalDistance()))
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
        try {
            return lineDao.findById(id);
        } catch (DataAccessException e) {
            throw new NoSuchLineException();
        }

    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        Line line = new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor());
        if (lineDao.update(line) == 0) {
            throw new NoSuchLineException();
        }
    }

    public void deleteLineById(Long id) {
        if (lineDao.deleteById(id) == 0) {
            throw new NoSuchLineException();
        }
    }

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

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        Station station = stationService.findStationById(stationId);
        line.removeSection(station);

        if (sectionDao.deleteByLineId(lineId) == 0) {
            throw new NoSuchSectionException();
        }
        sectionDao.insertSections(line);
    }

    public void deleteSectionsByLineId(Long id) {
        if (sectionDao.deleteByLineId(id) == 0) {
            throw new NoSuchSectionException();
        }
    }

    public List<LineResponseWithSection> findMap() {
        List<Line> lines = lineDao.findAll();
        List<LineResponseWithSection> lineResponsesWithSections = new ArrayList<>();
        for (Line line : lines) {
            List<SectionResponse> sectionResponses = line.getSections().getSections().stream()
                .map(section -> matchSectionsByLine(line, section))
                .collect(Collectors.toList());
            lineResponsesWithSections.add(
                new LineResponseWithSection(line.getId(), line.getName(), line.getColor(), line.getTotalDistance(),
                    sectionResponses));
        }
        lineResponsesWithSections.sort(Comparator.comparing(LineResponseWithSection::getName));
        return lineResponsesWithSections;
    }

    private SectionResponse matchSectionsByLine(Line line, Section section) {
        Station upStation = section.getUpStation();
        List<Long> lineIdsByStationId = sectionDao.findLineIdsByStationId(upStation.getId());
        List<Line> linesByStationId = lineIdsByStationId.stream()
            .distinct()
            .filter(id -> !line.getId().equals(id))
            .map(id -> lineDao.findById(id))
            .collect(Collectors.toList());
        List<TransferLineResponse> lineResponses = linesByStationId.stream()
            .map(line1 -> new TransferLineResponse(line1.getId(), line1.getName(), line1.getColor()))
            .collect(Collectors.toList());
        return new SectionResponse(upStation.getId(), upStation.getName(), section.getDistance(), lineResponses);
    }
}
