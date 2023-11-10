package bdbe.bdbd._core.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

public class ApiUtils {

    public static <T> ApiResult<T> success(T response) {
        return new ApiResult<>(true, response, null);
    }

    public static ApiResult<?> error(String status, String code, String message) {
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
        private final String message;
    }}