package wooteco.subway.line.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.dto.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@Valid @RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> findAllLines() {
        return ResponseEntity.ok(lineService.findLineResponses());
    }

    @GetMapping("/detail")
    public ResponseEntity<List<LineDetailResponse>> findAllLinesDetail() {
        return ResponseEntity.ok(lineService.findLineDetailResponses());
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<LineDetailResponse> findLineDetailById(@PathVariable Long id) {
        return ResponseEntity.ok(lineService.findLineDetailResponseById(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> findLineById(@PathVariable Long id) {
        return ResponseEntity.ok(lineService.findLineResponseById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable Long id, @RequestBody @Valid LineRequest lineUpdateRequest) {
        lineService.updateLine(id, lineUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{lineId}/sections")
    public ResponseEntity<LineResponse> addLineStation(@PathVariable Long lineId, @RequestBody SectionRequest sectionRequest) {
        LineResponse line = lineService.addLineStation(lineId, sectionRequest);
        return ResponseEntity.created(URI.create("/lines/" + lineId)).body(line);
    }

    @DeleteMapping("/{lineId}/sections")
    public ResponseEntity<Void> removeLineStation(@PathVariable Long lineId, @ModelAttribute @Valid StationRemoveRequest stationRemoveRequest) {
        lineService.removeLineStation(lineId, stationRemoveRequest.getStationId());
        return ResponseEntity.noContent().build();
    }
}
