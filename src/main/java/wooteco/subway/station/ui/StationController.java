package wooteco.subway.station.ui;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

@RestController
@RequestMapping("/api/stations")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class StationController {

    private StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping("")
    public ResponseEntity<StationResponse> createStation(
        @RequestBody @Valid StationRequest stationRequest) {
        StationResponse station = stationService.saveStation(stationRequest);
        return ResponseEntity.created(URI.create("/stations/" + station.getId())).body(station);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StationResponse>> showStations() {
        return ResponseEntity.ok().body(stationService.findAllStationResponses());
    }

    @PutMapping("/{id}")
    public ResponseEntity<StationResponse> updateStation(@PathVariable Long id,
        @RequestBody @Valid StationRequest stationRequest) {
        StationResponse station = stationService.updateStation(id, stationRequest);
        return ResponseEntity.ok(station);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteStation(@PathVariable Long id) {
        stationService.deleteStationById(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity handleSQLException() {
        return ResponseEntity.badRequest().build();
    }
}
