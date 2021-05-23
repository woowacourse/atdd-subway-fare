package wooteco.subway.line.dto;

public class SectionAddResponse {
    private Long upStationId;
    private Long downStationId;
    private Integer distance;

    public SectionAddResponse(Long upStationId, Long downStationId, Integer distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Integer getDistance() {
        return distance;
    }
}
