package wooteco.subway.member.domain;

public class Guest extends Member {
    private static final Long GUEST_ID = 0L;
    private static final String GUEST_EMAIL = "guest";
    private static final Integer GUEST_AGE = 20;

    public Guest() {
        super(GUEST_ID, GUEST_EMAIL, GUEST_AGE);
    }

    @Override
    public Long getId() {
        return GUEST_ID;
    }

    @Override
    public String getEmail() {
        return GUEST_EMAIL;
    }

    @Override
    public Integer getAge() {
        return GUEST_AGE;
    }
}
