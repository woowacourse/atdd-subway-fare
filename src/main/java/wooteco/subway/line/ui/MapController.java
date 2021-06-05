package wooteco.subway.line.ui;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.dto.LineResponse;

import java.util.List;

@RestController
@RequestMapping("/map")
public class MapController {

    private final LineService lineService;

    public MapController(LineService lineService) {
        this.lineService = lineService;
    }

    @GetMapping
    @ApiOperation(value = "노선, 구간, 역 전체 조회")
    public ResponseEntity<List<LineResponse>> map() {
        return ResponseEntity.ok(lineService.findLinesWithSections());
    }
}
