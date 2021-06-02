package wooteco.subway.member.domain;

public abstract class RequestUser {
    private Long id;
    private String email;
    private Integer age;

    public RequestUser() {
    }

    public RequestUser(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public abstract boolean isAnonymous();

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }
}
