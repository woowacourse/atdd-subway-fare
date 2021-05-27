package wooteco.subway.line.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.dto.*;
import wooteco.subway.validate.LineValidator;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LineController {
    private LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @InitBinder("lineRequest")
    private void initBind(WebDataBinder webDataBinder) {
        webDataBinder .addValidators(new LineValidator());
    }

    @PostMapping
    public ResponseEntity createLine(@RequestBody @Valid LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> findAllLines() {
        return ResponseEntity.ok(lineService.findLineResponses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> findLineById(@PathVariable Long id) {
        return ResponseEntity.ok(lineService.findLineResponseById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity updateLine(@PathVariable Long id, @RequestBody @Valid LineRequest lineUpdateRequest) {
        lineService.updateLine(id, lineUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteLine(@PathVariable Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{lineId}/sections")
    public ResponseEntity addLineStation(@PathVariable Long lineId, @RequestBody SectionRequest sectionRequest) {
        SectionResponse sectionResponse = lineService.addLineStation(lineId, sectionRequest);
        return ResponseEntity.created(URI.create("/" + lineId + "/sections")).body(sectionResponse);
    }

    @DeleteMapping("/{lineId}/sections")
    public ResponseEntity removeLineStation(@PathVariable Long lineId, @RequestParam Long stationId) {
        lineService.removeLineStation(lineId, stationId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/map")
    public ResponseEntity<List<MapResponse>> findAllLine() {
        List<Line> lines = lineService.findLines();
        return ResponseEntity.ok(MapResponse.listOf(lines));
    }
}

