package wooteco.subway.line.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.ui.dto.map.MapResponse;

import java.util.List;

@RestController
@RequestMapping("/map")
public class MapController {

    private final LineService lineService;

    public MapController(LineService lineService) {
        this.lineService = lineService;
    }

    @GetMapping
    public ResponseEntity<List<MapResponse>> showMap() {
        return ResponseEntity.ok(lineService.findMap());
    }

}
