package wooteco.subway.line.application;

import org.springframework.stereotype.Service;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.line.infrastructure.dao.LineDao;
import wooteco.subway.line.infrastructure.dao.SectionDao;
import wooteco.subway.line.ui.dto.LineRequest;
import wooteco.subway.line.ui.dto.LineResponse;
import wooteco.subway.line.ui.dto.SectionRequest;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

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

    public List<LineResponse> findLineResponses() {
        List<Line> persistLines = findLines();
        return persistLines.stream()
                .map(line -> LineResponse.of(line))
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

    private static class TransferLineResponse {
        private final Long id;
        private final String name;
        private final String color;

        public TransferLineResponse(Line line) {
            this.id = line.getId();
            this.name = line.getName();
            this.color = line.getColor();
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getColor() {
            return color;
        }
    }

    private static class StationOfLineResponse {
        private final Long id;
        private final String name;
        private final List<TransferLineResponse> transferLineResponses;

        public StationOfLineResponse(Station station, List<Line> lines) {
            this.id = station.getId();
            this.name = station.getName();

            this.transferLineResponses = lines.stream()
                    .map(TransferLineResponse::new)
                    .collect(toList());
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public List<TransferLineResponse> getTransferLineResponses() {
            return transferLineResponses;
        }
    }

    private static class LineWithTransferLineResponse {
        private final Long id;
        private final String color;
        private final String name;

        private final List<StationOfLineResponse> stationResponses;

        public LineWithTransferLineResponse(Long id, String color, String name, List<StationOfLineResponse> stationResponses) {
            this.id = id;
            this.color = color;
            this.name = name;

            this.stationResponses = new ArrayList<>(stationResponses);
        }

        public Long getId() {
            return id;
        }

        public String getColor() {
            return color;
        }

        public String getName() {
            return name;
        }

        public List<StationOfLineResponse> getStationResponses() {
            return stationResponses;
        }
    }

    private static class StationOfSectionResponse {
        private final Long id;
        private final String name;

        public StationOfSectionResponse(Station station) {
            this.id = station.getId();
            this.name = station.getName();
        }

        public StationOfSectionResponse(Long id, String name) {
            this.id = id;
            this.name = name;
        }

    }

    private static class SectionResponse {
        private final StationOfSectionResponse upStation;
        private final StationOfSectionResponse downStation;
        private final int distance;

        public SectionResponse(Section section) {
            this.upStation = new StationOfSectionResponse(section.getUpStation());
            this.downStation = new StationOfSectionResponse(section.getDownStation());
            this.distance = section.getDistance();
        }

        public SectionResponse(StationOfSectionResponse upStation,
                               StationOfSectionResponse downStation,
                               int distance) {
            this.upStation = upStation;
            this.downStation = downStation;
            this.distance = distance;
        }
    }

    private static class SectionsResponse {
        private final List<SectionResponse> sectionResponses;

        public SectionsResponse(Sections sections) {
            this.sectionResponses = sections.getSections().stream()
                    .map(SectionResponse::new)
                    .collect(toList());
        }

        public SectionsResponse(List<SectionResponse> sectionResponses) {
            this.sectionResponses = sectionResponses;
        }

        public List<SectionResponse> getSectionResponses() {
            return sectionResponses;
        }

    }

    public static class SectionsOfLineResponse {
        private final Long id;
        private final String color;
        private final String name;

        private final List<StationOfLineResponse> stations;
        private final List<SectionResponse> sections;

        public SectionsOfLineResponse(LineWithTransferLineResponse lineResponse, SectionsResponse sectionsResponse) {
            this.id = lineResponse.getId();
            this.color = lineResponse.getColor();
            this.name = lineResponse.getName();

            this.stations = lineResponse.getStationResponses();
            this.sections = sectionsResponse.getSectionResponses();
        }

        public Long getId() {
            return id;
        }

        public String getColor() {
            return color;
        }

        public String getName() {
            return name;
        }

        public List<StationOfLineResponse> getStations() {
            return stations;
        }

        public List<SectionResponse> getSections() {
            return sections;
        }
    }

    public SectionsOfLineResponse getSectionsResponseOfLine(Long lineId) {
        Line line = findLineById(lineId);

        List<StationOfLineResponse> collect = line.getStations().stream().map(station -> new StationOfLineResponse(station, findTransferLinesOfStation(station))).collect(toList());


        LineWithTransferLineResponse lineResponse = new LineWithTransferLineResponse(line.getId(), line.getColor(), line.getName(), collect);
        SectionsResponse sectionsResponse = new SectionsResponse(line.getSections());

        return new SectionsOfLineResponse(lineResponse, sectionsResponse);
    }

    private List<Line> findTransferLinesOfStation(Station station) {
        List<Line> all = lineDao.findAll();
        return all.stream().filter(line -> line.getStations().contains(station)).collect(toList());
    }

}
