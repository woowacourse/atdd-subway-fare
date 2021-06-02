package wooteco.subway.line.application;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.dto.LineDetailResponse;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.SectionRequest;
import wooteco.subway.map.dto.MapDetailResponse;
import wooteco.subway.map.dto.MapSectionDto;
import wooteco.subway.map.dto.TransferLineDto;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.exception.StationAlreadyRegisteredInLineException;

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
        Line persistLine = lineDao
            .insert(new Line(request.getName(), request.getColor(), request.getExtraFare()));
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

    public List<LineDetailResponse> findLineDetailResponse() {
        List<Line> persistLines = findLines();
        return persistLines.stream()
            .map(line -> LineDetailResponse.of(line))
            .sorted(Comparator.comparing(LineDetailResponse::getName))
            .collect(Collectors.toList());
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

    public void checkRegisteredStation(Station station) {
        List<Line> lines = lineDao.findAll();

        lines.stream().filter(line -> line.getStations().contains(station)).findAny()
            .orElseThrow(
                () -> new StationAlreadyRegisteredInLineException("노선에 등록된 역은 삭제할 수 없습니다."));

    }

    public List<MapDetailResponse> showMap() {
        List<Line> lines = findLines();
        List<MapDetailResponse> mapDetailResponses = new ArrayList<>();

        for (Line line : lines) {
            mapDetailResponses.add(
                new MapDetailResponse(line.getId(),
                    line.getName(),
                    line.getColor(),
                    line.getSections().getTotalDistance(),
                    createStationsFromLine(line)));
        }
        return mapDetailResponses.stream().sorted(Comparator.comparing(MapDetailResponse::getName))
            .collect(Collectors.toList());
    }

    private List<MapSectionDto> createStationsFromLine(Line line) {
        List<MapSectionDto> mapSectionDtos = new ArrayList<MapSectionDto>();

        for (Section section : line.getSections().getSections()) {
            mapSectionDtos.add(
                new MapSectionDto(section.getUpStation().getId(), section.getUpStation().getName(),
                    section.getDistance(),
                    createTransferLines(line.getId(), section.getUpStation()))
            );
        }
        Section lastSection = line.getSections().getSections()
            .get(line.getSections().getSections().size() - 1);
        mapSectionDtos.add(
            new MapSectionDto(lastSection.getDownStation().getId(),
                lastSection.getDownStation().getName(), 0,
                createTransferLines(line.getId(), lastSection.getDownStation())));

        return mapSectionDtos;
    }

    private List<TransferLineDto> createTransferLines(Long lineId, Station upStation) {
        List<Long> transferLineIdsBylineIdAndStation = lineDao
            .findTransFerLineIdsBylineIdAndStation(lineId, upStation);

        if (transferLineIdsBylineIdAndStation.isEmpty()) {
            return new ArrayList<TransferLineDto>();
        }

        return transferLineIdsBylineIdAndStation.stream()
            .map(relativeLineId -> lineDao.findById(relativeLineId))
            .map(line -> new TransferLineDto(line.getId(), line.getName(), line.getColor()))
            .collect(Collectors.toList());

    }

    public void checkRegisteredLine(Long id) {
        lineDao.checkHaveLine(id);
    }
}
