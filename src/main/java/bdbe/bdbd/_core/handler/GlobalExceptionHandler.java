package bdbe.bdbd._core.handler;


import bdbe.bdbd._core.exception.*;
import bdbe.bdbd._core.utils.ApiUtils;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestError.class)
    public ResponseEntity<?> badRequest(BadRequestError e){
        ApiUtils.ApiResult<?> errorResult = ApiUtils.error(e.getMessage(), e.status());

        return new ResponseEntity<>(errorResult, e.status());
    }


    @ExceptionHandler(UnAuthorizedError.class)
    public ResponseEntity<?> unAuthorized(UnAuthorizedError e) {
        ApiUtils.ApiResult<?> errorResult = ApiUtils.error(e.getMessage(), e.status());

        return new ResponseEntity<>(errorResult, e.status());
    }

    @ExceptionHandler(ForbiddenError.class)
    public ResponseEntity<?> forbidden(ForbiddenError e){
        ApiUtils.ApiResult<?> errorResult = ApiUtils.error(e.getMessage(), e.status());

        return new ResponseEntity<>(errorResult, e.status());
    }

    @ExceptionHandler(NotFoundError.class)
    public ResponseEntity<?> notFound(NotFoundError e){
        ApiUtils.ApiResult<?> errorResult = ApiUtils.error(e.getMessage(), e.status());

        return new ResponseEntity<>(errorResult, e.status());
    }

    @ExceptionHandler(InternalServerError.class)
    public ResponseEntity<?> serverError(InternalServerError e){
        ApiUtils.ApiResult<?> errorResult = ApiUtils.error(e.getMessage(), e.status());

        return new ResponseEntity<>(errorResult, e.status());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        ApiUtils.ApiResult<?> errorResult = ApiUtils.error(e.getMessage(), HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(errorResult,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        String message = String.format("The parameter '%s' of value '%s' could not be converted to type '%s'",
                e.getName(), e.getValue(), e.getRequiredType().getSimpleName());
        ApiUtils.ApiResult<?> errorResult = ApiUtils.error(message, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        String errorMessage = errors.entrySet()
                .stream()
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.joining(","));
        ApiUtils.ApiResult<?> errorResult = ApiUtils.error(errorMessage, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingParams(MissingServletRequestParameterException ex) {
        String name = ex.getParameterName();
        String type = ex.getParameterType();
        String message = String.format("The required parameter '%s' of type '%s' is missing", name, type);
        ApiUtils.ApiResult<?> errorResult = ApiUtils.error(message, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(errorResult,HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<?> handleMissingServletRequestPartException(MissingServletRequestPartException e) {
        ApiUtils.ApiResult<?> errorResult = ApiUtils.error(e.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> unknownServerError(Exception e){
        ApiUtils.ApiResult<?> errorResult = ApiUtils.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(errorResult, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        ApiUtils.ApiResult<?> errorResult = ApiUtils.error("Request has invalid or missing fields.", HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
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
