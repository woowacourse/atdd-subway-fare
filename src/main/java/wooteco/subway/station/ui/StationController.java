package wooteco.subway.station.ui;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.infrastructure.validate.StationValidator;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class StationController {
    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @InitBinder("stationRequest")
    private void initBind(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(new StationValidator());
    }

    @PostMapping("/stations")
    public ResponseEntity<StationResponse> createStation(@RequestBody @Valid StationRequest stationRequest, BindingResult bindingResult) {
        StationResponse station = stationService.saveStation(stationRequest);
        return ResponseEntity.created(URI.create("/stations/" + station.getId())).body(station);
    }

    @GetMapping(value = "/stations", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StationResponse>> showStations() {
        return ResponseEntity.ok().body(stationService.findAllStationResponses());
    }

    @PutMapping("/stations/{id}")
    public ResponseEntity<Void> updateStation(@PathVariable Long id, @RequestBody @Valid StationRequest stationRequest, BindingResult bindingResult) {
        stationService.updateStation(id, stationRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/stations/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        stationService.deleteStationById(id);
        return ResponseEntity.noContent().build();
    }
}
