package wooteco.subway.line.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.dto.*;

import javax.validation.Valid;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/lines")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@Valid @RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/api/lines/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity<List<LineWithTransferLinesAndNextDistanceResponse>> findAllLines() {
        return ResponseEntity.ok(lineService.findLinesWithTransferLinesAndNextDistance());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineWithTransferLinesAndNextDistanceResponse> findLineById(@PathVariable Long id) {
        return ResponseEntity.ok(lineService.findLineWithTransferLinesAndNextDistance(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LineUpdateResponse> updateLine(@PathVariable Long id,
                                                         @Valid @RequestBody LineUpdateRequest lineUpdateRequest) {
        LineUpdateResponse lineUpdateResponse = lineService.updateLine(id, lineUpdateRequest);
        return ResponseEntity.ok(lineUpdateResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{lineId}/sections")
    public ResponseEntity<SectionAddResponse> addLineStation(@PathVariable Long lineId,
                                                             @Valid @RequestBody SectionRequest sectionRequest) {
        SectionAddResponse sectionAddResponse = lineService.addLineStation(lineId, sectionRequest);
        return ResponseEntity.created(URI.create("/api/lines" + lineId + "/sections")).body(sectionAddResponse);
    }

    @DeleteMapping("/{lineId}/sections")
    public ResponseEntity<Void> removeLineStation(@PathVariable Long lineId,
                                                  @RequestParam Long stationId) {
        lineService.removeLineStation(lineId, stationId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Void> handleSQLException() {
        return ResponseEntity.badRequest().build();
    }
}
