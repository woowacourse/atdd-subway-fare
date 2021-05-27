package wooteco.subway.map.ui;


import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.line.application.LineService;
import wooteco.subway.map.dto.MapDetailResponse;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MapController {

    private LineService lineService;

    public MapController(LineService lineService) {
        this.lineService = lineService;
    }

    @GetMapping("/api/map")
    public ResponseEntity<List<MapDetailResponse>> showMap() {
        List<MapDetailResponse> mapDetailResponse = lineService.showMap();
        return ResponseEntity.ok().body(mapDetailResponse);
//        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }
}
