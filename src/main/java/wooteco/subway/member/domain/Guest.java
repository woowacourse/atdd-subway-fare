package wooteco.subway.member.domain;

public class Guest implements User {

    private static final int ADULT_AGE = 20;

    @Override
    public Integer getAge() {
        return 20;
    }

    @Override
    public boolean isGuest() {
        return true;
    }
}
