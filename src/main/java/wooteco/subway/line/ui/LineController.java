package wooteco.subway.line.ui;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.dto.LineDetailResponse;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.SectionRequest;
import wooteco.subway.line.exception.InvalidDistanceException;
import wooteco.subway.line.exception.SameStationsInSameSectionException;
import wooteco.subway.map.dto.MapDetailResponse;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.ui.SubwayController;

@RestController
@RequestMapping("/api/lines")
public class LineController extends SubwayController {

    public LineController(StationService stationService,
        LineService lineService) {
        super(stationService, lineService);
    }

    @PostMapping
    public ResponseEntity createLine(@RequestBody @Valid LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/api/lines/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity<List<LineDetailResponse>> findAllLines() {
        return ResponseEntity.ok(lineService.findLineDetailResponse());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> findLineById(@PathVariable Long id) {
        lineService.checkRegisteredLine(id);
        return ResponseEntity.ok(lineService.findLineResponseById(id));
    }

    @GetMapping("/map")
    public ResponseEntity<List<MapDetailResponse>> showMap() {
        List<MapDetailResponse> mapDetailResponse = lineService.showMap();
        return ResponseEntity.ok().body(mapDetailResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateLine(@PathVariable Long id,
        @RequestBody LineRequest lineUpdateRequest) {
        lineService.checkRegisteredLine(id);
        lineService.updateLine(id, lineUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteLine(@PathVariable Long id) {
        lineService.checkRegisteredLine(id);
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{lineId}/sections")
    public ResponseEntity addLineStation(@PathVariable Long lineId,
        @RequestBody SectionRequest sectionRequest) {
        if (isSameBothStation(sectionRequest)) {
            throw new SameStationsInSameSectionException("같은 역 두 개가 올 수 없습니다.");
        }

        if (isWrongDistance(sectionRequest)) {
            throw new InvalidDistanceException("잘못된 거리값입니다.");
        }

        lineService.addLineStation(lineId, sectionRequest);
        return ResponseEntity.created(URI.create("/api/lines/" + lineId + "/sections"))
            .body(sectionRequest);
    }

    @DeleteMapping("/{lineId}/sections")
    public ResponseEntity removeLineStation(@PathVariable Long lineId,
        @RequestParam Long stationId) {
        lineService.removeLineStation(lineId, stationId);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity handleSQLException() {
        return ResponseEntity.badRequest().build();
    }

    private boolean isSameBothStation(SectionRequest sectionRequest) {
        return sectionRequest.getUpStationId().equals(sectionRequest.getDownStationId());
    }

    private boolean isWrongDistance(SectionRequest sectionRequest) {
        return sectionRequest.getDistance() <= 0;
    }
}
