package bdbe.bdbd._core.exception;


import bdbe.bdbd._core.utils.ApiUtils;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * HTTP 상태 코드 404 (Not Found)
 * 리소스 찾을 수 없을 때 발생합니다.
 */
@Getter
public class NotFoundError extends RuntimeException {
    public NotFoundError(String message) {
        super(message);
    }

    public ApiUtils.ApiResult<?> body(){
        return ApiUtils.error(getMessage(), HttpStatus.NOT_FOUND);
    }

    public HttpStatus status(){
        return HttpStatus.NOT_FOUND;
    }
}