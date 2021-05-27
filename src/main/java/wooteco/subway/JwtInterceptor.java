package wooteco.subway;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.exception.InvalidTokenException;

@Component
public class JwtInterceptor extends HandlerInterceptorAdapter {

    Logger logger = LoggerFactory.getLogger("io.ojw.mall.interceptor.Jwtinterceptor");

    private static final String TOKEN = "jwt-token";

    private AuthService authService;

    public JwtInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler)
        throws Exception {
        final String token = request.getHeader(TOKEN);

        logger.debug("Jwtinterceptor > preHandle > token: " + token);

        if (StringUtils.equals(request.getMethod(), "OPTIONS")) {
            logger.debug("if request options method is options, return true");

            return true;
        }

        if (token != null && authService.isValidToken(token)) {
            return true;
        } else {
            throw new InvalidTokenException("잘못된 토큰입니다.");
        }
    }
}

