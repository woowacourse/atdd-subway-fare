package wooteco.subway.member.domain;

public class GuestUser implements User {

    private static final int DEFAULT_GUEST_AGE = 20;

    private final int age;

    public GuestUser() {
        this.age = DEFAULT_GUEST_AGE;
    }

    @Override
    public boolean isGuest() {
        return true;
    }

    @Override
    public long getId() {
        throw new IllegalArgumentException("Guest의 정보는 불러올 수 없습니다.");
    }

    @Override
    public String getEmail() {
        throw new IllegalArgumentException("Guest의 정보는 불러올 수 없습니다.");
    }

    @Override
    public int getAge() {
        return age;
    }
}
