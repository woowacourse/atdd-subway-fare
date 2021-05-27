package wooteco.subway.member.domain;

public enum MinimumAge {
    ADULT(19),
    TEENAGER(13),
    CHILD(6);

    private final int value;

    MinimumAge(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
