package wooteco.subway.member.domain;

import wooteco.subway.line.domain.fare.AgeFarePolicy;

public final class LoginUser implements User {
    private Long id;
    private String email;
    private Integer age;

    public LoginUser() {
    }

    public LoginUser(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public Integer getAge() {
        return age;
    }

    @Override
    public AgeFarePolicy farePolicy() {
        return AgeFarePolicy.of(this);
    }

    @Override
    public boolean isOverThan(int age) {
        return this.age >= age;
    }
}
