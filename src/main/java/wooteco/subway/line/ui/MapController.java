package wooteco.subway.line.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.dto.LineMapResponse;

import java.util.List;

@Controller
@RequestMapping("/map")
public class MapController {
    private LineService lineService;

    public MapController(final LineService lineService) {
        this.lineService = lineService;
    }

    @GetMapping
    public ResponseEntity<List<LineMapResponse>> findAllLinesForMap() {
        List<LineMapResponse> body = lineService.findLinesForMap();
        return ResponseEntity.ok().body(body);
    }
}
