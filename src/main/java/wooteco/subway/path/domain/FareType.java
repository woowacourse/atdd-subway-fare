package wooteco.subway.path.domain;

import wooteco.subway.member.domain.MemberType;

import java.util.Arrays;
import java.util.List;

public enum FareType {
    HUNDRED(1.0, MemberType.BABY),
    HALF(0.5, MemberType.CHILD),
    TWENTY(0.2, MemberType.ADOLESCENT),
    ZERO(0, MemberType.ADULT, MemberType.NONE);

    private List<MemberType> memberTypes;
    private double discountRate;

    FareType(double discountRate, MemberType... memberTypes){
        this.discountRate = discountRate;
        this.memberTypes = Arrays.asList(memberTypes);
    }

    public static FareType of(MemberType memberType){
        return Arrays.stream(values())
                .filter(fareType -> fareType.memberTypes.contains(memberType))
                .findFirst()
                .orElse(ZERO);
    }

    public int price(int fare){
        return (int)(fare * (1-discountRate));
    }
}
