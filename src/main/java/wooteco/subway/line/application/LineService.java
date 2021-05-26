package wooteco.subway.line.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.line.dto.*;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class LineService {
    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationService stationService;

    public LineResponse saveLine(LineRequest request) {
        Line persistLine = lineDao.insert(new Line(request.getName(), request.getColor()));
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

    public List<TotalLineResponse> findTotalLineResponses() {
        List<Line> persistLines = findLines();
        return persistLines.stream()
                .map(TotalLineResponse::of)
                .collect(toList());
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

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        lineDao.deleteById(id);
        sectionDao.deleteByLineId(id);
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

    public List<LineMapResponse> findLineMapResponses() {
        List<LineMapResponse> lineMapResponses = new ArrayList<>();

        for (Line line : lineDao.findAll()) {
            Sections sections = line.getSections();
            List<SectionResponse> sectionResponses = getSectionResponses(sections, line.getId());
            LineMapResponse lineMapReponse = new LineMapResponse(line, sectionResponses);
            lineMapResponses.add(lineMapReponse);
        }
        return lineMapResponses;
    }

    private List<SectionResponse> getSectionResponses(Sections sections, Long lineId) {
        List<SectionResponse> sectionResponses = new ArrayList<>();
        List<Section> sectionList = sections.getSections();

        for (Section section : sectionList) {
            List<Line> transferLines = lineDao.findIncludingStation(section.getUpStation().getId(), lineId);
            List<TransferLineResponse> transferLineResponses = transferLines.stream()
                    .map(TransferLineResponse::new)
                    .collect(toList());
            SectionResponse sectionResponse = new SectionResponse(section, transferLineResponses);
            sectionResponses.add(sectionResponse);
        }
        Section lastSection = sectionList.get(sectionList.size() - 1);
        sectionResponses.add(getLastSectionResponses(lastSection, lineId));
        return sectionResponses;
    }

    private SectionResponse getLastSectionResponses(Section section, Long lineId) {
        List<Line> transferLines = lineDao.findIncludingStation(section.getDownStation().getId(), lineId);
        List<TransferLineResponse> transferLineResponses = transferLines.stream()
                .map(TransferLineResponse::new)
                .collect(toList());
        return new SectionResponse(section.getId(), section.getDownStation().getName(),
                0, transferLineResponses);
    }
}
