package wooteco.subway.station.dto;

import java.util.List;
import wooteco.subway.station.domain.Station;

public class StationTransferResponse {
    private Long id;
    private String name;
    private List<String> transfer;

    public StationTransferResponse() {

    }

    public StationTransferResponse(Long id, String name, List<String> transfer) {
        this.id = id;
        this.name = name;
        this.transfer = transfer;
    }

    public static StationTransferResponse from(Station station, List<String> transfer) {
        return new StationTransferResponse(station.getId(), station.getName(), transfer);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getTransfer() {
        return transfer;
    }
}
