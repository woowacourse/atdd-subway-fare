package wooteco.subway.member.domain;

public class LoginMember implements Member {
    private Long id;
    private String email;
    private Integer age;

    public LoginMember() {
    }

    public LoginMember(Long id, String email, Integer age) {
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
    public int calculateFare(int fare) {
        return AgeDiscountPolicy.of(age).calculateFare(fare);
    }
}
