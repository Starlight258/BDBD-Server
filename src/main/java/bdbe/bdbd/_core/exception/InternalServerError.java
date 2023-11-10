package bdbe.bdbd._core.exception;


import bdbe.bdbd._core.utils.ApiUtils;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * HTTP 상태 코드 500 (Internal Server Error) : 내부 서버 오류
 * 서버에 에러가 발생할 때 발생합니다.
 */
@Getter
public class InternalServerError extends ApiException {

    public InternalServerError(ErrorCode errorCode, Object errors) {
        super(errorCode, errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public enum ErrorCode implements ApiException.ErrorCode {
        INTERNAL_SERVER_ERROR(2000, "Server Error");

        private final int code;
        private final String message;

        ErrorCode(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }
}
