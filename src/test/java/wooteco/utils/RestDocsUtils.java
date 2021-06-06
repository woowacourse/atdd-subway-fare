package wooteco.utils;

import io.restassured.specification.RequestSpecification;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

public final class RestDocsUtils {
    private static RequestSpecification requestSpecification;

    private RestDocsUtils() {
    }

    public static void setRequestSpecification(RequestSpecification requestSpecification) {
        RestDocsUtils.requestSpecification = requestSpecification;
    }

    public static RequestSpecification getRequestSpecification() {
        return requestSpecification;
    }

    public static OperationRequestPreprocessor getRequestPreprocessor() {
        return preprocessRequest(
                modifyUris()
                        .scheme("https")
                        .host("test.com")
                        .removePort(),
                prettyPrint());
    }

    public static OperationResponsePreprocessor getResponsePreprocessor() {
        return preprocessResponse(prettyPrint());
    }
}