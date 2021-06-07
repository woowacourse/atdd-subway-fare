package wooteco.subway.member.domain;

public class LoginMember {
    private Long id;
    private String email;
    private Integer age;

    public LoginMember() {
        age = 0;
    }

    public LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public LoginMember(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.age = member.getAge();
    }

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
