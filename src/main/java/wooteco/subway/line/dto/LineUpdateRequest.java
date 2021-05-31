package wooteco.subway.line.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public class LineUpdateRequest {
    @NotBlank(message = "이름에 빈 문자열 또는 공백만 있을 수 없습니다.")
    @Length(min = 2, max = 10, message = "노선 이름은 2글자 이상 10글자 이하여야합니다.")
    @Pattern(regexp = "^[가-힣|0-9]*$", message = "노선 이름은 한글 또는 숫자여야 합니다.")
    private String name;

    @NotBlank(message = "색깔에 빈 문자열 또는 공백만 있을 수 없습니다.")
    private String color;

    public LineUpdateRequest() {
    }

    public LineUpdateRequest(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

}
