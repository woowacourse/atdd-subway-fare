package wooteco.subway.member.dto;

public class ChangeAgeResponse {

    private Long id;
    private Integer age;

    public ChangeAgeResponse() {
    }

    public ChangeAgeResponse(Long id, Integer age) {
        this.id = id;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public Integer getAge() {
        return age;
    }
}
