package wooteco.subway.auth.infrastructure;

import wooteco.subway.auth.application.AuthorizationException;

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

        String token = streamOf(headers)
                .filter(AuthorizationExtractor::isBearerType)
                .map(AuthorizationExtractor::extractBearerTokenFromHeader)
                .findAny()
                .orElseThrow(AuthorizationException::new);

        request.setAttribute(ACCESS_TOKEN_TYPE, BEARER_TYPE);

        return token;
    }

    private static Stream<String> streamOf(Enumeration<String> headers) {
        List<String> list = new ArrayList<>();

        while (headers.hasMoreElements()) {
            list.add(headers.nextElement());
        }

        return list.stream();
    }

    private static boolean isBearerType(String header) {
        return header.toLowerCase().startsWith(BEARER_TYPE.toLowerCase());
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

