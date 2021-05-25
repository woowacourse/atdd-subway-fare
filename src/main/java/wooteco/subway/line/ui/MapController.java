package wooteco.subway.line.ui;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.dto.MapResponse;

@RestController
@RequestMapping("/map")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MapController {

    private final LineService lineService;

    public MapController(LineService lineService) {
        this.lineService = lineService;
    }

    @GetMapping
    public ResponseEntity<List<MapResponse>> findMap() {
        return ResponseEntity.ok(lineService.findMapResponse());
    }
}

