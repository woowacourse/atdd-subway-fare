package wooteco.subway.line.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.dto.LineResponse;

import java.util.List;

@RestController
public class MapController {

    private final LineService lineService;

    public MapController(LineService lineService) {
        this.lineService = lineService;
    }

    @GetMapping("/maps")
    public ResponseEntity<List<LineResponse>> findAllLines() {
        List<LineResponse> lineResponses = lineService.findLineResponses();
        return ResponseEntity.ok(lineResponses);
    }
}
