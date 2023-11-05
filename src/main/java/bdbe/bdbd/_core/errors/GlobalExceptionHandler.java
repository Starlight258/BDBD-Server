package bdbe.bdbd._core.errors;


import bdbe.bdbd._core.errors.exception.*;
import bdbe.bdbd._core.errors.utils.ApiUtils;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestError.class)
    public ResponseEntity<?> badRequest(BadRequestError e){
        return new ResponseEntity<>(e.body(), e.status());
    }

    @ExceptionHandler(UnAuthorizedError.class)
    public ResponseEntity<?> unAuthorized(UnAuthorizedError e){
        return new ResponseEntity<>(e.body(), e.status());
    }

    @ExceptionHandler(ForbiddenError.class)
    public ResponseEntity<?> forbidden(ForbiddenError e){
        return new ResponseEntity<>(e.body(), e.status());
    }

    @ExceptionHandler(NotFoundError.class)
    public ResponseEntity<?> notFound(NotFoundError e){
        return new ResponseEntity<>(e.body(), e.status());
    }

    @ExceptionHandler(InternalServerError.class)
    public ResponseEntity<?> serverError(InternalServerError e){
        return new ResponseEntity<>(e.body(), e.status());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        String message = String.format("The parameter '%s' of value '%s' could not be converted to type '%s'",
                e.getName(), e.getValue(), e.getRequiredType().getSimpleName());
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("success", false);
        errorBody.put("error", Collections.singletonMap("message", message));
        return new ResponseEntity<>(errorBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingParams(MissingServletRequestParameterException ex) {
        String name = ex.getParameterName();
        String type = ex.getParameterType();
        String message = String.format("The required parameter '%s' of type '%s' is missing", name, type);

        Map<String, String> errorBody = new HashMap<>();
        errorBody.put("error", message);

        return new ResponseEntity<>(errorBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> unknownServerError(Exception e){
        ApiUtils.ApiResult<?> apiResult = ApiUtils.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(apiResult, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Request has invalid or missing fields.");
    }

    @ExceptionHandler(AmazonServiceException.class)
    public ResponseEntity<?> handleAmazonServiceException(AmazonServiceException ex) {
        String errorMsg = String.format("AmazonServiceException: %s", ex.getErrorMessage());
        ApiUtils.ApiResult<?> apiResult = ApiUtils.error(errorMsg, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(apiResult, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SdkClientException.class)
    public ResponseEntity<?> handleSdkClientException(SdkClientException ex) {
        String errorMsg = String.format("SdkClientException: %s", ex.getMessage());
        ApiUtils.ApiResult<?> apiResult = ApiUtils.error(errorMsg, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(apiResult, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
