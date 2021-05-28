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

    private LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity createLine(@RequestBody @Valid LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/api/lines/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity<List<LineWithTransferLinesAndStationsResponse>> findAllLines() {
        return ResponseEntity.ok(lineService.findLineResponses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineWithTransferLinesAndStationsResponse> findLineById(@PathVariable Long id) {
        return ResponseEntity.ok(lineService.findLineResponseById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity updateLine(@PathVariable Long id, @RequestBody @Valid LineRequest lineUpdateRequest) {
        LineUpdateResponse response = lineService.updateLine(id, lineUpdateRequest);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteLine(@PathVariable Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{lineId}/sections")
    public ResponseEntity addLineStation(@PathVariable Long lineId, @RequestBody @Valid SectionRequest sectionRequest) {
        SectionResponse sectionResponse = lineService.addLineStation(lineId, sectionRequest);
        return ResponseEntity.ok().body(sectionResponse);
    }

    @DeleteMapping("/{lineId}/sections")
    public ResponseEntity removeLineStation(@PathVariable Long lineId, @RequestParam Long stationId) {
        lineService.removeLineStation(lineId, stationId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity handleSQLException() {
        return ResponseEntity.badRequest().build();
    }
}
