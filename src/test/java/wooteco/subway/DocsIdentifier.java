package wooteco.subway;

public class DocsIdentifier {
    public static String AUTH_LOGIN_FAIL = "auth/login/post/fail";
    public static String AUTH_LOGIN_SUCCESS = "auth/login/post/success/";


    public static String MEMBERS_POST_SUCCESS = "members/post/success/";
    public static String MEMBERS_POST_FAIL_EMAIL = "members/post/fail/email";
    public static String MEMBERS_POST_FAIL_AGE = "members/post/fail/age";
    public static String MEMBERS_POST_FAIL_PASSWORD = "members/post/fail/password";


    public static String MEMBERS_ME_GET = "members/me/get/";
    public static String MEMBERS_ME_GET_WRONG_TOKEN = "members/me/get/wrong_token/";
    public static String MEMBERS_ME_GET_CHECK_VALIDATION_SUCCESS = "members/get/check_validation/success/";
    public static String MEMBERS_ME_GET_CHECK_VALIDATION_FAIL = "members/get/check_validation/fail/";

    public static String MEMBERS_ME_PUT_SUCCESS = "members/me/put/success";
    public static String MEMBERS_ME_PUT_FAIL_EMAIL = "members/me/put/fail/email";
    public static String MEMBERS_ME_PUT_FAIL_AGE = "members/me/put/fail/age";
    public static String MEMBERS_ME_PUT_FAIL_PASSWORD = "members/me/put/fail/password";

    public static String MEMBERS_ME_DELETE = "members/me/delete/";


    public static String LINES_GET = "lines/get/";
    public static String LINES_GET_MAP = "lines/get/map";
    public static String LINES_GET_BY_ID = "lines/get/id";

    public static String LINES_POST_SUCCESS = "lines/post/success/";
    public static String LINES_POST_FAIL_NAME_LENGTH = "lines/post/fail/name/length";
    public static String LINES_POST_FAIL_NAME_BLANK = "lines/post/fail/name/blank/";
    public static String LINES_POST_FAIL_DUPLICATED = "lines/post/fail/duplicated/";

    public static String LINES_PUT_SUCCESS = "lines/put/success";
    public static String LINES_PUT_FAIL_NOT_EXIST = "lines/put/fail/not_exist/";
    public static String LINES_PUT_FAIL_DUPLICATED = "lines/put/fail/duplicated/";
    public static String LINES_PUT_FAIL_NAME_LENGTH = "lines/put/fail/name/length";
    public static String LINES_PUT_FAIL_NAME_BLANK = "lines/put/fail/name/blank";

    public static String LINES_DELETE_SUCCESS = "lines/delete/success";
    public static String LINES_DELETE_FAIL = "lines/delete/fail";

    public static String SECTIONS_POST_SUCCESS = "sections/post/success";
    public static String SECTIONS_POST_FAIL_EVERY = "sections/post/fail/every";
    public static String SECTIONS_POST_FAIL_ANY = "sections/post/fail/any";

    public static String SECTIONS_DELETE_SUCCESS = "sections/delete/success";
    public static String SECTIONS_DELETE_FAIL = "sections/delete/fail";

    public static String PATHS_GET = "paths/get/";
    public static String PATHS_GET_TOKEN = "paths/get/token/";

    public static String STATIONS_GET = "stations/get";

    public static String STATIONS_POST_SUCCESS = "stations/post/success";
    public static String STATIONS_POST_FAIL_NAME_BLANK = "stations/post/fail/name/blank";
    public static String STATIONS_POST_FAIL_NAME_LENGTH = "stations/post/fail/name/length";
    public static String STATIONS_POST_FAIL_DUPLICATED = "stations/post/fail/duplicated";

    public static String STATIONS_PUT_SUCCESS = "stations/put/success";
    public static String STATIONS_PUT_FAIL_DUPLICATED = "stations/put/fail/duplicated";
    public static String STATIONS_PUT_FAIL_NAME_BLANK = "stations/put/fail/name/blank";
    public static String STATIONS_PUT_FAIL_NAME_LENGTH = "stations/put/fail/name/length";

    public static String STATIONS_DELETE_SUCCESS = "stations/delete/success";
    public static String STATIONS_DELETE_FAIL = "stations/delete/fail";




}
