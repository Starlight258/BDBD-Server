package bdbe.bdbd._core.exception;


import bdbe.bdbd._core.utils.ApiUtils;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * HTTP 상태 코드 500 (Internal Server Error) : 내부 서버 오류
 * 서버에 에러가 발생할 때 발생합니다.
 */
@Getter
public class InternalServerError extends RuntimeException {
    public InternalServerError(String message) {
        super(message);
    }

    public ApiUtils.ApiResult<?> body(){
        return ApiUtils.error(getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public HttpStatus status(){
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
