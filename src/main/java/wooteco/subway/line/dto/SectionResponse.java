package wooteco.subway.line.dto;

import java.util.List;

public class SectionResponse {
    private Long id;
    private String name;
    private int distanceToNextStation;
    private List<TransferLineResponse> transferLines;

    public SectionResponse() {
    }

    public SectionResponse(Long id, String name, int distanceToNextStation,
        List<TransferLineResponse> transferLines) {
        this.id = id;
        this.name = name;
        this.distanceToNextStation = distanceToNextStation;
        this.transferLines = transferLines;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDistanceToNextStation() {
        return distanceToNextStation;
    }

    public List<TransferLineResponse> getTransferLines() {
        return transferLines;
    }
}
