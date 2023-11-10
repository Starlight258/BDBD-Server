package bdbe.bdbd._core.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class ApiUtils {

    public static <T> ApiResult<T> success(T response) {
        return new ApiResult<>(true, response, null);
    }

    public static ApiResult<?> error(String status, String code, Map<String, String> message) {
        return new ApiResult<>(false, null, new ApiError(status, code, message));
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ApiResult<T> {
        private final boolean success;
        private final T response;
        private final ApiError error;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ApiError {
        private final String status;
        private final String code;
        private final Map<String, String> message; // 필드 이름 변경

        public ApiError(String status, String code, String message) {
            this.status = status;
            this.code = code;
            this.message = new HashMap<>();
            this.message.put("defaultMessage", message); // 기본 메시지 처리
        }
    }
}
