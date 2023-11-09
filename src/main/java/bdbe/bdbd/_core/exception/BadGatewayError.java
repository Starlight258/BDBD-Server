package bdbe.bdbd._core.exception;

import bdbe.bdbd._core.utils.ApiUtils;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * HTTP 상태 코드 502 (Bad Gateway)
 * 게이트웨이나 프록시 역할을 하는 서버가 상위 서버로부터 잘못된 응답을 받았을 때 발생합니다.
 */
@Getter
public class BadGatewayError extends RuntimeException {
    public BadGatewayError(String message) {
        super(message);
    }

    public ApiUtils.ApiResult<?> body(){
        return ApiUtils.error(getMessage(), HttpStatus.BAD_GATEWAY);
    }

    public HttpStatus status(){
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}