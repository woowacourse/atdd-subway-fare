package wooteco.subway.member.domain;

public class LoginMember implements AuthMember {
    private Long id;
    private String email;
    private Age age;

    public LoginMember() {
    }

    public LoginMember(Member member) {
        this(member.getId(), member.getEmail(), member.getAge());
    }

    public LoginMember(Long id, String email, Integer age) {
        this(id, email, new Age(age));
    }

    public LoginMember(Long id, String email, Age age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    @Override
    public boolean isLoggedIn() {
        return true;
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
        return age.toInt();
    }

    @Override
    public int getDiscountedFare(int fare) {
        return age.getDiscountedFare(fare);
    }
}
