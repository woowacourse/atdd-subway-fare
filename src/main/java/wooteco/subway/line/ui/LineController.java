package wooteco.subway.line.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.SectionRequest;
import wooteco.subway.line.dto.SimpleLineResponse;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody @Valid LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        URI location = URI.create("/lines/" + line.getId());
        return ResponseEntity.created(location)
                .body(line);
    }

    @GetMapping
    public ResponseEntity<List<SimpleLineResponse>> findAllLines() {
        List<SimpleLineResponse> simpleLineResponses = lineService.findSimpleLineResponses();
        return ResponseEntity.ok(simpleLineResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> findLineById(@PathVariable Long id) {
        LineResponse lineResponse = lineService.findLineResponseById(id);
        return ResponseEntity.ok(lineResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable Long id, @RequestBody @Valid LineRequest lineUpdateRequest) {
        lineService.updateLine(id, lineUpdateRequest);
        return ResponseEntity.ok()
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent()
                .build();
    }

    @PostMapping("/{lineId}/sections")
    public ResponseEntity<Void> addLineStation(@PathVariable Long lineId, @RequestBody SectionRequest sectionRequest) {
        lineService.addLineStation(lineId, sectionRequest);
        return ResponseEntity.ok()
                .build();
    }

    @DeleteMapping("/{lineId}/sections")
    public ResponseEntity<Void> removeLineStation(@PathVariable Long lineId, @RequestParam Long stationId) {
        lineService.removeLineStation(lineId, stationId);
        return ResponseEntity.ok()
                .build();
    }
}
