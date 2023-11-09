package bdbe.bdbd._core.exception;

import bdbe.bdbd._core.utils.ApiUtils;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * HTTP 상태 코드 400 (Bad Request) : 잘못된 요청
 * 유효성 검사 실패 또는 잘못된 파라미터 요청시 발생합니다.
 */
@Getter
public class BadRequestError extends RuntimeException {

    public BadRequestError(String message) {
        super(message);
    }

    public ApiUtils.ApiResult<?> body(){
        return ApiUtils.error(getMessage(), HttpStatus.BAD_REQUEST);
    }

    public HttpStatus status(){
        return HttpStatus.BAD_REQUEST;
    }
}