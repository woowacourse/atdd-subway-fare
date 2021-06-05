package wooteco.subway.line.dto;

public class LineRequest {
    private static final Long NONE_EXTRA_FARE = 0L;

    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private Long extraFare;

    public LineRequest() {
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
        this(name, color, upStationId, downStationId, distance, NONE_EXTRA_FARE);
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance, Long extraFare) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.extraFare = extraFare;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }

    public Long getExtraFare() {
        return extraFare;
    }
}
