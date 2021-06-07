package wooteco.subway.member.domain;

import wooteco.subway.member.application.MemberException;

public class User {

    public static final User GUEST = new Guest();

    private Long id;
    private String email;
    private Integer age;

    public User() {
    }

    public User(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public boolean isLoggedIn(){
        return true;
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

    private static class Guest extends User{
        @Override
        public boolean isLoggedIn() {
            return false;
        }
    }
}
