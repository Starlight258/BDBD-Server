package bdbe.bdbd._core.handler;


import bdbe.bdbd._core.exception.BadRequestError;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.stream.Collectors;

@Aspect
@Component
public class GlobalValidationHandler {
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping) || @annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void postOrPutMapping() {
    }

    @Before("postOrPutMapping()")
    public void validationAdvice(JoinPoint jp) {
        Object[] args = jp.getArgs();
        for (Object arg : args) {
            if (arg instanceof Errors) {
                Errors errors = (Errors) arg;

                if (errors.hasErrors()) {
                    BadRequestError.ErrorCode errorCode = BadRequestError.ErrorCode.VALIDATION_FAILED;
                    String detailedErrorMessage = errors.getAllErrors()
                            .stream()
                            .map(error -> String.format("%s:%s", error.getDefaultMessage(), ((FieldError) error).getField()))
                            .collect(Collectors.joining(", "));

                    throw new BadRequestError(errorCode, detailedErrorMessage);
                }
            }
        }
    }
}
