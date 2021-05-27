package wooteco.subway.line.application;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.exception.DuplicatedLineException;
import wooteco.subway.exception.NoSuchLineException;
import wooteco.subway.exception.NoSuchStationException;
import wooteco.subway.exception.SameStationsInSectionException;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.dto.LineInfoResponse;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.SectionRequest;
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

        return persistLines.stream()
                .map(line -> new LineInfoResponse(line.getId(), line.getName(), line.getColor(), line.getStartStation(), line.getEndStation(), line.getTotalDistance()))
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
        if(lineDao.update(line) == 0) {
            throw new NoSuchLineException();
        }
    }

    public void deleteLineById(Long id) {
        if(lineDao.deleteById(id) == 0) {
            throw new NoSuchLineException();
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
