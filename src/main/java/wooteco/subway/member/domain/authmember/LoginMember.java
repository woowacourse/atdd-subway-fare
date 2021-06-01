package wooteco.subway.member.domain.authmember;

import java.util.Arrays;
import java.util.List;
import wooteco.subway.exception.notfound.NotExistException;
import wooteco.subway.member.domain.agemember.AgeMember;
import wooteco.subway.member.domain.agemember.Baby;
import wooteco.subway.member.domain.agemember.Child;
import wooteco.subway.member.domain.agemember.Teenager;

public class LoginMember implements AuthMember {

    private static final List<AgeMember> ageMembers
        = Arrays.asList(new Baby(), new Child(), new Teenager());

    private Long id;
    private String email;
    private Integer age;

    public LoginMember() {
    }

    public LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
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

    public AgeMember distinguishAgeMember() {
        return ageMembers.stream()
            .filter(ageMember -> ageMember.isInAgeRange(age))
            .findAny()
            .orElseThrow(NotExistException::new);
    }

    @Override
    public int discountFareByAge(int fare) {
        return distinguishAgeMember().discountFareByAge(fare);
    }

    @Override
    public boolean isLoggedIn() {
        return true;
    }
}
