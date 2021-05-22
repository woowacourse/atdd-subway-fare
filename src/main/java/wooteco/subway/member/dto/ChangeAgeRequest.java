package wooteco.subway.member.dto;

public class ChangeAgeRequest {

    private int age;

    public ChangeAgeRequest() {
    }

    public ChangeAgeRequest(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }
}
