package wooteco.subway.line.ui;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
        webDataBinder.addValidators(new LineValidator());
    }

    @ApiOperation(value = "노선 생성", notes = "노선을 생성한다.")
    @PostMapping
    public ResponseEntity createLine(@RequestBody @Valid LineRequest lineRequest, BindingResult bindingResult) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @ApiOperation(value = "노선 조회", notes = "모든 노선을 조회한다.")
    @GetMapping
    public ResponseEntity<List<LineResponse>> findAllLines() {
        return ResponseEntity.ok(lineService.findLineResponses());
    }

    @ApiOperation(value = "노선 조회", notes = "노선을 조회한다.")
    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> findLineById(@PathVariable Long id) {
        return ResponseEntity.ok(lineService.findLineResponseById(id));
    }

    @ApiOperation(value = "노선 수정", notes = "노선을 수정한다.")
    @PutMapping("/{id}")
    public ResponseEntity updateLine(@PathVariable Long id, @RequestBody @Valid LineRequest lineUpdateRequest, BindingResult bindingResult) {
        lineService.updateLine(id, lineUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "노선 삭제", notes = "노선을 삭제한다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "구간 추가", notes = "노선에 구간을 추가한다.")
    @PostMapping("/{lineId}/sections")
    public ResponseEntity<SectionResponse> addLineStation(@PathVariable Long lineId, @RequestBody SectionRequest sectionRequest) {
        SectionResponse sectionResponse = lineService.addLineStation(lineId, sectionRequest);
        return ResponseEntity.ok(sectionResponse);
    }

    @ApiOperation(value = "구간 삭제", notes = "노선에 구간을 삭제한다.")
    @DeleteMapping("/{lineId}/sections")
    public ResponseEntity<Void> removeLineStation(@PathVariable Long lineId, @RequestParam Long stationId) {
        lineService.removeLineStation(lineId, stationId);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "노선 조회", notes = "모든 노선을 조회한다.")
    @GetMapping("/map")
    public ResponseEntity<List<MapResponse>> findAllLine() {
        List<Line> lines = lineService.findLines();
        return ResponseEntity.ok(MapResponse.listOf(lines));
    }
}

