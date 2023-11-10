package bdbe.bdbd._core.exception;

import bdbe.bdbd._core.utils.ApiUtils;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class ApiException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Object errors;
    private final HttpStatus status;

    public ApiException(ErrorCode errorCode, Object errors, HttpStatus status) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.errors = errors;
        this.status = status;
    }

    public ApiUtils.ApiResult<?> body() {
        return ApiUtils.error(
                String.valueOf(this.getStatus().value()),
                String.valueOf(this.errorCode.getCode()),
                this.getMessage()
        );
    }

    public interface ErrorCode {
        int getCode();
        String getMessage();
    }
}
