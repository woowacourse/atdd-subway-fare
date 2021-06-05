package wooteco.subway.station.dto;

import java.util.List;

public class StationWithTransferResponse {
    private Long id;
    private String name;
    private List<String> transfer;

    public StationWithTransferResponse() {
    }

    public StationWithTransferResponse(Long id, String name, List<String> transfer) {
        this.id = id;
        this.name = name;
        this.transfer = transfer;
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
