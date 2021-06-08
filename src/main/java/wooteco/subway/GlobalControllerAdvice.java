package wooteco.subway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.auth.exception.AuthorizationException;
import wooteco.subway.dto.BindErrorResponse;
import wooteco.subway.dto.ErrorResponse;
import wooteco.subway.line.exception.LineDaoException;
import wooteco.subway.line.exception.LineDomainException;
import wooteco.subway.member.exception.MemberDaoException;
import wooteco.subway.path.exception.PathDomainException;
import wooteco.subway.station.exception.StationDaoException;
import wooteco.subway.station.exception.StationDomainException;

import java.sql.SQLException;

@ControllerAdvice
public class GlobalControllerAdvice {
    Logger logger = LoggerFactory.getLogger(GlobalControllerAdvice.class);

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationException(AuthorizationException e) {
        logger.error(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> resolveRequest(BindException e) {
        logger.error(e.getMessage(), e);
        return ResponseEntity
                .badRequest()
                .body(BindErrorResponse.from("인자값을 검증하는 과정에서 오류가 발생했습니다.", e.getBindingResult()));
    }

    @ExceptionHandler(MemberDaoException.class)
    public ResponseEntity<ErrorResponse> handleMemberException(BindException e) {
        logger.error(e.getMessage(), e);
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(value = {StationDaoException.class, StationDomainException.class})
    public ResponseEntity<ErrorResponse> handleStationException(Exception e) {
        logger.error(e.getMessage(), e);
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(value = {LineDaoException.class, LineDomainException.class})
    public ResponseEntity<ErrorResponse> handleLineException(Exception e) {
        logger.error(e.getMessage(), e);
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(PathDomainException.class)
    public ResponseEntity<ErrorResponse> handlePathException(PathDomainException e) {
        logger.error(e.getMessage(), e);
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ErrorResponse> handleSQLException(SQLException e) {
        logger.error(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("예상치 못한 DB 예외가 발생했습니다."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception e) {
        logger.error(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("예상치 못한 예외가 발생했습니다."));
    }
}
