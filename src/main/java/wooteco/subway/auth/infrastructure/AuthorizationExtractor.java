package wooteco.subway.auth.infrastructure;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import wooteco.subway.exception.AuthorizationException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Stream;

public class AuthorizationExtractor {
    public static final String AUTHORIZATION = "Authorization";
    public static String BEARER_TYPE = "Bearer";
    public static final String ACCESS_TOKEN_TYPE = AuthorizationExtractor.class.getSimpleName() + ".ACCESS_TOKEN_TYPE";

    public static String extract(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaders(AUTHORIZATION);

        String token = findAuthorizationHeader(headers)
                .orElseThrow(AuthorizationException::new);

        request.setAttribute(ACCESS_TOKEN_TYPE, BEARER_TYPE);

        return token;
    }

    public static boolean isExtractable(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaders(AUTHORIZATION);

        return findAuthorizationHeader(headers)
            .isPresent();
    }

    private static Optional<String> findAuthorizationHeader(Enumeration<String> headers) {
        return streamOf(headers)
            .filter(AuthorizationExtractor::isBearerType)
            .map(AuthorizationExtractor::extractBearerTokenFromHeader)
            .findAny();
    }

    private static Stream<String> streamOf(Enumeration<String> headers) {
        List<String> list = new ArrayList<>();

        while (headers.hasMoreElements()) {
            list.add(headers.nextElement());
        }

        return list.stream();
    }

    private static boolean isBearerType(String header) {
        return StringUtils.startsWithIgnoreCase(header, BEARER_TYPE);
    }

    private static String extractBearerTokenFromHeader(String header) {
        String authHeaderValue = header.substring(BEARER_TYPE.length()).trim();

        int commaIndex = authHeaderValue.indexOf(',');
        if (commaIndex > 0) {
            authHeaderValue = authHeaderValue.substring(0, commaIndex);
        }

        return authHeaderValue;
    }

}

