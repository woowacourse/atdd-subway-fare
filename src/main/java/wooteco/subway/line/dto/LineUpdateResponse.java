package wooteco.subway.line.dto;

import wooteco.subway.line.domain.Line;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class LineUpdateResponse {
    @NotNull(message = "입력되지 않은 항목을 확인해주세요")
    private Long id;
    @NotBlank(message = "입력되지 않은 항목을 확인해주세요")
    private String name;
    @NotBlank(message = "입력되지 않은 항목을 확인해주세요")
    private String color;

    public LineUpdateResponse(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public static LineUpdateResponse of(Line line) {
        return new LineUpdateResponse(line.getId(), line.getName(), line.getColor());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
