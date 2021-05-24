package wooteco.subway.member.domain;

public class Age {

    private final int age;

    public Age(int age) {
        this.age = age;
    }

    public int getDiscountedFare(int fare) {
        if (age >= 19) {
            return fare;
        }

        if (age >= 13) {
            return (int) ((fare - 350) * 0.8);
        }

        if (age >= 6) {
            return (int) ((fare - 350) * 0.5);
        }

        return 0;
    }

    public int toInt() {
        return age;
    }
}
