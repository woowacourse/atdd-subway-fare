package wooteco.subway.member;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wooteco.subway.member.ui.MemberTypeProducerResolver;

import java.util.List;

@Configuration
public class MemberTypeProducerConfig implements WebMvcConfigurer {

    private MemberTypeProducerResolver memberTypeProducerResolver;

    public MemberTypeProducerConfig(MemberTypeProducerResolver memberTypeProducerResolver) {
        this.memberTypeProducerResolver = memberTypeProducerResolver;
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(memberTypeProducerResolver);
    }
}
