package bdbe.bdbd._core.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Map;

public class ApiUtils {

    public static <T> ApiResult<T> success(T response) {
        return new ApiResult<>(true, response, null);
    }

    public static ApiResult<?> error(String status, String code, String message) {
        return new ApiResult<>(false, null, new ApiError(status, code, message));
    }

    public static ApiResult<?> error(String status, String code, Map<String, String> errors) {
        return new ApiResult<>(false, null, new ApiError(status, code, null, errors));
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
        private final String message;
        private Map<String, String> errors;

        public ApiError(String status, String code, String message) {
            this.status = status;
            this.code = code;
            this.message = message;
            this.errors = null;
        }
    }
}