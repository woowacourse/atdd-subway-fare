package wooteco.subway.line.dto;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class LineRequest {
    private static final Long NONE_EXTRA_FARE = 0L;

    @Pattern(regexp ="^[ㄱ-ㅎ|가-힣|a-z|A-Z|0-9|]+$" , message = "지원되지 않는 언어, 공백, 특수문자는 입력 불가능합니다.")
    @Size(min = 2, max = 10, message = "노선의 전체 글자 수는 2자 이상 10자 이하여야 합니다.")
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
