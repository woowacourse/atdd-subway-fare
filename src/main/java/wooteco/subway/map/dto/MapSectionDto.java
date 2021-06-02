package wooteco.subway.map.dto;

import java.util.List;

public class MapSectionDto {

    private Long id;
    private String name;
    private int distanceToNextStation;
    private List<TransferLineDto> transferLines;

    public MapSectionDto() {

    }

    public MapSectionDto(Long id, String name, int distanceToNextStation,
        List<TransferLineDto> transferLines) {
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

    public List<TransferLineDto> getTransferLines() {
        return transferLines;
    }
}
