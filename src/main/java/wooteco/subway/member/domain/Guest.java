package wooteco.subway.member.domain;

public class Guest implements User {

    @Override
    public Long getId() {
        throw new IllegalStateException("다시 로그인 후 시도해주세요");
    }

    @Override
    public String getEmail() {
        throw new IllegalStateException("다시 로그인 후 시도해주세요");
    }

    @Override
    public String getPassword() {
        throw new IllegalStateException("다시 로그인 후 시도해주세요");
    }

    @Override
    public Integer getAge() {
        throw new IllegalStateException("다시 로그인 후 시도해주세요");
    }

    public boolean isGuest() {
        return true;
    }
}
