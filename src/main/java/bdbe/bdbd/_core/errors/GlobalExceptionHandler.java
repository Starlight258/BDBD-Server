package bdbe.bdbd._core.errors;


import bdbe.bdbd._core.errors.exception.*;
import bdbe.bdbd._core.errors.utils.ApiUtils;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> unknownServerError(Exception e){
        ApiUtils.ApiResult<?> apiResult = ApiUtils.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(apiResult, HttpStatus.INTERNAL_SERVER_ERROR);
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
